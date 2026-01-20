-- ========================================
-- YShop-Drink 数据库索引优化脚本
-- 创建时间: 2026-01-20
-- 说明: 添加复合索引以优化查询性能
-- ========================================

-- 使用数据库
USE `yixiang-drink-open`;

-- ========================================
-- 1. 订单表 (yshop_store_order) 索引优化
-- ========================================

-- 添加用户订单状态复合索引，优化订单列表查询
-- 场景：用户查看不同状态的订单列表
ALTER TABLE `yshop_store_order`
ADD INDEX `idx_uid_status_paid_refund` (`uid`, `status`, `paid`, `refund_status`);

-- 添加创建时间和用户ID复合索引，优化时间范围查询
-- 场景：按时间范围查询用户订单
ALTER TABLE `yshop_store_order`
ADD INDEX `idx_create_time_uid` (`create_time`, `uid`);

-- 添加已支付和创建时间复合索引
-- 场景：统计已支付订单
ALTER TABLE `yshop_store_order`
ADD INDEX `idx_paid_create_time` (`paid`, `create_time`);

-- 添加门店ID和支付状态复合索引
-- 场景：门店订单统计
ALTER TABLE `yshop_store_order`
ADD INDEX `idx_shop_id_paid_status` (`shop_id`, `paid`, `status`);

-- ========================================
-- 2. 商品表 (yshop_store_product) 索引优化
-- ========================================

-- 添加门店ID和上架状态复合索引
-- 场景：查询门店的上架商品
ALTER TABLE `yshop_store_product`
ADD INDEX `idx_shop_id_is_show` (`shop_id`, `is_show`);

-- 添加上架状态和创建时间复合索引
-- 场景：按时间排序查询上架商品
ALTER TABLE `yshop_store_product`
ADD INDEX `idx_is_show_create_time` (`is_show`, `create_time`);

-- 添加上架状态和销量复合索引
-- 场景：按销量排序查询热门商品
ALTER TABLE `yshop_store_product`
ADD INDEX `idx_is_show_sales` (`is_show`, `sales`);

-- 删除低效的单字段布尔索引（可选，需要评估影响）
-- 注意：执行前请备份数据库
-- ALTER TABLE `yshop_store_product` DROP INDEX `is_hot`;
-- ALTER TABLE `yshop_store_product` DROP INDEX `is_benefit`;
-- ALTER TABLE `yshop_store_product` DROP INDEX `is_best`;
-- ALTER TABLE `yshop_store_product` DROP INDEX `is_new`;
-- ALTER TABLE `yshop_store_product` DROP INDEX `is_postage`;

-- ========================================
-- 3. 商品属性值表 (yshop_store_product_attr_value) 索引优化
-- ========================================

-- 添加商品ID和SKU复合索引
-- 场景：根据商品ID和规格查询价格
ALTER TABLE `yshop_store_product_attr_value`
ADD INDEX `idx_product_id_sku` (`product_id`, `sku`);

-- ========================================
-- 4. 订单购物车信息表 (yshop_store_order_cart_info) 索引优化
-- ========================================

-- 添加订单ID索引（如果不存在）
-- 场景：查询订单商品详情
ALTER TABLE `yshop_store_order_cart_info`
ADD INDEX `idx_oid` (`oid`);

-- ========================================
-- 5. 用户账单表 (yshop_user_bill) 索引优化
-- ========================================

-- 添加扩展字段索引，用于订单关联查询
ALTER TABLE `yshop_user_bill`
ADD INDEX `idx_extend_field` (`extend_field`);

-- 添加用户ID和类型复合索引
-- 场景：查询用户特定类型的账单
ALTER TABLE `yshop_user_bill`
ADD INDEX `idx_uid_type` (`uid`, `type`);

-- ========================================
-- 6. 优惠券用户表 (yshop_coupon_user) 索引优化
-- ========================================

-- 添加用户ID和状态复合索引
-- 场景：查询用户可用优惠券
ALTER TABLE `yshop_coupon_user`
ADD INDEX `idx_user_id_status` (`user_id`, `status`);

-- ========================================
-- 执行完成提示
-- ========================================
SELECT 'Index optimization completed successfully!' AS message;
