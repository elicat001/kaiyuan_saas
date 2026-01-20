package co.yixiang.yshop.module.order.service.storeorder;

import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import co.yixiang.yshop.module.order.service.storeordercartinfo.StoreOrderCartInfoService;
import co.yixiang.yshop.module.product.service.storeproduct.AppStoreProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 订单库存服务实现类
 *
 * @author yshop
 */
@Slf4j
@Service
public class OrderStockServiceImpl implements OrderStockService {

    private static final String CHECK_STOCK_LOCK_KEY = "order:check:stock:lock";
    private static final String OPERATE_STOCK_LOCK_KEY = "order:operate:stock:lock";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private AppStoreProductService appStoreProductService;

    @Resource
    private StoreOrderCartInfoService storeOrderCartInfoService;

    @Override
    public void checkStock(Long uid, List<String> productIds, List<String> numbers, List<String> specs) {
        RLock lock = redissonClient.getLock(CHECK_STOCK_LOCK_KEY);
        if (lock.tryLock()) {
            try {
                for (int i = 0; i < productIds.size(); i++) {
                    String newSku = StrUtil.replace(specs.get(i), "|", ",");
                    appStoreProductService.checkProductStock(uid, Long.valueOf(productIds.get(i)),
                            Integer.valueOf(numbers.get(i)), newSku);
                }
            } catch (Exception ex) {
                log.error("[checkStock][执行异常]", ex);
                throw exception(new ErrorCode(999999, ex.getMessage()));
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void decreaseStockIncreaseSales(List<String> productIds, List<String> numbers, List<String> specs) {
        log.info("========减库存增加销量start=========");
        RLock lock = redissonClient.getLock(OPERATE_STOCK_LOCK_KEY);
        if (lock.tryLock()) {
            try {
                for (int i = 0; i < productIds.size(); i++) {
                    String newSku = StrUtil.replace(specs.get(i), "|", ",");
                    appStoreProductService.decProductStock(Integer.valueOf(numbers.get(i)),
                            Long.valueOf(productIds.get(i)),
                            newSku, 0L, "");
                }
            } catch (Exception ex) {
                log.error("[decreaseStockIncreaseSales][执行异常]", ex);
                throw exception(new ErrorCode(999999, ex.getMessage()));
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void restoreStock(Long orderId) {
        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderCartInfoDO::getOid, orderId);

        List<StoreOrderCartInfoDO> cartInfoList = storeOrderCartInfoService.list(wrapper);
        for (StoreOrderCartInfoDO cartInfo : cartInfoList) {
            String newSku = StrUtil.replace(cartInfo.getSpec(), "|", ",");
            appStoreProductService.incProductStock(cartInfo.getNumber(), cartInfo.getProductId(),
                    newSku, 0L, null);
        }
    }
}
