// 底部组件 - 适用于多页应�?const FooterComponent = {
    // 生成底部 HTML
    getHTML() {
        return `
            <footer class="el-footer">
                <div class="footer">
                    <div class="footer-container">
                        <p>&copy; 2026 Vivi Style。All rights reserved.</p>
                    </div>
                </div>
            </footer>
        `;
    },

    // 渲染底部
    render() {
        const footerContainer = document.getElementById('footer-container');
        if (footerContainer) {
            footerContainer.innerHTML = this.getHTML();
        }
    }
};
