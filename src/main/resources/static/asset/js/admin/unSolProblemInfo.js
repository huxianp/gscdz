$(function () {
    layui.config({
        version: '1545041465480' //为了更新 js 缓存，可忽略
    });
    //注意：这里是数据表格的加载数据，必须写
    layui.use(['table', 'layer', 'form', 'laypage', 'laydate','upload'], function () {
        var table = layui.table //表格
            ,layer = layui.layer //弹层
            ,form = layui.form //form表单
            ,laypage = layui.laypage //分页
            ,laydate = layui.laydate//日期
            ,upload=layui.upload;//文件上传
        //执行一个laydate实例
        laydate.render({
            elem: '#start' //指定元素
        });
 
        //执行一个laydate实例
        laydate.render({
            elem: '#end' //指定元素
        });
 
        //执行一个 table 实例
        table.render({
            elem: '#zq_table'
            ,id: 'tableReload'//重载数据表格
            ,url: '/quiz/getAllProblems' //数据接口
            ,toolbar: '#zq_toolbar' //开启头工具栏，此处default：显示默认图标，可以自定义模板，详见文档
            ,title: 'xx表'
            ,page: true //开启分页
            // ,totalRow: true //开启合计行
            ,cols: [[ //表头
                {type: 'checkbox', fixed: 'left'}
                ,{field: 'problemId', title: '问题ID', width:100, sort: true, fixed: 'left', totalRowText: '合计：'} //totalRow：true则代表这列数据要合计
                ,{field: 'subject', title: '题干',edit: 'text',}
                ,{field: 'relevantKps', title: '相关知识点',  sort: true, totalRow: true,edit: 'text'}
                ,{field: 'answerNumAndVisitNum', title: '回答次数/访问次数', sort: true,edit: 'text'}
                ,{field:'createTime',title: '创建时间', sort: true,edit: 'text'}
                ,{fixed: 'right', width: 165, align:'center', toolbar: '#zq_bar'}
            ]]
                ,done:function (res) {
                $.getScript("//cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-MML-AM_CHTML",function(){
                    MathJax.Hub.Config({tex2jax:{inlineMath:[['$','$'],['\\(','\\)']]}});
                    let cell=document.getElementsByClassName("layui-table-cell");
                    MathJax.Hub.Queue(["Typeset",MathJax.Hub,cell]);
                });


            }
        });


        //监听行工具事件
        table.on('tool(zq_table)', function(obj){ //注：tool 是工具条事件名，zq_table 是 table 原始容器的属性 lay-filter="对应的值"
            var data = obj.data //获得当前行数据
                ,layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            switch(layEvent){
                case 'detail':
                    layer.open({
                        type: 1,
                        title:'题目详细信息',
                        area: ['700px', '450px'],
                        fixed: false, //不固定
                        maxmin: true,
                        content:'<form class="layui-form" lay-filter="test1" style="margin: 15px" >' +
                            '  <div class="layui-form-item">\n' +
                            '    <label class="layui-form-label">问题ID</label>\n' +
                            '    <div class="layui-input-inline">\n' +
                            '      <input type="text" id="problemId" disabled lay-verify="required" disabled autocomplete="off" class="layui-input layui-disabled">\n' +
                            '    </div>\n' +
                            '  </div>'+
                            '  <div class="layui-form-item">\n' +
                            '    <label class="layui-form-label">题干</label>\n' +
                            '    <div class="layui-input-block">\n' +
                            '      <input type="text" id="subject" autocomplete="off" disabled class="layui-input layui-disabled math">\n' +
                            '    </div>\n' +
                            '  </div>\n' +
                            '  <div class="layui-form-item">\n' +
                            '    <label class="layui-form-label">分析</label>\n' +
                            '    <div class="layui-input-block">\n' +
                            '      <textarea disabled  id="analysis" class="layui-textarea layui-disabled math"></textarea>\n' +
                            '    </div>\n' +
                            '  </div>\n' +
                            '  <div class="layui-form-item">\n' +
                            '    <label class="layui-form-label">相关知识点</label>\n' +
                            '    <div class="layui-input-block">\n' +
                            '      <input type="text" id="kps" autocomplete="off" disabled  class="layui-input layui-disabled">\n' +
                            '    </div>\n' +
                            '  </div>\n' +
                            '  <div class="layui-form-item">\n' +
                            '    <label class="layui-form-label">回答人数</label>\n' +
                            '    <div class="layui-input-inline">\n' +
                            '      <input type="text" id="answerPersonNum" disabled lay-verify="required" autocomplete="off" class="layui-input layui-disabled">\n' +
                            '    </div>\n' +
                            '  </div>'+
                            '  <div class="layui-form-item layui-form-text">\n' +
                            '    <label class="layui-form-label">答案</label>\n' +
                            '    <div class="layui-input-block">\n' +
                            '      <textarea disabled  id="answer" class="layui-textarea layui-disabled math"></textarea>\n' +
                            '    </div>\n' +
                            '  </div>\n' +


                            '</form>'
                    });
                    form.render(null,'test1');
                    $.ajax({
                        url:'/quiz/getSolProblemById/'+data.problemId,
                        dataType:'json',
                        contentType: "application/json;charset=UTF-8",
                        method: 'get',
                        success:function (res) {

                            let  answerContext='';
                            res.data.answerList.forEach(answer=>{
                                if(answer!=null&&answer!=''&&answer!=undefined&&answer.content!=undefined){
                                    console.log(answer.content);
                                    answerContext+=answer.content
                                }
                            })

                            $("#problemId").val(res.data.problemId);
                            $("#subject").val(res.data.subject);
                            $("#analysis").val(res.data.analysis);
                            $("#kps").val(res.data.relevantKps);
                            $("#answerPersonNum").val(res.data.answerNumAndVisitNum);
                            $("#answer").val(answerContext);
                            $.getScript("//cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-MML-AM_CHTML",function(){
                                MathJax.Hub.Config({tex2jax:{inlineMath:[['$','$'],['\\(','\\)']]}});
                                let math=document.getElementsByClassName("math");
                                MathJax.Hub.Queue(["Typeset",MathJax.Hub,math]);
                            });

                        }
                    })
                    break;
                case 'edit':
                    layer.open({
                        type: 1,
                        title:'题目详细信息',
                        area: ['900px', '600px'],
                        fixed: false, //不固定
                        maxmin: true,
                        content: ' <div class="layui-row">\n' +
                            '    <div class="layui-col-xs12">\n' +
                            '      <div class="grid-demo" id="formTitle">题目类型转换</div>\n' +
                            '    </div>\n' +
                            '  </div>\n'+
                            ' <div class="layui-row">\n' +
                            '    <div class="layui-col-xs2">\n' +
                            '      <div class="grid-demo grid-demo-bg1 titleContainer" >题干</div>\n' +
                            '    </div>\n' +
                            '    <div class="layui-col-xs10">\n' +
                            '      <div class="grid-demo contentContaier" id="subjectContainer"></div>\n' +
                            '    </div>\n' +
                            '  </div>\n'+
                            ' <div class="layui-row">\n' +
                            '    <div class="layui-col-xs2">\n' +
                            '      <div class="grid-demo grid-demo-bg1 titleContainer" >分析</div>\n' +
                            '    </div>\n' +
                            '    <div class="layui-col-xs10">\n' +
                            '      <div class="grid-demo contentContaier" id="analysisContainer"></div>\n' +
                            '    </div>\n' +
                            '  </div>\n'+
                            ' <div class="layui-row">\n' +
                            '    <div class="layui-col-xs2">\n' +
                            '      <div class="grid-demo grid-demo-bg1 titleContainer" >答案</div>\n' +
                            '    </div>\n' +
                            '    <div class="layui-col-xs10">\n' +
                            '      <div class="grid-demo contentContaier" id="answerContainer"></div>\n' +
                            '    </div>\n' +
                            '  </div>\n'+
                            '<div class="layui-row">\n' +
                                    '<div class="layui-col-xs8">\n' +
                                    '      <div class="grid-demo" id="formTile"><button class="">  ' +
                                                '<button type="button" class="layui-btn layui-btn-fluid" id="submit-btn"><i class="layui-icon">&#xe67c;</i>添加到系统题库</button>' +
                                            '</div>\n' +
                                    '</div>\n' +
                            '</div>\n'
                    })
                    $.ajax({
                        url:'/quiz/getSolProblemById/'+data.problemId,
                        dataType:'json',
                        contentType: "application/json;charset=UTF-8",
                        method: 'get',
                        success:function (res) {
                            sessionStorage.setItem("problemId",res.data.problemId);
                            let  answerContext='';
                            res.data.answerList.forEach(answer=>{
                                if(answer!=null&&answer!=''&&answer!=undefined&&answer.content!=undefined){
                                    answerContext+=answer.content
                                }
                            })
                            $("#subjectContainer").html(res.data.subject);
                            $("#analysisContainer").html(res.data.analysis);
                            $("#answerContainer").html(answerContext)
                            $.getScript("//cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-MML-AM_CHTML",function(){
                                MathJax.Hub.Config({tex2jax:{inlineMath:[['$','$'],['\\(','\\)']]}});
                                let math=document.getElementsByClassName("math");
                                MathJax.Hub.Queue(["Typeset",MathJax.Hub,math]);
                            });
                            $("#submit-btn").unbind('click');
                            $("#submit-btn").click(function(){
                                let problemId=sessionStorage.problemId;
                                $.ajax({
                                    url:"/quiz/addToQuestionBank",
                                    type:"post",
                                    dataType: "json",
                                    data:JSON.stringify({
                                        problemId:problemId
                                    }),
                                    contentType: "application/json;charset=UTF-8",
                                    success:function (res) {
                                        console.log(res)
                                    },
                                    error:function () {
                                        console.log("error");
                                    }
                                })
                            })
                        }
                    })
            }
        });
 
        //监听单元格编辑   zq_table 对应 <table> 中的 lay-filter="zq_table"       做可编辑表格使用
        table.on('edit(zq_table)', function(obj){
            var value = obj.value //得到修改后的值
                ,data = obj.data //得到所在行所有键值
                ,field = obj.field; //得到字段
            layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
        });

        /*
        //监听显示操作
        form.on('switch(isShow)', function(obj) {
            var t = this;
            layer.tips(t.value + ' ' + t.name + '：' + obj.elem.checked, obj.othis);
        });*/

        //监听提交 lay-filter="zq_submit"
        form.on('submit(zq_submit)', function(data){
            // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
            // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
            console.log(data.field) //当前from表单所提交的所有字段， 名值对形式：{name: value}
            layer.msg(JSON.stringify(data.field));//表格数据序列化
            var formData = data.field;
            var id = formData.id,
                name = formData.name,
                url=formData.url,
                icon=formData.icon,
                parent_id=formData.parent_id;
            $.ajax({
                type: "post",  //数据提交方式（post/get）
                url: "/menu/save",  //提交到的url
                data: {"id":id,"name":name,"url":url,"icon":icon,"parent_id":parent_id},//提交的数据
                dataType: "json",//返回的数据类型格式
                success: function(msg){
                    if (msg.success){  //成功
                        layer.msg(msg.msg, { icon: 1, time: 1500 });
                        table.reload('tableReload');//数据表格重载
                        layer.close(index);//关闭弹出层
                    }else {  //失败
                        layer.alert(msg.msg, { icon: 2},function () {
                            // $(".layui-laypage-btn").click();//执行分页刷新当前页
                            layer.close(index);
                            // window.location.reload();
                        });
                    }
                }
            });
            return false;//false：阻止表单跳转  true：表单跳转
        });
 
        //监听提交 lay-filter="search"
        form.on('submit(search)', function(data){

            var msg = data.field.name;
            layer.msg("msg:"+msg);//表格数据序列化
            //数据表格重载
            table.reload('tableReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {//这里传参向后台
                    msg: msg,
                    userId:sessionStorage.getItem("userId")
                }
                , url: 'http://localhost:8080/pictureSharing/searchAlbumByMsg'//后台做模糊搜索接口路径
                , method: 'post'
            });
            return false;//false：阻止表单跳转  true：表单跳转
        });
    });
});
 
var index;//layer.open 打开窗口后的索引，通过layer.close(index)的方法可关闭
//表单弹出层
function zq_form(title,url,w,h){
    if (title == null || title == '') { title=false; };
    if (url == null || url == '') {  };// url="404.html";
    if (w == null || w == '') {  w=($(window).width()*0.9);  };
    if (h == null || h == '') {  h=($(window).height() - 50);  };
    index = layer.open({  //layer提供了5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
        type:1,
        title:title,
        area: ['25%','55%'],//类型：String/Array，默认：'auto'  只有在宽高都定义的时候才不会自适应
        // area: [w+'px', h +'px'],
        fix: false, //不固定
        maxmin: true,//开启最大化最小化按钮
        shadeClose: true,//点击阴影处可关闭
        shade:0.4,//背景灰度
        skin: 'layui-layer-rim', //加上边框
        content:$("#zq_formpopbox").html()
    });
}