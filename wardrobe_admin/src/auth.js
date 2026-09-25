// router.js中用到
export default {
    //用于检查用户是否登录，token不为空，已经登录，返回true
    isAuthenticated () {
        // 这里应该根据你的应用逻辑来检查用户是否登录
        // 检查本地存储（localStorage）中是否有token
        return localStorage.getItem('token') !== null
    }
}