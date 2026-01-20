package co.yixiang.yshop.module.order.service.storeorder;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.module.order.enums.OrderLogEnum;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import co.yixiang.yshop.module.product.service.storeproduct.AppStoreProductService;
import co.yixiang.yshop.module.product.service.storeproductattrvalue.StoreProductAttrValueService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单计算服务实现类
 *
 * @author yshop
 */
@Slf4j
@Service
public class OrderCalculationServiceImpl implements OrderCalculationService {

    @Resource
    private StoreProductAttrValueService storeProductAttrValueService;

    @Resource
    private AppStoreProductService appStoreProductService;

    @Override
    public BigDecimal calculateProductTotal(List<String> productIds, List<String> numbers, List<String> specs) {
        BigDecimal sumPrice = BigDecimal.ZERO;

        for (int i = 0; i < productIds.size(); i++) {
            String newSku = StrUtil.replace(specs.get(i), "|", ",");

            StoreProductAttrValueDO storeProductAttrValue = storeProductAttrValueService
                    .getOne(Wrappers.<StoreProductAttrValueDO>lambdaQuery()
                            .eq(StoreProductAttrValueDO::getSku, newSku)
                            .eq(StoreProductAttrValueDO::getProductId, productIds.get(i)));

            if (storeProductAttrValue != null) {
                sumPrice = NumberUtil.add(sumPrice, NumberUtil.mul(numbers.get(i),
                        storeProductAttrValue.getPrice().toString()));
            }
        }

        return sumPrice;
    }

    @Override
    public BigDecimal calculateGainIntegral(List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 批量查询所有商品，避免 N+1 查询问题
        List<Long> ids = productIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
        List<StoreProductDO> products = appStoreProductService.listByIds(ids);

        // 计算积分总和
        return products.stream()
                .filter(product -> product.getGiveIntegral() != null && product.getGiveIntegral().intValue() > 0)
                .map(StoreProductDO::getGiveIntegral)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculatePayPrice(BigDecimal sumPrice, BigDecimal postagePrice,
                                         BigDecimal couponPrice, BigDecimal deductionPrice, Integer orderType) {
        BigDecimal payPrice;

        // 外卖订单需要加运费
        if (OrderLogEnum.ORDER_TAKE_OUT.getValue().equals(orderType)) {
            payPrice = NumberUtil.sub(NumberUtil.add(sumPrice, postagePrice), couponPrice, deductionPrice);
        } else {
            payPrice = NumberUtil.sub(sumPrice, couponPrice, deductionPrice);
        }

        // 确保支付金额不为负数
        if (payPrice.compareTo(BigDecimal.ZERO) < 0) {
            payPrice = BigDecimal.ZERO;
        }

        return payPrice;
    }
}
