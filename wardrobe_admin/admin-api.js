// 管理员后台 API 调用模块
const AdminAPI = {
    // 通用请求方法
    async request(url, options = {}) {
        const headers = {
            'Content-Type': 'application/json'
        };

        const token = AdminUtils.getToken();
        if (token) {
            headers['token'] = token;
        }

        const config = {
            method: 'GET',
            headers: headers,
            ...options
        };

        try {
            const response = await fetch(ADMIN_CONFIG.API_BASE_URL + url, config);
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

    // 登录
    login(userInfo, password) {
        return this.get('/login', { userInfo, password, isAdminLogin: true });
    },

    // 获取所有服装
    getAllClothes() {
        return this.get('/getAllClothesData');
    },

    // 获取所有类型
    allTypes() {
        return this.get('/allTypes');
    },

    // 获取所有风格
    allStyles() {
        return this.get('/allStyles');
    },

    // 搜索服装
    searchClothes(params) {
        return this.get('/searchClothes', params);
    },

    // 删除服装
    delClothes(id) {
        return this.post('/delClothes', { id });
    },

    // 添加服装
    addClothes(data) {
        return this.post('/addClothes', data);
    },

    // 更新服装
    updateClothes(data) {
        return this.post('/editClothes', data);
    },

    // 获取所有用户
    getAllUsers() {
        return this.get('/getAllUser');
    },

    // 删除用户
    delUser(id) {
        return this.post('/delUser', { id });
    },

    // 添加用户
    addUser(data) {
        return this.post('/addUser', data);
    },

    // 更新用户
    updateUser(data) {
        return this.post('/editUser', data);
    },

    // 获取所有订单
    getAllOrders() {
        return this.get('/allOrderData');
    },

    // 更新订单状态（发货）
    updateOrderStatus(id, status) {
        return this.post('/deliveryOrder', { id });
    },

    // 删除订单
    delOrder(id) {
        return this.post('/delOrderData', { id });
    },

    // 上传图片
    uploadImage(file) {
        const formData = new FormData();
        formData.append('clothesImage', file);
        return fetch(ADMIN_CONFIG.API_BASE_URL + '/uploadFile', {
            method: 'POST',
            headers: {
                'token': AdminUtils.getToken()
            },
            body: formData
        }).then(response => response.json());
    }
};
