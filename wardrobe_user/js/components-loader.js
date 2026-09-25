// 组件加载器 - 用于在所有页面中统一加载头部和底部
const ComponentLoader = {
    // 初始化组件
    init(activePage) {
        // 渲染头部
        if (typeof HeaderComponent !== 'undefined') {
            HeaderComponent.render(activePage);
        }
        
        // 渲染底部
        if (typeof FooterComponent !== 'undefined') {
            FooterComponent.render();
        }
    }
};
