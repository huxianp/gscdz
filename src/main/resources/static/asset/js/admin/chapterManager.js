$(document).ready(function () {
    $(function(){
        $(".acco-body").eq(0).show();
        $(".acco-title").eq(0).addClass("current-part");
        $("#passage-material").height($(".box").height());
        $(".acco-title").click(function(){
            $(this).toggleClass("current-part").siblings('.acco-title').removeClass("current-part");
            $(this).next(".acco-body").slideToggle(500,function f() {
                $("#passage-material").animate({height:$(".box").height()},200);
            }).siblings(".acco-body").slideUp(500);
        });
    });
//请求materials
    let kpId;
    $(".values").click(function () {
        $("#passage-body").html("");
        kpId=$(this).siblings()[0].innerText;
        $.ajax({
            method:"get",
            url:"/chapter/knowledgePoints/getMaterials/"+kpId,
            dataType:"json",
            contentType: "application/json;charset=utf-8",
            success:function (data) {
                if(data.code){
                    if(data.materials==null){
                        swal("materials null","you can update the materials into white board","warning")
                    }else{
                        $("#passage-body").html(data.materials);
                    }
                }else{
                    swal ( "Oops" ,  "请求数据错误，稍后重试" ,  "error" )
                }
               // $("#passage-body").html(obj.resText);
            },
            error:function (data,status,e) {
                swal ( "Oops" ,  "请求数据错误，稍后重试" ,  "error" )
                console.log(data);
                console.log(status);
                console.log(e);
            }
        })

    });

    $(".acco-body-part").hover(function () {
        $(this).children(".delIcon").css("display","initial");
    },function () {
        $(this).children(".delIcon").css("display","none");
    });

    $(".acco-part").hover(function () {
        $(this).find(".addPart").css("display","initial");
    },function () {
        $(this).find(".addPart").css("display","none");
    });

    $(".addPart").click(function () {
        let title=$(this).parent().parent().siblings("h3").text();
        $(this).parent().before("<div class='acco-body-part'><form method='post' action='/chapter/knowledgePoints/add'><input type='text'name='newSubtitle'><input type='text' name='title' value='"+title+"' hidden><button type='submit'>ADD</button></form></div>");
    })

    $(".delIcon").click(function () {
       let subTitle=$(this).siblings("a").text();
       if(subTitle!=null||subTitle!=""){
           $.ajax({
               url:"/chapter/knowledgePoints/delete",
               method:"Post",
               dataType:"json",
               data:JSON.stringify(
                   {subTitle:subTitle}
               ),
               contentType:"application/json;charset=UTF-8",
               success:function (results) {
                   if(results.code){
                       swal("delete okay!","","success")
                       setTimeout(function () {
                           window.location.reload();
                       },2000);

                   }else{
                       swal("Oops!","Please try later","error");
                   }
               },
               error:function(error){
                   console.log(error);
                   swal("Oops!","Please try later","error");
               }
           })
       }
    })
    $("#update-Material-btn").click(function () {
        console.log(kpId);
        console.log($("#passage-body").text())
        $.ajax({
            method: "post",
            url: "/chapter/knowledgePoints/updateMaterials",
            data:JSON.stringify(
                {"kpId":kpId,
                    "Materials":$("#passage-body").text()
                }
            ),
            dataType: "json",
            contentType:"application/json;charset=utf-8",
            success:function (data){
                if(data.code){
                    swal(data.msg,"","success")
                }else{
                    swal(data.msg,"","error");
                }
            },
            error:function (error) {
                console.log(error);
                swal("Oops!","","error");
            }

        })

    })
    let hasClick=false
    $("#addChapter").click(function (){
        console.log("zzzzz")
        if(!hasClick){
            $(this).append("<input type='text' class='chapter-name-input'></input><br/><input type='button' id='chapter-submit-btn' value='submit' style='position: absolute;\n" +
                "    top: 6px;\n" +
                "    right: 4px;'></input>");
            hasClick=!hasClick;

            $("#chapter-submit-btn").click(function () {
                let chapterName=$(".chapter-name-input").val();
                if(chapterName!=null||chapterName!=''){
                    $.ajax({
                        url:"/chapter/add",
                        method:"post",
                        data:JSON.stringify({
                            "chapterName": chapterName
                        }),
                        dataType:"json",
                        contentType:"application/json;charset=UTF-8",
                        success:function (result) {
                            console.log(result)
                            if(result.code){
                                swal("add okay!","","success")
                                setTimeout(function () {
                                    window.location.reload();
                                },2000);

                            }else{
                                swal("Oops!","Please try later","error");
                            }

                        },
                        error:function (error) {
                            console.log(error);
                            swal("Oops!","","error");
                        }
                    })
                }
            })
        }


    })
});
