// 工具函数
const Utils = {
    // 获取 URL 查询参数
    getQueryParam(name) {
        const hash = window.location.hash || '#';
        const queryString = hash.split('?')[1];
        if (!queryString) return null;
        
        const urlParams = new URLSearchParams(queryString);
        return urlParams.get(name);
    },

    // 格式化货币
    formatCurrency(amount) {
        return '￥' + parseFloat(amount).toFixed(2);
    },

    // 格式化日期时间
    formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },

    // 显示提示信息
    showAlert(message, type = 'info') {
        // 移除已存在的提示
        const existingAlert = document.querySelector('.alert');
        if (existingAlert) {
            existingAlert.remove();
        }

        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;
        document.body.insertBefore(alertDiv, document.body.firstChild);
        
        setTimeout(() => {
            alertDiv.remove();
        }, 3000);
    },

    // 验证手机号
    validatePhone(phone) {
        return /^1[34578]\d{9}$/.test(phone);
    },

    // 验证必填项
    validateRequired(value) {
        return value && value.trim().length > 0;
    },

    // 表单序列化
    serializeForm(formId) {
        const form = document.getElementById(formId);
        const formData = new FormData(form);
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });
        return data;
    }
};
