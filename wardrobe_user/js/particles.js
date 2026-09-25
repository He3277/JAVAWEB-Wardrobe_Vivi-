// 粒子效果系统
const ParticleSystem = {
    canvas: null,
    ctx: null,
    particles: [],
    mouse: { x: null, y: null, radius: 150 },
    animationId: null,
    particleCount: 80,
    connectionDistance: 120,

    init() {
        this.createCanvas();
        this.createParticles();
        this.bindEvents();
        this.animate();
    },

    createCanvas() {
        this.canvas = document.createElement('canvas');
        this.canvas.id = 'particle-canvas';
        this.canvas.style.background = 'transparent';
        // 插入到body最前面
        if (document.body.firstChild) {
            document.body.insertBefore(this.canvas, document.body.firstChild);
        } else {
            document.body.appendChild(this.canvas);
        }
        this.ctx = this.canvas.getContext('2d');
        this.resize();
    },

    resize() {
        this.canvas.width = window.innerWidth;
        this.canvas.height = window.innerHeight;
    },

    createParticles() {
        this.particles = [];
        for (let i = 0; i < this.particleCount; i++) {
            this.particles.push({
                x: Math.random() * this.canvas.width,
                y: Math.random() * this.canvas.height,
                vx: (Math.random() - 0.5) * 0.8,
                vy: (Math.random() - 0.5) * 0.8,
                size: Math.random() * 2 + 1,
                opacity: Math.random() * 0.5 + 0.2,
                hue: Math.random() * 40 + 200 // 蓝色范围
            });
        }
    },

    bindEvents() {
        window.addEventListener('resize', () => {
            this.resize();
        });

        window.addEventListener('mousemove', (e) => {
            this.mouse.x = e.clientX;
            this.mouse.y = e.clientY;
        });

        window.addEventListener('mouseout', () => {
            this.mouse.x = null;
            this.mouse.y = null;
        });
    },

    drawParticle(p) {
        const ctx = this.ctx;
        ctx.beginPath();
        ctx.arc(p.x, p.y, p.size, 0, Math.PI * 2);
        ctx.fillStyle = `hsla(${p.hue}, 80%, 65%, ${p.opacity})`;
        ctx.fill();

        // 发光效果
        ctx.beginPath();
        ctx.arc(p.x, p.y, p.size * 3, 0, Math.PI * 2);
        const gradient = ctx.createRadialGradient(p.x, p.y, 0, p.x, p.y, p.size * 3);
        gradient.addColorStop(0, `hsla(${p.hue}, 80%, 65%, ${p.opacity * 0.4})`);
        gradient.addColorStop(1, `hsla(${p.hue}, 80%, 65%, 0)`);
        ctx.fillStyle = gradient;
        ctx.fill();
    },

    drawConnection(p1, p2, distance) {
        const ctx = this.ctx;
        const opacity = (1 - distance / this.connectionDistance) * 0.3;
        ctx.beginPath();
        ctx.moveTo(p1.x, p1.y);
        ctx.lineTo(p2.x, p2.y);
        ctx.strokeStyle = `rgba(64, 158, 255, ${opacity})`;
        ctx.lineWidth = 0.5;
        ctx.stroke();
    },

    update() {
        this.particles.forEach(p => {
            // 移动
            p.x += p.vx;
            p.y += p.vy;

            // 边界反弹
            if (p.x < 0 || p.x > this.canvas.width) p.vx *= -1;
            if (p.y < 0 || p.y > this.canvas.height) p.vy *= -1;

            // 鼠标交互
            if (this.mouse.x !== null && this.mouse.y !== null) {
                const dx = p.x - this.mouse.x;
                const dy = p.y - this.mouse.y;
                const dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < this.mouse.radius) {
                    const force = (this.mouse.radius - dist) / this.mouse.radius;
                    p.x += dx * force * 0.02;
                    p.y += dy * force * 0.02;
                    p.opacity = Math.min(1, p.opacity + 0.02);
                }
            }

            // 透明度恢复
            p.opacity = Math.max(0.2, p.opacity - 0.002);
        });
    },

    animate() {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

        // 绘制连接线
        for (let i = 0; i < this.particles.length; i++) {
            for (let j = i + 1; j < this.particles.length; j++) {
                const dx = this.particles[i].x - this.particles[j].x;
                const dy = this.particles[i].y - this.particles[j].y;
                const dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < this.connectionDistance) {
                    this.drawConnection(this.particles[i], this.particles[j], dist);
                }
            }
        }

        // 绘制粒子
        this.particles.forEach(p => this.drawParticle(p));

        this.update();
        this.animationId = requestAnimationFrame(() => this.animate());
    },

    destroy() {
        if (this.animationId) {
            cancelAnimationFrame(this.animationId);
        }
        if (this.canvas) {
            this.canvas.remove();
        }
    }
};

// 页面加载后初始化
document.addEventListener('DOMContentLoaded', () => {
    ParticleSystem.init();
});
