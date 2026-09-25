// 管理员后台工具函数
const AdminUtils = {
    // 显示提示信息
    showMessage(message, type = 'info') {
        alert(message);
    },

    // 确认对话框
    confirm(message) {
        return window.confirm(message);
    },

    // 获取用户信息
    getUser() {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    },

    // 设置用户信息
    setUser(user) {
        localStorage.setItem('user', JSON.stringify(user));
    },

    // 获取token
    getToken() {
        const user = this.getUser();
        return user ? user.token : null;
    },

    // 检查是否已登录
    isAuthenticated() {
        return this.getToken() !== null;
    },

    // 退出登录
    logout() {
        localStorage.clear();
        window.location.href = 'login.html';
    },

    // 表单验证 - 必填
    validateRequired(value) {
        return value && value.trim().length > 0;
    },

    // 格式化价格
    formatPrice(price) {
        return '¥' + parseFloat(price).toFixed(2);
    }
};
