window.onload=function(){
    initialMathJax();
    let middle=document.getElementById("middle");
    let proInfo=document.getElementById("proInfo");

    let container=document.getElementById("container");
    let elseAnswer=document.getElementById("elseAnswer");
    let resize=document.getElementById("resize");
    let answerPos=document.getElementById("answerPos");


    let height=proInfo.offsetHeight+elseAnswer.clientHeight+500;
    middle.style.height=height+"px";
    console.log(proInfo.offsetHeight+"1||"+elseAnswer.clientHeight+"||"+height);
    let resizeIcon=document.getElementById("resize-canvas");
    let ctx=resizeIcon.getContext("2d");
    ctx.strokeStyle="black";
    ctx.moveTo(3,0);
    ctx.lineTo(3,50);
    ctx.moveTo(8,0);
    ctx.lineTo(8,50);

    ctx.stroke();

    resize.onmousedown = function (e) {
        var startX = e.clientX;
        resize.left = resize.offsetLeft;
        document.onmousemove = function (e) {
            var endX = e.clientX;

            var moveLen = resize.left + (endX - startX);
            var maxT = container.clientWidth - resize.offsetWidth;
            if (moveLen < 360) moveLen = 360;
            if (moveLen > maxT - 150) moveLen = maxT - 150;

            resize.style.left = moveLen;
            elseAnswer.style.width = moveLen + "px";
            answerPos.style.width = (container.clientWidth - moveLen - 14) + "px";
        }
        document.onmouseup = function (evt) {
            document.onmousemove = null;
            document.onmouseup = null;
            resize.releaseCapture && resize.releaseCapture();
        }
        resize.setCapture && resize.setCapture();
        return false;
    }

    //answerPos部分
    let MathDiv=document.getElementById("MathDiv");
    let analysis=document.getElementById("analysis");
    let nobr;
    let stmt;
    MathJax.Hub.Queue(function(){
        nobr=document.querySelector("#MathDiv nobr");
        stmt=nobr.innerText;//MathDiv中的初始内容
    });

    $(".pros-icon").click(function () {
        let answerId=$(this).parent().siblings(".answerId").html();
        $.ajax({
            url:"/quiz/prosAndCons",
            method:"post",
            dataType:"json",
            data:JSON.stringify({"answerId":answerId,"action":"pros"}),
            contentType:"application/json;charset=UTF-8",
            success:function (values) {
                if(values.msg){
                    window.location.reload();
                }
            },
            error:function (data,status,e) {
                console.log(data);
                console.log(status);
                console.log(e);
            }
        })
    })
    $(".cons-icon").click(function () {
        let answerId=$(this).parent().siblings(".answerId").html();
        $.ajax({
            url:"/quiz/prosAndCons",
            method:"post",
            dataType:"json",
            data:JSON.stringify({"answerId":answerId,"action":"cons"}),
            contentType:"application/json;charset=UTF-8",
            success:function (values) {
                if(values.msg){
                    window.location.reload();
                }
            },
            error:function (data,status,e) {
                console.log(data);
                console.log(status);
                console.log(e);
            }
        })
    })

    let btnSub=document.getElementById("btn-submit");
    btnSub.onclick=function(){
        let problemId=$("#problem-id").html()
        stmt=document.querySelector("#MathDiv script").innerText
        if(stmt!=""&&analysis.innerText!=""){
            $.ajax({
                url:"/quiz/addAnswer",
                method:"post",
                dataType:"json",
                data:JSON.stringify({"content":stmt,"analysis":analysis.innerText,"problemId":problemId}),
                contentType:"application/json;charset=UTF-8",
                success:function (values) {
                    if(values.msg){
                        window.location.reload();
                    }
                },
                error:function (data,status,e) {
                    console.log(data);
                    console.log(status);
                    console.log(e);
                }
            })
        }
        else{
            swal("请仔细填写","","warning");
        }
    }

    //特殊字符按钮
    let toolBtn=document.querySelectorAll(".toolBtn");
    for(let i=0;i<toolBtn.length-4;++i){
        let textInBtn=toolBtn[i].innerText.slice(1,toolBtn[i].innerText.length-1);//切掉$$
        toolBtn[i].onclick=function(){
            MathJax.Hub.Queue(function(){
                stmt+=textInBtn;
                let math = MathJax.Hub.getAllJax("MathDiv")[0];
                MathJax.Hub.Queue(["Text", math,stmt]);
            });

        }
    }
    //字符输入
    function render(){
        nobr=document.querySelector("#MathDiv nobr");
        stmt=nobr.innerText;
    }
    MathDiv.addEventListener('input', render, false);

    //分数的输入
    $("#frac").dialog({
        id : "frac1",
        title : "请输入分子和分母",
        type : 0,
        easyClose : true,
        form : [{
            description:"分子",
            type:"text",
            name:"fenzi",
            value:"",
        },
            {
                description:"分母",
                type:"text",
                name:"fenmu",
                value:"",
            }
        ],
        submit:function(data){
            MathJax.Hub.Queue(function(){
                stmt+="\(\\frac{"+data.fenzi+"}{"+data.fenmu+"}\)";
                let math = MathJax.Hub.getAllJax("MathDiv")[0];
                MathJax.Hub.Queue(["Text", math,stmt]);
            });
        }
    })
    //sum
    $("#sum").dialog({
        id : "sum1",
        title : "积分起点和终点",
        type : 0,
        easyClose : true,
        form : [{
            description:"起点",
            type:"text",
            name:"start",
            value:"",
        },
            {
                description:"终点",
                type:"text",
                name:"final",
                value:"",
            }
        ],
        submit:function(data){
            MathJax.Hub.Queue(function(){
                stmt+="\\sum_{"+data.start+"}^{"+data.final+"}\(\)";
                let math = MathJax.Hub.getAllJax("MathDiv")[0];
                MathJax.Hub.Queue(["Text", math,stmt]);
            });
        }
    })
//int
    $("#int").dialog({
        id : "int1",
        title : "积分起点和终点",
        type : 0,
        easyClose : true,
        form : [{
            description:"起点",
            type:"text",
            name:"start",
            value:"",
        },
            {
                description:"终点",
                type:"text",
                name:"final",
                value:"",
            }
        ],
        submit:function(data){
            MathJax.Hub.Queue(function(){
                stmt+="\\int_{"+data.start+"}^{"+data.final+"}\(\)dx";
                let math = MathJax.Hub.getAllJax("MathDiv")[0];
                MathJax.Hub.Queue(["Text", math,stmt]);
            });
        }
    })
    //sqrt
    $("#sqrt").dialog({
        id : "sqrt1",
        title : "开方",
        type : 0,
        easyClose : true,
        form : [{
            description:"次方",
            type:"text",
            name:"x",
            value:"",
        },
            {
                description:"开方内容",
                type:"text",
                name:"y",
                value:"",
            }
        ],
        submit:function(data){
            MathJax.Hub.Queue(function(){
                stmt+="\(\\sqrt["+data.x+"]{"+data.y+"}\)";
                let math = MathJax.Hub.getAllJax("MathDiv")[0];
                MathJax.Hub.Queue(["Text", math,stmt]);
            });
        }
    })
}