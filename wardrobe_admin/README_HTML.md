# Vivi Style后台管理系统 - 纯HTML版本

## 项目说明

本项目是�?Vue 项目的重构版本，已将所�?Vue 组件转换为纯 HTML + JavaScript 实现，不再依�?Vue 构建工具�?
## 技术栈

- **Vue 3** (通过 CDN 引入)
- **Element Plus** (通过 CDN 引入)
- **原生 JavaScript**
- **Fetch API** 进行网络请求

## 文件结构

```
wardrobe_admin/
├── admin-config.js          # 配置文件（API地址等）
├── admin-utils.js           # 工具函数（认证、验证等�?├── admin-api.js             # API 调用模块
├── login.html               # 登录页面
├── home.html                # 主框架页面（包含导航和路由）
├── clothes-manage.html      # 服装管理页面
├── order-manage.html        # 订单管理页面
├── user-manage.html         # 用户管理页面
└── src/assets/img/          # 图片资源目录
```

## 功能模块

### 1. 登录模块 (login.html)
- 管理员登�?- 表单验证
- Token 存储

### 2. 主框�?(home.html)
- 侧边栏导航菜�?- 页面切换（使�?iframe 加载子页面）
- 用户信息显示
- 退出登�?
### 3. 服装管理 (clothes-manage.html)
- 查看所有服装列�?- 多条件搜索（名称、类别、风格）
- 添加服装（含图片上传�?- 编辑服装信息
- 删除/下架服装
- 分页显示

### 4. 订单管理 (order-manage.html)
- 查看所有订�?- 按用户名和状态搜�?- 发货操作
- 分页显示

### 5. 用户管理 (user-manage.html)
- 查看所有用�?- 按用户名或手机号搜索
- 添加新用�?- 编辑用户信息
- 删除/注销用户
- 分页显示

## 使用方法

### 1. 启动后端服务
确保 Tomcat 服务器已启动，并且项目在正确的端口上运行（默�?8082）�?
### 2. 访问系统
在浏览器中访问：
```
http://localhost:8082/wardrobe_admin/login.html
```

### 3. 登录
使用管理员账号登录（role = 1 的用户）�?
## 配置说明

### 修改 API 地址
编辑 `admin-config.js` 文件�?```javascript
const ADMIN_CONFIG = {
    API_BASE_URL: 'http://localhost:8082/wardrobe_back',
    IMAGE_BASE_URL: '/wardrobe_back/images/',
    TIMEOUT: 10000
};
```

## 主要特�?
1. **无需构建工具**：直接在浏览器中运行，无需 npm、webpack �?2. **CDN 依赖**：所有第三方库通过 CDN 加载
3. **响应式设�?*：使�?Element Plus 组件�?4. **模块化代�?*：配置、工具、API 分离
5. **完整�?CRUD 操作**：支持增删改查所有功�?
## 注意事项

1. **网络连接**：需要联网以加载 CDN 资源（Vue、Element Plus�?2. **浏览器兼容�?*：建议使用现代浏览器（Chrome、Firefox、Edge�?3. **Token 认证**：所�?API 请求会自动携�?token
4. **图片上传**：确保后�?uploadFile 接口正常工作

## 与原 Vue 项目的区�?
| 特�?| �?Vue 项目 | 当前 HTML 版本 |
|------|------------|---------------|
| 构建工具 | 需�?Vite/Webpack | 无需构建 |
| 路由 | Vue Router | iframe + 手动切换 |
| 状态管�?| Vuex | localStorage |
| 组件�?| .vue 单文件组�?| 独立 HTML 文件 |
| 开发复杂度 | 较高 | 简单直�?|

## 常见问题

### Q: 页面无法加载�?A: 检查以下几点：
1. Tomcat 是否正常运行
2. 端口号是否正确（默认 8082�?3. 浏览器控制台是否有错误信�?
### Q: 登录后跳转到首页失败�?A: 检查：
1. 登录接口是否返回正确的用户信�?2. localStorage 是否正常存储 token
3. home.html 路径是否正确

### Q: 图片无法显示�?A: 确认�?1. 图片是否成功上传到服务器
2. IMAGE_BASE_URL 配置是否正确
3. images 目录是否有读取权�?
## 开发者信�?
- 原项目：Vue 3 + Element Plus + Vite
- 重构为：�?HTML + CDN 方式
- 重构时间�?026�?
## 许可�?
本项目仅供学习使用�?