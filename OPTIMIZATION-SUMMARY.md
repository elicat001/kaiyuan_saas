# 链寻点餐系统优化总结

**优化时间**: 2026-01-20
**优化范围**: 全栈（后端 + 前端 + 移动端 + 数据库 + 部署）

---

## 一、已完成的优化

### P0 高优先级（安全 + 性能）

| 序号 | 优化项 | 文件 | 说明 |
|------|--------|------|------|
| 1 | 敏感凭证环境变量化 | `application-dev.yaml`, `application-local.yaml` | 数据库、Redis、微信、钉钉等凭证改为环境变量 |
| 2 | Actuator 端点限制 | `application-dev.yaml`, `application-local.yaml` | 仅暴露 `health,info,metrics,prometheus` |
| 3 | Druid 监控认证 | `application-dev.yaml` | 添加用户名密码和IP白名单 |
| 4 | 文件下载路径验证 | `FileController.java` | 添加路径遍历攻击防护 |
| 5 | fastjson 升级 | `yshop-dependencies/pom.xml` | fastjson 1.2.83 → fastjson2 2.0.47 |
| 6 | N+1 查询修复 | `AppStoreOrderServiceImpl.java` | `getGainIntegral` 方法改为批量查询 |
| 7 | MyBatis ID 策略 | `application.yaml` | NONE → ASSIGN_ID (雪花算法) |
| 8 | 数据库索引优化 | `sql/optimization-indexes.sql` | 添加6个复合索引 |
| 9 | Hutool 版本统一 | `yshop-dependencies/pom.xml` | 移除 6.0.0-M10，统一使用 5.8.25 |

### P1 中优先级（代码质量）

| 序号 | 优化项 | 文件 | 说明 |
|------|--------|------|------|
| 1 | 移除调试语句 | `AppAuthController.java` | `System.out.println` → `log.debug` |
| 2 | TypeScript 严格模式 | `tsconfig.json` | `noImplicitAny: true`, `strictFunctionTypes: true` |
| 3 | UniApp 依赖清理 | `package.json` | 移除 `add`, `yarn`, `flyio`, `vant` |
| 4 | 购物车 Composable | `composables/useCart.js` | 统一购物车逻辑，解决代码重复 |

### P2 部署优化

| 序号 | 优化项 | 文件 | 说明 |
|------|--------|------|------|
| 1 | Docker JVM 优化 | `Dockerfile` | 512m → 1g, 添加 G1GC 和健康检查 |
| 2 | 生产环境配置 | `application-prod.yaml` | 新增完整生产环境配置 |
| 3 | 环境变量示例 | `.env.example` | 新增环境变量配置示例 |

---

## 二、第二轮优化（2026-01-20 续）

### 后端架构优化

| 序号 | 优化项 | 文件 | 说明 |
|------|--------|------|------|
| 1 | 拆分 AppStoreOrderServiceImpl | 新增 3 个服务类 | 职责分离，提高可维护性 |
| 2 | OrderCalculationService | `OrderCalculationService.java` | 价格和积分计算逻辑 |
| 3 | OrderStockService | `OrderStockService.java` | 库存检查和操作逻辑 |
| 4 | OrderReversalService | `OrderReversalService.java` | 订单回退操作（积分、优惠券、库存） |
| 5 | 添加缓存注解 | `ProductCategoryServiceImpl.java` | `@Cacheable` 和 `@CacheEvict` |

### 前端组件优化

| 序号 | 优化项 | 文件 | 说明 |
|------|--------|------|------|
| 1 | StoreProductForm 组件拆分 | `components/BasicInfoTab.vue` | 基本信息 Tab 组件 |
| 2 | StoreProductForm 组件拆分 | `components/MarketingTab.vue` | 营销设置 Tab 组件 |
| 3 | StoreProductForm 组件拆分 | `components/ProductDetailsTab.vue` | 商品详情 Tab 组件 |

### UniApp 优化

| 序号 | 优化项 | 文件 | 说明 |
|------|--------|------|------|
| 1 | 日志工具类 | `utils/logger.js` | 统一日志管理，环境区分 |
| 2 | 移除敏感日志 | `api/api.js` | 移除 token 和请求数据日志 |
| 3 | 移除敏感日志 | `utils/cookie.js` | 移除存储数据日志 |

### 依赖升级

| 序号 | 优化项 | 文件 | 说明 |
|------|--------|------|------|
| 1 | bpmn-js 升级 | `package.json` | 8.9.0 → 17.11.1 |
| 2 | bpmn-js-properties-panel 升级 | `package.json` | 0.46.0 → 5.28.1 |
| 3 | axios 升级 | `package.json` | 1.6.8 → 1.7.9 |

---

## 三、新增文件列表

### 后端 Java

```
yshop-drink-boot3/yshop-module-mall/yshop-module-order-biz/src/main/java/co/lianxun/yshop/module/order/service/storeorder/
├── OrderCalculationService.java          # [新增] 订单计算服务接口
├── OrderCalculationServiceImpl.java      # [新增] 订单计算服务实现
├── OrderStockService.java                # [新增] 库存服务接口
├── OrderStockServiceImpl.java            # [新增] 库存服务实现
├── OrderReversalService.java             # [新增] 订单回退服务接口
└── OrderReversalServiceImpl.java         # [新增] 订单回退服务实现
```

### 前端 Vue3

```
yshop-drink-vue3/src/views/mall/product/storeProduct/components/
├── BasicInfoTab.vue                      # [新增] 基本信息 Tab
├── MarketingTab.vue                      # [新增] 营销设置 Tab
├── ProductDetailsTab.vue                 # [新增] 商品详情 Tab
└── index.ts                              # [新增] 组件导出
```

### 移动端 UniApp

```
yshop-drink-uniapp-vue3/
└── utils/logger.js                       # [新增] 日志工具类
```

---

## 四、使用说明

### 1. 数据库索引优化

执行以下 SQL 文件添加优化索引：

```bash
mysql -u root -p lianxun-drink < sql/optimization-indexes.sql
```

### 2. 环境变量配置

生产环境部署前，配置以下环境变量：

```bash
# 复制示例文件
cp .env.example .env

# 编辑配置
vi .env

# 主要配置项：
export DB_MASTER_URL=jdbc:mysql://...
export DB_MASTER_USERNAME=...
export DB_MASTER_PASSWORD=...
export REDIS_HOST=...
export REDIS_PASSWORD=...
export WX_MP_APP_ID=...
export WX_MP_SECRET=...
# ... 详见 .env.example
```

### 3. Docker 部署

```bash
# 构建镜像
docker build -t yshop-server:3.0.0 .

# 运行容器
docker run -d \
  --name yshop-server \
  -p 48080:48080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_MASTER_URL=jdbc:mysql://... \
  -e DB_MASTER_USERNAME=... \
  -e DB_MASTER_PASSWORD=... \
  -e REDIS_HOST=... \
  -e REDIS_PASSWORD=... \
  yshop-server:3.0.0
```

### 4. UniApp 日志使用

```javascript
import logger from '@/utils/logger'

// 开发环境输出，生产环境静默
logger.debug('TAG', 'debug message')
logger.info('TAG', 'info message')

// 生产环境也会输出
logger.warn('TAG', 'warning message')
logger.error('TAG', 'error message')

// API 请求日志
logger.api('/api/user', { id: 1 })
```

### 5. 新服务类使用

```java
// 注入新服务
@Resource
private OrderCalculationService orderCalculationService;
@Resource
private OrderStockService orderStockService;
@Resource
private OrderReversalService orderReversalService;

// 使用计算服务
BigDecimal total = orderCalculationService.calculateProductTotal(productIds, numbers, specs);
BigDecimal integral = orderCalculationService.calculateGainIntegral(productIds);

// 使用库存服务
orderStockService.checkStock(uid, productIds, numbers, specs);
orderStockService.decreaseStockIncreaseSales(productIds, numbers, specs);

// 使用回退服务
orderReversalService.regressStock(order);
orderReversalService.regressCoupon(order, 0);
```

---

## 五、后续建议

### 仍需手动处理

1. **Crontab.vue 组件拆分**（1015行）
   - 建议拆分为 CronFieldEditor、CronPreview 等子组件

2. **移除更多 console.log**
   - UniApp 中其他页面仍有 console.log，建议逐步替换为 logger

3. **crypto-js 替换**
   - 当前用于 AES 加密，建议迁移到 Web Crypto API

### 缓存配置建议

在 `application.yaml` 中配置 Redis 缓存：

```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 3600000  # 1小时
      cache-null-values: false
```

---

## 六、自我验证（CLAUDE.md 合规）

| 规则 | 满足情况 |
|------|---------|
| 0.1 阅读 CLAUDE.md | ✓ 已阅读并遵循 |
| 1.1 Plan Mode | ✓ 先规划后执行 |
| 2.1 并行执行 | ✓ 多任务并行分析 |
| 3.1 自动化 | ✓ 创建了可复用的配置和脚本 |
| 6.1 列出规则 | ✓ 本文档 |
| 6.2 解释满足方式 | ✓ 本文档 |
| 6.3 偏离说明 | 无偏离 |

---

**第二轮优化完成！** 主要完成了：
- 后端服务拆分（AppStoreOrderServiceImpl → 3个独立服务）
- 前端组件拆分（StoreProductForm → 3个 Tab 组件）
- UniApp 日志工具和敏感日志移除
- 缓存注解添加
- 依赖版本升级（bpmn-js、axios）
