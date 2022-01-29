window.onload = function() {
    initialMathJax();
    let startBtn = document.getElementById("startBtn");
    let tipsBtn = document.getElementById("tipsBtn");
    let answerPos = document.getElementById("answerPos");
    startBtn.onclick = function() {
        startBtn.style.display = "none";
        tipsBtn.style.display = "none";
        answerPos.style.display = "block";
    }
    let tipArray = new Array();
    let number = 0;
    function getTips() {
        let tipsDiv = document.getElementById("tips");
        let tips = tipsDiv.innerText;

        if (tips) {
            tipArray = tips.split("||");

        } else {
            return;
        }
    }

    function alertTip() {
        console.log(tipArray.length);
        if (number < tipArray.length) {
            swal(tipArray[number],"","");
            number++;
        }
        else {
            swal("已经全部提示完毕了","","");
        }
    }

    getTips();
    tipsBtn.onclick = alertTip;
    let btnSub = document.getElementById("btn-submit");
    let btnAdd = document.getElementById("btn-addToctj");
    let MathDiv = document.getElementById("MathDiv");
    let solution = document.getElementById("solution");
    let analysis = document.getElementById("analysis");
    btnSub.onclick = function() {
        if (MathDiv.innerText != null) {
            solution.removeAttribute("hidden");
            analysis.removeAttribute("hidden");
            btnAdd.style.display = "initial";
        } else {
            alert("请仔细作答哦");
        }

    }
    let problemId=document.getElementById("problemId");
    btnAdd.onclick = function() {
        console.log(problemId.innerText);
        $.ajax({
            url: "/solvedProblem/addToctj",
            type:"post",
            dataType:"json",
            data:JSON.stringify({"problemId":problemId.innerText}),
            contentType:"application/json;charset=UTF-8",
            success:function (values) {
                if (values.msg){
                    swal("添加成功","","success");
                }
                else{
                    swal("添加失败","","error");
                }
            },
            error:function (data,status,e) {
                console.log(data);
                console.log(status);
                console.log(e);
            }
        })
    }
    let stmt;
    MathJax.Hub.Queue(function(){
        stmt=document.querySelector("#MathDiv script").innerText;
    });
    //特殊字符按钮
    let toolBtn=document.querySelectorAll(".toolBtn");
    for(let i=0;i<toolBtn.length-4;++i){
        let textInBtn=toolBtn[i].innerText.slice(1,toolBtn[i].innerText.length-1);//切掉$$
        toolBtn[i].onclick=function(i){
            MathJax.Hub.Queue(function(i){
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