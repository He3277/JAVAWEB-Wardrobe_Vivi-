// AI穿搭助手组件 v2 - 支持拖拽缩放和商品链接
const AIAssistant = {
    isOpen: false,
    isSending: false,
    history: [],
    maxHistory: 10,

    // 拖拽相关状态
    dragState: {
        isDragging: false,
        startX: 0,
        startY: 0,
        startLeft: 0,
        startTop: 0
    },

    // 缩放相关状态
    resizeState: {
        isResizing: false,
        direction: '',
        startX: 0,
        startY: 0,
        startWidth: 0,
        startHeight: 0,
        startLeft: 0,
        startTop: 0
    },

    quickQuestions: [
        '推荐夏季穿搭',
        '适合约会的搭配',
        '休闲风格推荐',
        '有什么新品？'
    ],

    init() {
        this.createDOM();
        this.bindEvents();
        this.initDrag();
        this.initResize();
        this.loadProductCache();
    },

    // 预加载商品缓存，用于《商品名》格式的链接解析
    async loadProductCache() {
        try {
            const resp = await fetch(CONFIG.API_BASE_URL + '/allClothes');
            if (resp.ok) {
                this._productCache = await resp.json();
            }
        } catch (e) {
            // 忽略加载失败
        }
    },

    createDOM() {
        const floatBtn = document.createElement('button');
        floatBtn.className = 'ai-float-btn';
        floatBtn.id = 'ai-float-btn';
        floatBtn.innerHTML = '<span class="pulse"></span><span class="ai-icon">&#128172;</span>';
        floatBtn.title = 'AI穿搭助手';
        document.body.appendChild(floatBtn);

        const chatWindow = document.createElement('div');
        chatWindow.className = 'ai-chat-window';
        chatWindow.id = 'ai-chat-window';
        chatWindow.innerHTML = `
            <div class="ai-chat-header" id="ai-drag-handle">
                <div class="ai-avatar">&#129302;</div>
                <div class="ai-info">
                    <div class="ai-name">小V - AI穿搭顾问</div>
                    <div class="ai-status">在线 | 为你推荐穿搭</div>
                </div>
                <button class="ai-close-btn" id="ai-close-btn">&#10005;</button>
            </div>
            <div class="ai-chat-messages" id="ai-messages">
                <div class="ai-welcome">
                    <div class="welcome-icon">&#128084;</div>
                    <p>你好！我是小V，你的AI穿搭顾问<br>我可以为你推荐搭配、分析风格<br>有什么穿搭问题尽管问我~</p>
                </div>
            </div>
            <div class="ai-quick-questions" id="ai-quick-questions">
                ${this.quickQuestions.map(q => '<button class="ai-quick-btn" data-question="' + q + '">' + q + '</button>').join('')}
            </div>
            <div class="ai-chat-input">
                <input type="text" id="ai-input" placeholder="问我穿搭问题..." maxlength="200" autocomplete="off">
                <button class="ai-send-btn" id="ai-send-btn" title="发送">&#10148;</button>
            </div>
            <div class="ai-resize-handle se" data-dir="se"></div>
            <div class="ai-resize-handle sw" data-dir="sw"></div>
            <div class="ai-resize-handle ne" data-dir="ne"></div>
            <div class="ai-resize-handle nw" data-dir="nw"></div>
            <div class="ai-resize-handle n" data-dir="n"></div>
            <div class="ai-resize-handle s" data-dir="s"></div>
            <div class="ai-resize-handle e" data-dir="e"></div>
            <div class="ai-resize-handle w" data-dir="w"></div>
        `;
        document.body.appendChild(chatWindow);
    },

    bindEvents() {
        const self = this;

        document.getElementById('ai-float-btn').addEventListener('click', () => {
            self.toggle();
        });

        document.getElementById('ai-close-btn').addEventListener('click', () => {
            self.close();
        });

        document.getElementById('ai-send-btn').addEventListener('click', () => {
            self.handleSend();
        });

        document.getElementById('ai-input').addEventListener('keypress', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                self.handleSend();
            }
        });

        document.getElementById('ai-quick-questions').addEventListener('click', (e) => {
            if (e.target.classList.contains('ai-quick-btn')) {
                const question = e.target.getAttribute('data-question');
                document.getElementById('ai-input').value = question;
                self.handleSend();
            }
        });
    },

    // 初始化拖拽功能
    initDrag() {
        const self = this;
        const dragHandle = document.getElementById('ai-drag-handle');
        const chatWindow = document.getElementById('ai-chat-window');

        dragHandle.addEventListener('mousedown', (e) => {
            // 如果点击的是关闭按钮，不触发拖拽
            if (e.target.id === 'ai-close-btn') return;

            e.preventDefault();
            const rect = chatWindow.getBoundingClientRect();

            self.dragState.isDragging = true;
            self.dragState.startX = e.clientX;
            self.dragState.startY = e.clientY;
            self.dragState.startLeft = rect.left;
            self.dragState.startTop = rect.top;

            chatWindow.classList.add('dragging');
        });

        document.addEventListener('mousemove', (e) => {
            if (!self.dragState.isDragging) return;

            const dx = e.clientX - self.dragState.startX;
            const dy = e.clientY - self.dragState.startY;

            let newLeft = self.dragState.startLeft + dx;
            let newTop = self.dragState.startTop + dy;

            // 边界限制
            const maxX = window.innerWidth - chatWindow.offsetWidth;
            const maxY = window.innerHeight - chatWindow.offsetHeight;
            newLeft = Math.max(0, Math.min(newLeft, maxX));
            newTop = Math.max(0, Math.min(newTop, maxY));

            chatWindow.style.left = newLeft + 'px';
            chatWindow.style.top = newTop + 'px';
            chatWindow.style.right = 'auto';
            chatWindow.style.bottom = 'auto';
        });

        document.addEventListener('mouseup', () => {
            if (self.dragState.isDragging) {
                self.dragState.isDragging = false;
                chatWindow.classList.remove('dragging');
            }
        });
    },

    // 初始化缩放功能
    initResize() {
        const self = this;
        const chatWindow = document.getElementById('ai-chat-window');
        const handles = chatWindow.querySelectorAll('.ai-resize-handle');

        handles.forEach(handle => {
            handle.addEventListener('mousedown', (e) => {
                e.preventDefault();
                e.stopPropagation();

                const rect = chatWindow.getBoundingClientRect();
                const dir = handle.getAttribute('data-dir');

                self.resizeState.isResizing = true;
                self.resizeState.direction = dir;
                self.resizeState.startX = e.clientX;
                self.resizeState.startY = e.clientY;
                self.resizeState.startWidth = rect.width;
                self.resizeState.startHeight = rect.height;
                self.resizeState.startLeft = rect.left;
                self.resizeState.startTop = rect.top;
            });
        });

        document.addEventListener('mousemove', (e) => {
            if (!self.resizeState.isResizing) return;

            const dx = e.clientX - self.resizeState.startX;
            const dy = e.clientY - self.resizeState.startY;
            const dir = self.resizeState.direction;

            let newWidth = self.resizeState.startWidth;
            let newHeight = self.resizeState.startHeight;
            let newLeft = self.resizeState.startLeft;
            let newTop = self.resizeState.startTop;

            const minW = 320;
            const minH = 400;

            if (dir.includes('e')) {
                newWidth = Math.max(minW, self.resizeState.startWidth + dx);
            }
            if (dir.includes('w')) {
                newWidth = Math.max(minW, self.resizeState.startWidth - dx);
                if (newWidth > minW) {
                    newLeft = self.resizeState.startLeft + dx;
                }
            }
            if (dir.includes('s')) {
                newHeight = Math.max(minH, self.resizeState.startHeight + dy);
            }
            if (dir.includes('n')) {
                newHeight = Math.max(minH, self.resizeState.startHeight - dy);
                if (newHeight > minH) {
                    newTop = self.resizeState.startTop + dy;
                }
            }

            chatWindow.style.width = newWidth + 'px';
            chatWindow.style.height = newHeight + 'px';
            chatWindow.style.left = newLeft + 'px';
            chatWindow.style.top = newTop + 'px';
            chatWindow.style.right = 'auto';
            chatWindow.style.bottom = 'auto';
        });

        document.addEventListener('mouseup', () => {
            self.resizeState.isResizing = false;
        });
    },

    toggle() {
        if (this.isOpen) {
            this.close();
        } else {
            this.open();
        }
    },

    open() {
        this.isOpen = true;
        document.getElementById('ai-chat-window').classList.add('show');
        document.getElementById('ai-float-btn').classList.add('active');
        document.getElementById('ai-float-btn').querySelector('.ai-icon').innerHTML = '&#10005;';
        document.getElementById('ai-input').focus();
    },

    close() {
        this.isOpen = false;
        document.getElementById('ai-chat-window').classList.remove('show');
        document.getElementById('ai-float-btn').classList.remove('active');
        document.getElementById('ai-float-btn').querySelector('.ai-icon').innerHTML = '&#128172;';
    },

    handleSend() {
        if (this.isSending) return;

        const input = document.getElementById('ai-input');
        const message = input.value.trim();
        if (!message) return;

        input.value = '';
        this.sendMessage(message);
    },

    async sendMessage(message) {
        this.isSending = true;
        document.getElementById('ai-send-btn').disabled = true;

        const welcomeMsg = document.querySelector('#ai-messages .ai-welcome');
        if (welcomeMsg) welcomeMsg.remove();

        this.appendMessage('user', message);
        this.history.push({ role: 'user', content: message });

        if (this.history.length > this.maxHistory * 2) {
            this.history = this.history.slice(-this.maxHistory * 2);
        }

        const typingId = this.showTyping();

        try {
            const response = await fetch(CONFIG.API_BASE_URL + '/aiChat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json; charset=UTF-8' },
                body: JSON.stringify({
                    message: message,
                    history: this.history.slice(0, -1)
                })
            });

            if (!response.ok) {
                throw new Error('HTTP ' + response.status);
            }

            const reader = response.body.getReader();
            const decoder = new TextDecoder('utf-8');
            let fullResponse = '';
            let buffer = '';

            this.removeTyping(typingId);
            const msgElement = this.appendMessage('ai', '', true);

            while (true) {
                const { done, value } = await reader.read();
                if (done) break;

                buffer += decoder.decode(value, { stream: true });
                const lines = buffer.split('\n');
                buffer = lines.pop() || '';

                for (const line of lines) {
                    const trimmed = line.trim();
                    if (!trimmed || !trimmed.startsWith('data:')) continue;

                    try {
                        const jsonStr = trimmed.substring(5).trim();
                        if (!jsonStr) continue;
                        const data = JSON.parse(jsonStr);

                        if (data.error) {
                            this.updateMessageContent(msgElement, data.error, true);
                            break;
                        }

                        if (data.content) {
                            fullResponse += data.content;
                            this.updateMessageContent(msgElement, fullResponse);
                        }

                        if (data.done) {
                            break;
                        }
                    } catch (e) {
                        // skip parse errors
                    }
                }
            }

            if (fullResponse) {
                this.history.push({ role: 'assistant', content: fullResponse });
            } else {
                this.updateMessageContent(msgElement, '抱歉，AI未能生成回复，请重试。', true);
            }

        } catch (error) {
            console.error('AI request failed:', error);
            this.removeTyping(typingId);
            this.appendMessage('ai', '抱歉，AI服务暂时不可用，请确认Ollama正在运行后再试。', false, true);
        } finally {
            this.isSending = false;
            document.getElementById('ai-send-btn').disabled = false;
            document.getElementById('ai-input').focus();
        }
    },

    appendMessage(role, content, isStreaming, isError) {
        const messagesDiv = document.getElementById('ai-messages');
        const msgDiv = document.createElement('div');
        msgDiv.className = 'ai-message ' + role;

        const avatar = role === 'ai' ? '&#129302;' : '&#128100;';

        let htmlContent;
        if (isStreaming) {
            htmlContent = '<span class="ai-typing"><span></span><span></span><span></span></span>';
        } else if (role === 'ai') {
            htmlContent = this.formatAIResponse(content);
        } else {
            htmlContent = this.escapeHtml(content);
        }

        msgDiv.innerHTML = '<div class="msg-avatar">' + avatar + '</div><div class="msg-content">' + htmlContent + '</div>';

        if (isError) {
            msgDiv.querySelector('.msg-content').classList.add('ai-error');
        }

        messagesDiv.appendChild(msgDiv);
        this.scrollToBottom();
        return msgDiv;
    },

    updateMessageContent(msgElement, content, isError) {
        const contentDiv = msgElement.querySelector('.msg-content');
        contentDiv.innerHTML = this.formatAIResponse(content);
        if (isError) {
            contentDiv.classList.add('ai-error');
        }
        this.scrollToBottom();
    },

    // 格式化AI回复 - 解析商品链接
    formatAIResponse(text) {
        if (!text) return '';

        let escaped = this.escapeHtml(text);

        // 解析格式: [商品:商品名:ID] - 校验ID与名称是否匹配
        escaped = escaped.replace(/\[商品:([^:]+):(\d+)\]/g, (match, name, id) => {
            const validId = this.validateProductId(name, id);
            if (validId) {
                return '<a class="ai-product-link" href="details.html?clothId=' + validId + '" title="点击查看「' + name + '」详情">' + name + '</a>';
            }
            // ID不匹配，尝试通过名称查找
            const foundId = this.findProductIdByName(name);
            if (foundId) {
                return '<a class="ai-product-link" href="details.html?clothId=' + foundId + '" title="点击查看「' + name + '」详情">' + name + '</a>';
            }
            // 找不到对应商品，只显示文字不加链接
            return '<span class="ai-product-text">' + name + '</span>';
        });

        // 解析格式: 【商品名】(ID:123) - 同样校验
        escaped = escaped.replace(/【([^】]+)】\(ID:(\d+)\)/g, (match, name, id) => {
            const validId = this.validateProductId(name, id);
            if (validId) {
                return '<a class="ai-product-link" href="details.html?clothId=' + validId + '" title="点击查看「' + name + '」详情">' + name + '</a>';
            }
            const foundId = this.findProductIdByName(name);
            if (foundId) {
                return '<a class="ai-product-link" href="details.html?clothId=' + foundId + '" title="点击查看「' + name + '」详情">' + name + '</a>';
            }
            return '<span class="ai-product-text">' + name + '</span>';
        });

        // 解析格式: 《商品名》 转换为可点击链接（通过商品名查找）
        escaped = escaped.replace(/《([^》]+)》/g, (match, name) => {
            const productId = this.findProductIdByName(name);
            if (productId) {
                return '<a class="ai-product-link" href="details.html?clothId=' + productId + '" title="点击查看「' + name + '」详情">' + name + '</a>';
            }
            return match;
        });

        return escaped;
    },

    // 校验商品ID与名称是否匹配
    validateProductId(name, id) {
        if (!this._productCache) return id; // 缓存未加载时信任AI给出的ID
        const product = this._productCache.find(p => String(p.id) === String(id));
        if (!product) return null; // ID不存在
        // 检查名称是否匹配（允许部分匹配）
        if (product.clothName === name || product.clothName.includes(name) || name.includes(product.clothName)) {
            return product.id;
        }
        // ID对应的商品名称完全不匹配，返回null让调用方尝试按名称查找
        return null;
    },

    // 根据商品名查找ID（支持模糊匹配）
    findProductIdByName(name) {
        if (!this._productCache) return null;
        // 精确匹配
        let product = this._productCache.find(p => p.clothName === name);
        if (product) return product.id;
        // 包含匹配
        product = this._productCache.find(p => p.clothName.includes(name) || name.includes(p.clothName));
        if (product) return product.id;
        return null;
    },

    showTyping() {
        const messagesDiv = document.getElementById('ai-messages');
        const typingDiv = document.createElement('div');
        const typingId = 'typing-' + Date.now();
        typingDiv.id = typingId;
        typingDiv.className = 'ai-message ai';
        typingDiv.innerHTML = '<div class="msg-avatar">&#129302;</div><div class="ai-typing"><span></span><span></span><span></span></div>';
        messagesDiv.appendChild(typingDiv);
        this.scrollToBottom();
        return typingId;
    },

    removeTyping(typingId) {
        const typingDiv = document.getElementById(typingId);
        if (typingDiv) typingDiv.remove();
    },

    scrollToBottom() {
        const messagesDiv = document.getElementById('ai-messages');
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    },

    escapeHtml(text) {
        if (!text) return '';
        return text
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/\n/g, '<br>');
    }
};
