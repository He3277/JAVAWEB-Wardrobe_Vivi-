// API 调用模块
const API = {
    // 通用请求方法
    async request(url, options = {}) {
        const headers = {
            'Content-Type': 'application/json'
        };

        const token = Store.getToken();
        if (token) {
            headers['token'] = token;
        }

        const config = {
            method: 'GET',
            headers: headers,
            ...options
        };

        try {
            const response = await fetch(CONFIG.API_BASE_URL + url, config);
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('API request error:', error);
            throw error;
        }
    },

    // GET 请求
    async get(url, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const fullUrl = queryString ? `${url}?${queryString}` : url;
        return await this.request(fullUrl, { method: 'GET' });
    },

    // POST 请求
    async post(url, data = {}) {
        return await this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    // 获取所有服装风格
    allStyles() {
        return this.get('/allStyles');
    },

    // 获取所有服装
    allClothes(params = {}) {
        return this.get('/allClothes', params);
    },

    // 根据名称查询服装
    clothesByName(clothesName) {
        return this.get('/clothesByName', { clothesName });
    },

    // 获取服装详情
    clothDetails(clothId) {
        return this.get('/clothDetails', { clothId });
    },

    // 登录
    login(userInfo, password) {
        return this.get('/login', { userInfo, password });
    },

    // 注册
    register(data) {
        return this.post('/register', data);
    },

    // 添加到购物车
    addToCart(clothId, clothSize, userId) {
        return this.post('/addToCart', { clothId, clothSize, userId });
    },

    // 获取用户购物车数据
    getCartDataByUser(userId) {
        return this.get('/getCartDataByUser', { userId });
    },

    // 更新购物车数据
    updateCartData(id, amount) {
        return this.post('/updateCartData', { id, amount });
    },

    // 删除购物车数据
    delCartData(id) {
        return this.post('/delCartData', { id });
    },

    // 提交订单
    addOrder(orderList) {
        return this.post('/addOrder', orderList);
    },

    // 获取用户订单
    getOrderByUser(userId, status) {
        return this.get('/getOrderByUser', { userId, status });
    },

    // 支付订单
    payOrder(id) {
        return this.post('/payOrder', { id });
    },

    // 确认收货
    receiveOrder(id) {
        return this.post('/receiveOrder', { id });
    },

    // 删除订单
    delOrderData(id) {
        return this.post('/delOrderData', { id });
    },

    // 获取当前用户信息
    getCurrentUser(id) {
        return this.get('/getCurrentUser', { id });
    },

    // 更新用户信息
    updateUser(data) {
        return this.post('/updateUser', data);
    }
};
