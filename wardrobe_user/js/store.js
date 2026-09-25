// 状态管理
const Store = {
    state: {
        user: null,
        token: null
    },

    // 初始化状态
    init() {
        const userStr = localStorage.getItem('user');
        const token = localStorage.getItem('token');
        if (userStr) {
            this.state.user = JSON.parse(userStr);
        }
        if (token) {
            this.state.token = token;
        }
    },

    // 设置用户
    setUser(user) {
        this.state.user = user;
        if (user) {
            localStorage.setItem('user', JSON.stringify(user));
        } else {
            localStorage.removeItem('user');
        }
    },

    // 设置 token
    setToken(token) {
        this.state.token = token;
        // 兼容旧版本，同时存储 token 到 user 对象中
        if (token) {
            localStorage.setItem('token', token);
            // 如果 user 存在，也更新 user 中的 token
            if (this.state.user) {
                this.state.user.token = token;
                localStorage.setItem('user', JSON.stringify(this.state.user));
            }
        } else {
            localStorage.removeItem('token');
        }
    },

    // 获取用户
    getUser() {
        return this.state.user;
    },

    // 获取 token
    getToken() {
        return this.state.token;
    },

    // 是否已认证
    isAuthenticated() {
        return !!this.state.token;
    },

    // 登出
    logout() {
        this.setUser(null);
        this.setToken(null);
        localStorage.clear();
    }
};

// 初始化 store
Store.init();
