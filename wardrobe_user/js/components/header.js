// 头部组件 - 适用于多页应用
const HeaderComponent = {
    // 获取当前页面名称
    getCurrentPage() {
        const path = window.location.pathname;
        const page = path.split('/').pop() || 'home.html';
        return page;
    },

    // 生成头部 HTML
    getHTML(activePage) {
        const user = Store.getUser();
        const isLoggedIn = Store.isAuthenticated();
        
        return `
            <header class="el-header">
                <ul class="el-menu">
                    <li class="el-menu-item index-0">
                        <img style="height: 40px;" src="assets/logo.png" alt="Logo"/>
                    </li>
                    <li class="el-menu-item index-1 ${activePage === 'home.html' ? 'active' : ''}">
                        <a href="home.html" style="text-decoration: none; color: #fff;">首页</a>
                    </li>
                    <li class="el-submenu index-3">
                        <span>风格筛选</span>
                        <div class="submenu-dropdown" id="styles-dropdown"></div>
                    </li>
                    <li class="el-menu-item index-7 ${activePage === 'cart.html' ? 'active' : ''}" onclick="location.href='cart.html'">购物车</li>
                    <li class="el-menu-item index-8 ${activePage === 'order.html' ? 'active' : ''}" onclick="location.href='order.html'">我的订单</li>
                    <li class="el-menu-item index-6 ${activePage === 'personal.html' ? 'active' : ''}" onclick="location.href='personal.html'">个人中心</li>
                    ${!isLoggedIn ? `
                        <li class="el-menu-item index-4 reg ${activePage === 'register.html' ? 'active' : ''}" onclick="location.href='register.html'">注册</li>
                        <li class="el-menu-item index-5 login ${activePage === 'login.html' ? 'active' : ''}" onclick="location.href='login.html'">登录</li>
                    ` : `
                        <li class="el-submenu index-5 login" id="user-menu">
                            <span id="user-name-display">${user ? user.userName : ''}，欢迎你</span>
                            <div class="submenu-dropdown">
                                <div class="submenu-dropdown-item" onclick="HeaderComponent.logout()">退出登录</div>
                            </div>
                        </li>
                    `}
                </ul>
            </header>
        `;
    },

    // 渲染头部
    render(activePage) {
        const headerContainer = document.getElementById('header-container');
        if (headerContainer) {
            headerContainer.innerHTML = this.getHTML(activePage);
            this.loadStyles();
        }
    },

    // 加载风格
    async loadStyles() {
        try {
            const stylesData = await API.allStyles();
            const stylesDropdown = document.getElementById('styles-dropdown');
            if (stylesData && stylesData.length > 0) {
                stylesDropdown.innerHTML = stylesData.map(style => 
                    `<div class="submenu-dropdown-item" onclick="HeaderComponent.filterByStyle('${style}')">${style}</div>`
                ).join('');
            }
        } catch (error) {
            console.error('Failed to load styles:', error);
        }
    },

    // 按风格筛选
    filterByStyle(style) {
        window.location.href = `home.html?style=${encodeURIComponent(style)}`;
    },



    // 登出
    logout() {
        Store.logout();
        location.reload();
    }
};
