$(document).ready(function () {
    initialMathJax();
    let tabCon=document.getElementById("tabCon");
    let pagCon=document.getElementById("pagCon");
    let MathDiv=document.getElementById("MathDiv");
    let kpoints=document.getElementById("kpoints");
    let nobr;
    let stmt;
    //initialize the text of mathDiv
    MathJax.Hub.Queue(function(){
        stmt=document.querySelector("#MathDiv script").innerText;
    });
    let btnQuiz=document.getElementById("btn-quiz");
    btnQuiz.onclick=function(){
        tabCon.style.marginLeft="10px";
        tabCon.style.width="690px";
        window.setTimeout(function(){pagCon.style.display="block"},2000);
    }
    let btnBack=document.getElementById("btn-back");
    btnBack.onclick=function(){
        pagCon.style.display="none";
        tabCon.style.marginLeft="25%";
        tabCon.style.width="800px";
    }


    let chapterId;
    let kpId;
    //动态添加知识点
    $(".chapter-name").click(function () {
        $("#dropdownMenu1").html($(this).html());
        //parseInt($(this).html().split("|")[0])
        chapterId=parseInt($(this).html().split("|")[0])
        $.ajax({
            url:"/chapter/getKnowledgePoints",
            type:"post",
            dataType:"json",
            data:JSON.stringify({"chapterId":chapterId}),
            contentType:"application/json;charset=UTF-8",
            success:function (values) {
                //$("#knowledgePoints-Select").append() <li role="presentation" th:each="chapter:${session.chapters}">
                //                             <a  class="chapter-name" role="menuitem" tabindex="-1"  th:text="${chapter.id}+'|'+${chapter.title}">数据挖掘</a>
                //                         </li>
                $("#kpsul").empty();//清空子节点
                $("#kpsul").append("<li role=\"presentation\" class=\"disabled\">\n" +
                    "                            <a role=\"menuitem\" tabindex=\"-1\" href=\"#\">以下为相关章节</a>\n" +
                    "                        </li>\n" +
                    "                        <li role=\"presentation\" class=\"divider\"></li>")
            for(let i=0;i<values.kps.length;++i){
                    $("#kpsul").append("<li role='presentation'><a class='kp-name' role='menuitem' tabIndex='-1'>"+values.kps[i].id+'|'+values.kps[i].title+"</a></li>")
                }
            },
            error:function (data,status,e) {
                console.log(data);
                console.log(status);
                console.log(e);
            }
        })
    })
    //选择知识点
    $("#kpsul").on('click','.kp-name',function () {
        kpId=parseInt($(this).html().split("|")[0])
        $("#dropdownMenu2").html($(this).html());
    })

    let btnSub=document.getElementById("btn-submit");
    btnSub.onclick=function(){
        let stmt1=document.querySelector("#MathDiv script").innerText.slice(1);
        if(stmt1!=""&&chapterId!=null&&kpId!=null) {
            console.log(stmt1)
            $.ajax({
                url: "/quiz/addUnsolvedPro",
                type:"post",
                dataType:"json",
                data:JSON.stringify({"chapterId":chapterId,"kpId":kpId,"content":stmt1}),
                contentType:"application/json;charset=UTF-8",
                success:function (values) {
                    if (values.msg){
                        swal("添加成功","","success");
                        setTimeout(function(){ window.location.reload(); }, 3000);
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
        toolBtn[i].onclick=function(i){
            MathJax.Hub.Queue(function(i){
                stmt+=textInBtn;
                let math = MathJax.Hub.getAllJax("MathDiv")[0];
                MathJax.Hub.Queue(function () {
                    MathJax.Hub.Queue(["Text", math,stmt]);
                })

            });

        }
    }
    //字符输入
    function render(){
        nobr=document.querySelector("#MathDiv nobr");
        stmt=nobr.innerText;
        console.log(stmt)
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
    let cp=document.getElementById("currentPage")
    let currentPage=parseInt(cp.innerText);
    let mP=document.getElementById("maxPage");
    let maxPage=parseInt(mP.innerText);
    let pre=currentPage-1;
    let next=currentPage+1;

    let leftIcon=document.getElementById("leftIcon");

    leftIcon.onclick=function(){

        if(currentPage>1){
            window.location.href ="/quiz/"+pre;
        }else{
            swal("当前为首页","","error");
        }

    }

    let rightIcon=document.getElementById("rightIcon");

    rightIcon.onclick=function(){

        if(currentPage<maxPage){
            window.location.href ="/quiz/"+next;
        }else{
            swal("当前为末尾页","","error");
        }

    }
})