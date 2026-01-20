package co.yixiang.yshop.module.order.service.storeorder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单计算服务 - 负责价格和积分计算
 *
 * @author yshop
 */
public interface OrderCalculationService {

    /**
     * 计算商品总价
     *
     * @param productIds 商品ID列表
     * @param numbers    商品数量列表
     * @param specs      商品规格列表
     * @return 商品总价
     */
    BigDecimal calculateProductTotal(List<String> productIds, List<String> numbers, List<String> specs);

    /**
     * 计算奖励积分
     *
     * @param productIds 商品ID列表
     * @return 奖励积分总和
     */
    BigDecimal calculateGainIntegral(List<String> productIds);

    /**
     * 计算最终支付价格
     *
     * @param sumPrice       商品总价
     * @param postagePrice   运费
     * @param couponPrice    优惠券抵扣
     * @param deductionPrice 积分抵扣
     * @param orderType      订单类型
     * @return 最终支付价格
     */
    BigDecimal calculatePayPrice(BigDecimal sumPrice, BigDecimal postagePrice,
                                  BigDecimal couponPrice, BigDecimal deductionPrice, Integer orderType);
}
