# 链寻点餐系统

<p align="center">
  <strong>SaaS 多租户扫码点餐解决方案</strong>
</p>

<p align="center">
  <a href="#技术栈"><img src="https://img.shields.io/badge/Java-17-blue.svg" alt="Java 17"></a>
  <a href="#技术栈"><img src="https://img.shields.io/badge/Spring%20Boot-3.2-green.svg" alt="Spring Boot 3.2"></a>
  <a href="#技术栈"><img src="https://img.shields.io/badge/Vue-3.x-brightgreen.svg" alt="Vue 3"></a>
  <a href="#技术栈"><img src="https://img.shields.io/badge/UniApp-Vue3-blue.svg" alt="UniApp"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="MIT License"></a>
</p>

---

## 简介

链寻点餐是一套完整的 **SaaS 多租户扫码点餐系统**，支持外卖、自取、堂食等多种点餐模式。采用前后端分离架构，提供管理后台、商家端、用户端（微信小程序 + H5）全端解决方案。

### 核心特性

| 特性 | 说明 |
|------|------|
| **多门店管理** | 支持连锁品牌多门店独立运营 |
| **SaaS 多租户** | 一套系统服务多个商户，数据隔离 |
| **扫码点餐** | 桌台二维码点餐，支持单人/多人协同 |
| **外卖自取** | 支持外卖配送和到店自取模式 |
| **收银台** | 支持扫码枪、扫码盒子收款 |
| **云小票打印** | 对接云打印机，自动出票 |
| **会员体系** | 会员卡、积分、优惠券、充值 |
| **营销工具** | 满减、折扣、积分兑换等 |

---

## 项目结构

```
lianxun-drink/
├── yshop-drink-boot3/          # 后端 Java 工程 (Spring Boot 3)
│   ├── yshop-server/           # 主启动模块
│   ├── yshop-module-mall/      # 商城模块（商品、订单、门店）
│   ├── yshop-module-member/    # 会员模块
│   ├── yshop-module-system/    # 系统模块（用户、权限、配置）
│   ├── yshop-framework/        # 框架层
│   └── sql/                    # 数据库脚本
├── yshop-drink-vue3/           # 管理后台 (Vue 3 + Element Plus)
└── yshop-drink-uniapp-vue3/    # 移动端 (UniApp Vue3，支持小程序/H5)
```

---

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | LTS 版本 |
| Spring Boot | 3.2 | 基础框架 |
| Spring Security | 6.x | 认证授权 (OAuth2) |
| MyBatis-Plus | 3.5 | ORM 框架 |
| Redis | 6+ | 缓存/会话 |
| MySQL | 8.0 | 数据库 |
| Druid | 1.2 | 连接池 |
| Hutool | 5.8 | 工具库 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.x | 渐进式框架 |
| Element Plus | 2.x | UI 组件库 |
| Vite | 5.x | 构建工具 |
| Pinia | 2.x | 状态管理 |
| TypeScript | 5.x | 类型支持 |

### 移动端

| 技术 | 说明 |
|------|------|
| UniApp (Vue3) | 跨端框架 |
| uv-ui | UI 组件库 |
| 微信小程序 | 主要投放渠道 |
| H5 | 公众号/浏览器 |

---

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 6+
- Node.js 16+
- Maven 3.8+
- pnpm 8+

### 1. 后端启动

```bash
# 1. 克隆项目
git clone https://github.com/elicat001/kaiyuan_saas.git
cd kaiyuan_saas

# 2. 创建数据库并导入
mysql -u root -p -e "CREATE DATABASE lianxun_drink DEFAULT CHARSET utf8mb4"
mysql -u root -p lianxun_drink < yshop-drink-boot3/sql/lianxun-drink.sql

# 3. 修改配置
# 编辑 yshop-drink-boot3/yshop-server/src/main/resources/application-local.yaml
# 配置数据库连接和 Redis 连接

# 4. 构建并启动
cd yshop-drink-boot3
mvn clean install -DskipTests
cd yshop-server
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

后端启动后访问：http://localhost:48080

### 2. 管理后台启动

```bash
cd yshop-drink-vue3

# 安装依赖
pnpm install

# 配置 API 地址 (.env.dev)
# VITE_BASE_URL='http://localhost:48080'

# 启动开发服务器
pnpm dev
```

管理后台访问：http://localhost:3000

### 3. 移动端启动

```bash
# 使用 HBuilderX 打开 yshop-drink-uniapp-vue3 目录

# 1. 配置 API 地址
#    编辑 config/app.js 中的 HTTP_REQUEST_URL

# 2. 运行到小程序
#    HBuilderX -> 运行 -> 运行到小程序模拟器 -> 微信开发者工具

# 3. 运行到 H5
#    HBuilderX -> 运行 -> 运行到浏览器
```

---

## 系统截图

### 小程序端

| 首页 | 商品详情 |
|:---:|:---:|
| ![首页](assets/1000.jpg) | ![商品详情](assets/1001.jpg) |

| 购物车 | 订单 |
|:---:|:---:|
| ![购物车](assets/200000.jpg) | ![订单](assets/1002.jpg) |

### 管理后台

| 控制台 |
|:---:|
| ![控制台](assets/3000.png) |

| 商品管理 |
|:---:|
| ![商品管理](assets/3001.png) |

| 订单管理 |
|:---:|
| ![订单管理](assets/3002.png) |

---

## 功能模块

### 商城管理
- 商品管理（多规格 SKU、分类、属性）
- 订单管理（外卖、自取、堂食）
- 门店管理（多门店、营业时间、配送范围）
- 桌台管理（二维码生成、桌台状态）

### 会员营销
- 会员管理（等级、积分、余额）
- 优惠券（满减券、折扣券、新人券）
- 充值活动（充值送、会员卡）
- 积分商城（积分兑换商品）

### 系统管理
- 用户权限（RBAC 权限模型）
- 租户管理（SaaS 多租户）
- 系统配置（支付、短信、存储）
- 操作日志（审计追踪）

### 硬件对接
- 云小票打印（飞鹅、易联云）
- 扫码设备（扫码枪、扫码盒子）
- 电子秤（称重计价）

---

## 部署指南

### Docker 部署

```bash
# 构建镜像
cd yshop-drink-boot3
docker build -t lianxun-drink:latest .

# 运行容器
docker run -d \
  --name lianxun-drink \
  -p 48080:48080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=your-db-host \
  -e DB_PASSWORD=your-db-password \
  -e REDIS_HOST=your-redis-host \
  lianxun-drink:latest
```

### Docker Compose 部署

```bash
cd yshop-drink-boot3/script/docker
docker-compose up -d
```

详细部署文档请参考：[Docker-HOWTO.md](yshop-drink-boot3/script/docker/Docker-HOWTO.md)

---

## 开发文档

- [优化记录](OPTIMIZATION-SUMMARY.md) - 系统优化历史
- [API 文档](http://localhost:48080/doc.html) - 后端启动后访问

---

## 开源协议

本项目采用 [MIT License](LICENSE) 开源协议，可免费用于商业项目。

---

## 鸣谢

- [ruoyi-vue-pro](https://gitee.com/zhijiantianya/ruoyi-vue-pro) - 基础框架
- [Element Plus](https://element-plus.org/) - Vue3 UI 组件库
- [uv-ui](https://www.uvui.cn/) - UniApp UI 组件库
- [pay-java-parent](https://gitee.com/egzosn/pay-java-parent) - 支付集成
