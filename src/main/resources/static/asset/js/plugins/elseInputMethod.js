//固定的渲染容器
function generate(elementId) {
    let isFirst=true;
    $("#"+elementId).click(function(){
        if(isFirst){
            isFirst=!isFirst;
            layer.tips('如果你觉得这种输入方式不是很方便的话，可以尝试一下<a href="javascript:void(0)" id="elseMethods">其他方式</a>', $(this),{tips:[1],time:10000});
            $("#elseMethods").click(function (emelemtId) {
                layer.open({
                    type: 2,
                    area: ['1500px', '800px'],
                    fixed: false, //不固定
                    maxmin: true,
                    content: '/getPage',
                    cancel:function () {
                        let newText=sessionStorage.getItem("newText");
                        if(newText!=null&&newText!=''){
                            let stmt=document.querySelector("#"+elementId+" script").innerText;
                            console.log(stmt);
                            MathJax.Hub.Queue(function(i){
                                stmt+=newText;
                                let math = MathJax.Hub.getAllJax(elementId)[0];
                                MathJax.Hub.Queue(function () {
                                    MathJax.Hub.Queue(["Text", math,stmt]);
                                })
                            });
                        }
                    }
                });
            })
        }
    })
}


