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
            ,url: 'user/getAllUserInfo' //数据接口
            ,toolbar: '#zq_toolbar' //开启头工具栏，此处default：显示默认图标，可以自定义模板，详见文档
            ,title: 'xx表'
            ,page: true //开启分页
            // ,totalRow: true //开启合计行
            ,cols: [[ //表头
                {type: 'checkbox', fixed: 'left'}
                ,{field: 'userId', title: '用户ID', width:80, sort: true, fixed: 'left', totalRowText: '合计：'} //totalRow：true则代表这列数据要合计
                ,{field: 'username', title: '用户名',edit: 'text'}
                ,{field: 'password', title: '密码',  sort: true, totalRow: true,edit: 'text'}
                ,{field: 'answerPros', title: '获赞数', sort: true,edit: 'text'}
                ,{field: 'answerCons', title: '被踩数', sort: true, totalRow: true,edit: 'text'}
                ,{fixed: 'right', width: 165, align:'center', toolbar: '#zq_bar'}
            ]]
        });


 
        //监听头工具栏事件
        table.on('toolbar(zq_table)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id)
                ,data = checkStatus.data; //获取选中的数据
            //json字符串转换成Json数据 eval("("+jsonStr+")")  /JSON.parse(jsonStr)
            data = eval("("+JSON.stringify(data)+")");
            switch(obj.event){
                case 'delAll':
                    if(data.length === 0){
                        layer.msg('请至少选择1行', { icon: 2, time: 1500 });
                    }else {
                        layer.alert('您确认要删除'+data.length+'条数据吗？', {
                            skin: 'layui-layer-molv' //样式类名layui-layer-lan或layui-layer-molv  自定义样式
                            ,closeBtn: 1    // 是否显示关闭按钮
                            ,anim: 1 //动画类型
                            ,btn: ['确定','取消'] //按钮
                            ,icon: 2    // icon
                            ,yes:function(){
                                // layer.msg('确定', { icon: 1, time: 1500 });
                                for (var i=0;i<data.length;i++){
                                    console.debug("id:======"+data[i].id)
                                    //发送请求到后台
                                    $.post("menu/delete", { id: data[i].id }, function (result) {
                                        if (result.code == "1") {//删除成功，刷新当前页表格
                                            // obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                                            layer.msg(result.msg, { icon: 1, time: 1500 });
                                            // layer.close(index);
                                            $(".layui-laypage-btn").click();//点击分页刷新当前页
                                        }else  if(result.code == "-1"){  //删除失败
                                            layer.alert(result.msg, { icon: 2},function () {
                                                $(".layui-laypage-btn").click();
                                                window.location.reload();
                                            });
                                        }
                                    });
                                }
                                /*   //捉到所有被选中的，发异步进行删除
                                   layer.msg('删除成功', {icon: 1});
                                   $(".layui-form-checked").not('.header').parents('tr').remove();*/
                            }
                            ,btn2:function(){
                                layer.msg('好的,暂时不给您删除。',{ icon: 1, time: 1500 });
                            }
                        });
                    }
                    break;
                case 'add':
                    window.open("/download/student-samples.xlsx");
                    layer.open({
                        title:"批量添加用户",
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['420px', '240px'], //宽高
                        content:
                            '<div class="layui-upload-drag" id="uploadFile">\n' +
                            '  <i class="layui-icon"></i>\n' +
                            '  <p>点击上传，或将文件拖拽到此处</p>\n' +
                            '</div>'+
                            '  <div class="" id="tipWords"> 请按student-samples.xlsx的格式上传文件</div>'
                    });
                    //拖拽上传
                    upload.render({
                        elem: '#uploadFile'
                        ,url: '/user/insertBatch',
                        accept: 'file',
                        acceptMime: 'application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
                        exts: 'xlsx|xls'//只允许上传excel文件
                        ,done: function(res){
                            if(res.code){
                                layer.msg('上传成功');
                                setTimeout(function () {
                                    window.location.reload();
                                },1200)
                            }else{
                                layer.alert('出错了，请检查文件的格式', {
                                    icon: 5,
                                    title: "错误"
                                });
                            }
                        },error:function (error) {
                            layer.alert('出错了，请稍后重试', {
                                icon: 5,
                                title: "错误"
                            });
                        }
                    });
                    break;
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
                        area: ['700px', '450px'],
                        fixed: false, //不固定
                        maxmin: true,
                        content: '<div style="height: 400px;height: 400px" id="main"></div>'
                    });
                    renderChart(data);
                    break;
                case 'del':
                    layer.confirm('您确定删除id：'+data.userId+'的数据吗？', {btn: ['确认','取消'],btn1:function (index){
                                $.ajax({
                                    url: "/user/deleteCustomUserById/"+data.userId,
                                    type: "get",
                                    dataType: "json",
                                    contentType: "application/json;charset=UTF-8",
                                    //向后端传输的章节数据
                                    success: function (value) {
                                        console.log(value);
                                        if (value.isSuccess) {//删除成功，刷新当前页表格
                                            layer.msg(value.msg, { icon: 1, time: 1500 }, function () {
                                                obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                                                layer.close(index);
                                            });
                                        }else{
                                            layer.msg(value.msg, { icon: 2, time: 1500 }, function () {
                                                layer.close(index);
                                            });
                                        }
                                    },
                                    error: function () {
                                        console.log("error");
                                        layer.msg("删除失败，稍后重试", { icon: 2, time: 1500 }, function () {
                                            layer.close(index);
                                        });
                                    }
                                })
                        },
                        btn2:function () {
                        }
                    });
                    break;
                case 'edit':
                    console.log(data);
                    $.ajax({
                        url:"/user/updateUserInfo",
                        type: "POST",
                        dataType: "json",
                        contentType: "application/json;charset=UTF-8",
                        //向后端传输的章节数据
                        data: JSON.stringify({
                            userId:data.userId,
                            password:"123456"
                        }),
                        success:function (value) {
                            if (value.isSuccess) {
                                layer.msg(value.msg, { icon: 1, time: 1500 })
                            }else{
                                layer.msg(value.msg, { icon: 2, time: 1500 })
                            }
                        },
                        error:function () {
                            layer.msg("请稍后重试", { icon: 2, time: 1500 })
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