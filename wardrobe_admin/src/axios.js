import axios from 'axios'

//使用axios函数创建一个可以发送请求的实例对象
const axiosInstance = axios.create({
  timeout : 5000
})

// 添加请求拦截器（如果已经登录过，就携带上token）
axiosInstance.interceptors.request.use(config => {
  config.headers['Content-Type'] = 'application/json;charset=utf-8'
  const user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
  if (user) {
    config.headers.token = user.token
  }
  return config
}, error => {
  return Promise.reject(error)
})

export default axiosInstance

