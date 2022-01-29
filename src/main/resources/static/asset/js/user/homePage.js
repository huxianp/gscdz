window.onload = function() {
    //	MathJax初始化
    initialMathJax();
    let chapters=document.getElementsByClassName("chapter");
    for(let i=chapters.length-1;i>chapters.length-4;--i){
        chapters[i].classList.add("chapter1");
        chapters[i].setAttribute("hidden",true);
    }
    let canvas = document.getElementById("canvas");
    let ctx = canvas.getContext("2d");
    function draw() {
        ctx.strokeStyle = "gray";
        ctx.moveTo(0, 0);
        ctx.lineTo(15, 20);
        ctx.lineTo(30, 0);
        ctx.moveTo(0, 10);
        ctx.lineTo(15, 30);
        ctx.lineTo(30, 10);
        ctx.stroke();
    }
    draw();
    let icon = document.getElementById("icon");
    let leftNav = document.querySelector(".leftNav");
    let stateOfIcon = "down";
    let chapter1 = document.querySelectorAll(".chapter1");
    icon.onclick = function() {
        if (stateOfIcon == "down") {
            leftNav.style.height = "500px";
            canvas.style.transform = "rotateX(180deg)";
            window.setTimeout(function() {
                for (let i = 0; i < chapter1.length; ++i) {
                    chapter1[i].removeAttribute("hidden");
                    console.log("false");
                }
            }, 500);
            stateOfIcon = "up";
        }
        else {
            leftNav.style.height = "360px";
            canvas.style.transform = "rotateX(0deg)";
            for (let j = 0; j < chapter1.length; ++j) {
                console.log("true");
                chapter1[j].setAttribute("hidden", true);
            }
            stateOfIcon = "down";
        }


    }
    //轮播图
    class Swiper {
        constructor() {
            this.w = $('.swiper-item').width();
            this.num = 0;//记录当前为第几张图片
            this.len = $('.swiper .swiper-item').length - 1;//图片的张数
            this.timer = null;
        }
        init() {
            //设置定时器
            this.setTime();
            //滑上停止定时器
            this.hover();
            //点击指示
            this.pointClick();
            //点击左右箭头
            this.lrClick();
        }
        setTime() {
            this.timer = setInterval(() => {
                this.num++;
                if (this.num > this.len) {
                    this.num = 0;
                }
                let cssTrx = -this.num * this.w;
                $('.swiper-point-item .point').eq(this.num).addClass('active').siblings().removeClass('active');
                $('.swiper').css({ transform: `translateX(${cssTrx}px)` })
            }, 6000)
        }
        hover() {
            $('.swiper-contione').hover(() => {
                this.timer = clearInterval(this.timer)
            }, () => {
                this.setTime();
            });
        }
        pointClick() {
            let that = this;
            $('.swiper-point-item .point').click(function() {
                that.num = $(this).index();
                let cssTrx = -that.num * that.w;
                $(this).addClass('active').siblings().removeClass('active');
                $('.swiper').css({
                    transform: `translateX(${cssTrx}px)`
                })
            })
        }
        lrClick() {
            $('.swiper-left img').click(() => {
                this.num--;
                if (this.num < 0) {
                    this.num = this.len;
                };
                console.log(this.num)
                let cssTrx = -this.num * this.w;
                $('.swiper-point-item .point').eq(this.num).addClass('active').siblings().removeClass('active');
                $('.swiper').css({
                    transform: `translateX(${cssTrx}px)`
                })
            });

            $('.swiper-right img').click(() => {
                this.num++;
                if (this.num > this.len) {
                    this.num = 0;
                }
                let cssTrx = -this.num * this.w;
                $('.swiper-point-item .point').eq(this.num).addClass('active').siblings().removeClass('active');
                $('.swiper').css({
                    transform: `translateX(${cssTrx}px)`
                })
            })
        }
    }
    let sw = new Swiper();
    sw.init();
}