import { createRouter, createWebHashHistory } from 'vue-router'
import Login from './components/Login.vue'
import Home from './components/Home.vue'
import Welcome from './components/Welcome.vue'
import Clothes from './components/ClothesManage/ClothesManage.vue'
import Order from './components/OrderManage/OrderManage.vue'
import User from './components/UserManage/UserManage.vue'

const router = createRouter({
    history: createWebHashHistory(),
    routes : [
        {
            path: '/',
            redirect: '/home'
        },
        {
            path: '/login',
            name: 'login',
            meta: {
                title: '登录'
            },
            component: Login,
        },
        {
            path: '/home',
            name: '主页',
            meta: {
                title: '主页',
                requiresAuth: true
            },
            component: Home,
            redirect: '/index',
            children: [
                {
                    path: '/index',
                    meta: {
                        title: '首页'
                    },
                    component: Welcome
                },
                {
                    path: '/clothes',
                    meta: {
                        title: '服装管理',
                        requiresAuth: true
                    },
                    component: Clothes,
                },
                {
                    path: '/order',
                    meta: {
                        title: '订单管理',
                        requiresAuth: true
                    },
                    component: Order,
                },
                {
                    path: '/user',
                    meta: {
                        title: '用户管理',
                        requiresAuth: true
                    },
                    component: User
                }
            ]
        },
    ]
})
// 挂载路由导航守卫：to表示将要访问的路径，from表示从哪里来，next是下一个要做的操作
router.beforeEach((to, from, next) => {
    const auth = require('./auth').default // 引入认证守卫
    // 判断当前路由是否需要认证
    console.log("路由守卫")
    if (to.matched.some(record => record.meta.requiresAuth)) {
        console.log("需要认证")
        // 如果需要认证，但用户未登录，则重定向到登录页面（或者弹出提示）
        if (!auth.isAuthenticated()) {
            next('login')
        } else {
            next(); // 已登录，正常进入路由
        }
    } else {
        // 如果不需要认证，则直接进入路由
        next();
    }
  })
  
// 导出路由
export default router