# 项目文件结构说明

## 📁 当前项目结构

```
wardrobe_user/
│
├── 📄 核心 HTML 页面
│   ├── index.html                  # 入口页面（可配置重定向）
│   ├── home.html                   # 首页 - 商品列表展示
│   ├── details.html                # 商品详情页
│   ├── login.html                  # 用户登录页
│   ├── register.html               # 用户注册页
│   ├── cart.html                   # 购物车页
│   ├── order.html                  # 订单管理页
│   └── personal.html               # 个人中心页
│
├── 📁 css/                         # 样式文件目录
│   └── style.css                   # 全局样式文件
│
├── 📁 js/                          # JavaScript 文件目录
│   ├── 📁 components/              # 公共组件目录
│   │   ├── header.js              # 头部导航组件
│   │   └── footer.js              # 底部组件
│   │
│   ├── components-loader.js        # 组件加载器
│   ├── config.js                   # 配置文件（API地址等）
│   ├── api.js                      # API 调用模块
│   ├── store.js                    # 状态管理（localStorage）
│   └── utils.js                    # 工具函数
│
├── 📁 assets/                      # 资源文件目录
│   └── logo.png                    # 网站 Logo
│
├── 📄 favicon.ico                  # 网站图标
│
└── 📄 文档文件
    ├── README.md                   # 项目主文档 ⭐
    ├── QUICK_START.md              # 快速开始指南 ⭐
    ├── REFACTORING_SUMMARY.md      # 重构总结
    ├── CLEANUP_CHECKLIST.md        # 清理清单
    └── cleanup.bat                 # 清理脚本（Windows）⭐
```

## 📝 文件说明

### HTML 页面

| 文件 | 说明 | 主要功能 |
|------|------|----------|
| `index.html` | 入口页面 | 可配置为跳转到 home.html |
| `home.html` | 首页 | 商品列表、搜索、按类型/风格筛选 |
| `details.html` | 商品详情 | 查看商品详情、选择尺码、加入购物车 |
| `login.html` | 登录页 | 用户登录、表单验证 |
| `register.html` | 注册页 | 用户注册、表单验证 |
| `cart.html` | 购物车 | 查看购物车、修改数量、删除商品、结算 |
| `order.html` | 订单页 | 查看订单、支付、收货、删除订单 |
| `personal.html` | 个人中心 | 查看/修改个人信息、修改密码 |

### JavaScript 模块

#### 核心模块

| 文件 | 说明 | 用途 |
|------|------|------|
| `config.js` | 配置文件 | 配置 API 地址、图片路径等 |
| `api.js` | API 模块 | 封装所有后端 API 调用 |
| `store.js` | 状态管理 | 管理用户信息、token、登录状态 |
| `utils.js` | 工具函数 | 提供常用工具方法（格式化、验证等） |

#### 组件模块

| 文件 | 说明 | 用途 |
|------|------|------|
| `components/header.js` | 头部组件 | 生成统一的头部导航菜单 |
| `components/footer.js` | 底部组件 | 生成统一的底部信息 |
| `components-loader.js` | 组件加载器 | 简化组件初始化流程 |

### CSS 文件

| 文件 | 说明 |
|------|------|
| `css/style.css` | 包含所有页面的样式定义，包括布局、颜色、字体等 |

## 🔧 如何添加新页面

1. **创建 HTML 文件**
   ```html
   <!DOCTYPE html>
   <html lang="zh-CN">
   <head>
       <meta charset="UTF-8">
       <title>新页面标题</title>
       <link rel="stylesheet" href="css/style.css">
   </head>
   <body>
       <div id="app">
           <!-- 头部容器 -->
           <div id="header-container"></div>

           <!-- 主要内容 -->
           <main id="main-container">
               <!-- 您的页面内容 -->
           </main>

           <!-- 底部容器 -->
           <div id="footer-container"></div>
       </div>

       <!-- 引入 JS 文件 -->
       <script src="js/config.js"></script>
       <script src="js/utils.js"></script>
       <script src="js/store.js"></script>
       <script src="js/api.js"></script>
       <script src="js/components/header.js"></script>
       <script src="js/components/footer.js"></script>
       <script src="js/components-loader.js"></script>

       <script>
           window.addEventListener('DOMContentLoaded', function() {
               // 初始化组件
               ComponentLoader.init('new-page.html');
               
               // 页面特定的初始化代码
               // ...
           });
       </script>
   </body>
   </html>
   ```

2. **在头部导航中添加链接**
   
   编辑 `js/components/header.js`，在 `getHTML()` 方法中添加新页面的导航链接。

3. **添加样式（如需要）**
   
   在 `css/style.css` 中添加新页面所需的样式。

## 🗑️ 待清理的文件

运行 `cleanup.bat` 后将删除以下文件和目录：

### 目录
- `src/` - Vue 源代码
- `public/` - Vue 公共资源
- `dist/` - Vue 构建输出
- `node_modules/` - Node.js 依赖
- `.idea/` - IDE 配置
- `js/pages/` - 旧的 SPA 页面模块

### 配置文件
- `vue.config.js` - Vue CLI 配置（已删除）
- `babel.config.js` - Babel 配置（已删除）
- `jsconfig.json` - JS 配置（已删除）
- `package.json` - Node 包配置（已删除）
- `package-lock.json` - Node 包锁定（已删除）

### 旧文件
- `js/app.js` - 旧的 SPA 入口
- `js/router.js` - 旧的路由管理器
- `README_NATIVE.md` - 旧文档
- `HTML_VERSION_README.md` - 旧文档
- `启动说明.html` - 旧文档
- `文件清单.txt` - 旧文档
- `项目说明_会话记录.txt` - 旧文档

## 📊 文件大小参考

| 类型 | 文件数 | 总大小（约） |
|------|--------|-------------|
| HTML 页面 | 8 | ~40 KB |
| JavaScript | 7 | ~15 KB |
| CSS | 1 | ~15 KB |
| 文档 | 5 | ~25 KB |
| **总计** | **21** | **~95 KB** |

*注意：清理后将删除 node_modules（约 200MB+）和其他不需要的文件*

## 🎯 整合到后端的建议

### Spring Boot 项目
```
src/main/resources/
├── static/
│   ├── css/
│   ├── js/
│   └── assets/
└── templates/
    ├── home.html
    ├── details.html
    └── ...
```

### Node.js Express 项目
```
project/
├── public/
│   ├── css/
│   ├── js/
│   └── assets/
└── views/
    ├── home.html
    ├── details.html
    └── ...
```

### PHP 项目
```
project/
├── css/
├── js/
├── assets/
├── home.html
├── details.html
└── ...
```

## 💡 最佳实践

1. **保持模块化**：将功能分解到不同的 JS 文件中
2. **使用组件**：通过组件系统复用头部和底部
3. **统一样式**：所有样式写在 `style.css` 中
4. **错误处理**：在 API 调用时添加适当的错误处理
5. **用户体验**：添加加载提示和操作反馈

---

**最后更新**: 2026年5月9日  
**项目版本**: 原生 HTML 版本 v1.0
