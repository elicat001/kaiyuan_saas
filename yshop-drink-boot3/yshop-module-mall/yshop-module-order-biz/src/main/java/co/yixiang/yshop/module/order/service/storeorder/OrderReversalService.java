package co.yixiang.yshop.module.order.service.storeorder;

import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;

/**
 * 订单回退服务 - 负责退款相关的回退操作
 *
 * @author yshop
 */
public interface OrderReversalService {

    /**
     * 退回积分
     *
     * @param order 订单信息
     * @param type  类型
     */
    void regressIntegral(AppStoreOrderQueryVo order, Integer type);

    /**
     * 退回优惠券
     *
     * @param order 订单信息
     * @param type  类型
     */
    void regressCoupon(AppStoreOrderQueryVo order, Integer type);

    /**
     * 退回库存
     *
     * @param order 订单信息
     */
    void regressStock(AppStoreOrderQueryVo order);

    /**
     * 奖励积分给用户
     *
     * @param order 订单信息
     */
    void grantUserIntegral(AppStoreOrderQueryVo order);
}
