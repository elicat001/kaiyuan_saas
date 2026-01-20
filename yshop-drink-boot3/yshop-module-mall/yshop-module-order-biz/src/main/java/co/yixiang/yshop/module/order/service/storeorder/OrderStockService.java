package co.yixiang.yshop.module.order.service.storeorder;

import java.util.List;

/**
 * 订单库存服务 - 负责库存管理
 *
 * @author yshop
 */
public interface OrderStockService {

    /**
     * 检查商品库存
     *
     * @param uid        用户ID
     * @param productIds 商品ID列表
     * @param numbers    商品数量列表
     * @param specs      商品规格列表
     */
    void checkStock(Long uid, List<String> productIds, List<String> numbers, List<String> specs);

    /**
     * 减库存加销量
     *
     * @param productIds 商品ID列表
     * @param numbers    商品数量列表
     * @param specs      商品规格列表
     */
    void decreaseStockIncreaseSales(List<String> productIds, List<String> numbers, List<String> specs);

    /**
     * 回退库存
     *
     * @param orderId 订单ID
     */
    void restoreStock(Long orderId);
}
