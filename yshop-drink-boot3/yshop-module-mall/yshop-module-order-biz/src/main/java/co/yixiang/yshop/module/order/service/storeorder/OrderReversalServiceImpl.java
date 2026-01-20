package co.yixiang.yshop.module.order.service.storeorder;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.module.coupon.dal.dataobject.couponuser.CouponUserDO;
import co.yixiang.yshop.module.coupon.service.couponuser.AppCouponUserService;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.enums.BillDetailEnum;
import co.yixiang.yshop.module.member.service.user.MemberUserService;
import co.yixiang.yshop.module.member.service.userbill.UserBillService;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import co.yixiang.yshop.module.order.dal.mysql.storeorder.StoreOrderMapper;
import co.yixiang.yshop.module.order.enums.OrderStatusEnum;
import co.yixiang.yshop.module.order.service.storeordercartinfo.StoreOrderCartInfoService;
import co.yixiang.yshop.module.product.service.storeproduct.AppStoreProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单回退服务实现类
 *
 * @author yshop
 */
@Slf4j
@Service
public class OrderReversalServiceImpl implements OrderReversalService {

    @Resource
    private MemberUserService userService;

    @Resource
    private UserBillService billService;

    @Resource
    private AppCouponUserService appCouponUserService;

    @Resource
    private StoreOrderCartInfoService storeOrderCartInfoService;

    @Resource
    private AppStoreProductService appStoreProductService;

    @Resource
    private StoreOrderMapper storeOrderMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regressIntegral(AppStoreOrderQueryVo order, Integer type) {
        if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(order.getPaid())
                || OrderStatusEnum.STATUS_MINUS_2.getValue().equals(order.getStatus())) {
            return;
        }

        if (order.getPayIntegral().compareTo(BigDecimal.ZERO) > 0) {
            order.setUseIntegral(order.getPayIntegral());
        }
        if (order.getUseIntegral().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        if (!OrderStatusEnum.STATUS_MINUS_2.getValue().equals(order.getStatus())
                && !OrderInfoEnum.REFUND_STATUS_2.getValue().equals(order.getRefundStatus())
                && order.getBackIntegral().compareTo(BigDecimal.ZERO) > 0) {
            return;
        }

        MemberUserDO user = userService.getById(order.getUid());

        // 增加积分
        BigDecimal newIntegral = NumberUtil.add(order.getUseIntegral(), user.getIntegral());
        user.setIntegral(newIntegral);
        userService.updateById(user);

        // 增加流水
        billService.income(user.getId(), "积分回退", BillDetailEnum.CATEGORY_2.getValue(),
                BillDetailEnum.TYPE_8.getValue(),
                order.getUseIntegral().doubleValue(),
                newIntegral.doubleValue(),
                "购买商品失败,回退积分" + order.getUseIntegral(), order.getId().toString());

        // 更新回退积分
        StoreOrderDO storeOrder = new StoreOrderDO();
        storeOrder.setBackIntegral(order.getUseIntegral());
        storeOrder.setId(order.getId());
        storeOrderMapper.updateById(storeOrder);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regressCoupon(AppStoreOrderQueryVo order, Integer type) {
        if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(order.getPaid())
                || OrderStatusEnum.STATUS_MINUS_2.getValue().equals(order.getStatus())) {
            return;
        }

        if (order.getCouponId() != null && order.getCouponId() > 0) {
            CouponUserDO couponUser = appCouponUserService
                    .getOne(Wrappers.<CouponUserDO>lambdaQuery()
                            .eq(CouponUserDO::getId, order.getCouponId())
                            .eq(CouponUserDO::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                            .eq(CouponUserDO::getUserId, order.getUid()));

            if (ObjectUtil.isNotNull(couponUser)) {
                couponUser.setStatus(ShopCommonEnum.IS_STATUS_0.getValue());
                appCouponUserService.updateById(couponUser);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regressStock(AppStoreOrderQueryVo order) {
        if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(order.getPaid())
                || OrderStatusEnum.STATUS_MINUS_2.getValue().equals(order.getStatus())) {
            return;
        }

        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderCartInfoDO::getOid, order.getId());

        List<StoreOrderCartInfoDO> cartInfoList = storeOrderCartInfoService.list(wrapper);
        for (StoreOrderCartInfoDO cartInfo : cartInfoList) {
            String newSku = StrUtil.replace(cartInfo.getSpec(), "|", ",");
            appStoreProductService.incProductStock(cartInfo.getNumber(), cartInfo.getProductId(),
                    newSku, 0L, null);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void grantUserIntegral(AppStoreOrderQueryVo order) {
        if (order.getGainIntegral().compareTo(BigDecimal.ZERO) > 0) {
            MemberUserDO user = userService.getUser(order.getUid());

            BigDecimal newIntegral = NumberUtil.add(user.getIntegral(), order.getGainIntegral());
            user.setIntegral(newIntegral);
            user.setId(order.getUid());
            userService.updateById(user);

            // 增加流水
            billService.income(user.getId(), "购买商品赠送积分", BillDetailEnum.CATEGORY_2.getValue(),
                    BillDetailEnum.TYPE_9.getValue(),
                    order.getGainIntegral().doubleValue(),
                    newIntegral.doubleValue(),
                    "购买商品赠送" + order.getGainIntegral() + "积分", order.getId().toString());
        }
    }
}
