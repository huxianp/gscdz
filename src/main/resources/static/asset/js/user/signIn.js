/**
 * JavaScript File of SignIn.html
 */
$(document).ready(function () {
    let isLegName=false;//密码是否通过
    let isLegPsw=false;
    let isLegCheckPsw=false;

    //input失去焦点判断是否存在
    $("#username").blur(function () {
        $(".checkIcon").eq(0).css("display","none")
          let username=$(this).val();
          if(username!=""){
            $.ajax({
                url:"/user/checkUsername",
                type:"Post",
                dataType:"json",
                contentType:"application/json;charset=UTF-8",
                data:JSON.stringify({"username":username}),
                success:function (values) {
                    console.log(values.isExist);
                    if(values.isExist){
                        swal("用户名已存在", "", "error");
                        isLegName=false;
                    }else{
                        isLegName=true;
                        $(".checkIcon").eq(0).css("display","initial")
                    }
                },
                error:function (data,status,e) {
                    console.log(data);
                    console.log(status);
                    console.log(e);
                }
            })
        }
    })
    //密码难度判断
    $("#password").blur(function () {
        $(".checkIcon").eq(1).css("display","none")
        let password=$(this).val();
        let chars='abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        let symbols='~!@#$%^&*()_=-/,.?<>;:[]{}|';
        let nums="0123456789";
        let hasChar=false;
        let hasSym=false;
        let hasNum=false;
        if(password.length<=8){
            swal("Opps","密码长度过短","error");
            return;
        }
        else{
            for(i=0;i<password.length&&!(hasSym&&hasChar&&hasNum);++i){
                if(chars.indexOf(password[i])!=-1){
                    hasChar=true;
                }else if(symbols.indexOf(password[i])!=-1){
                    hasSym=true;
                }else if(nums.indexOf(password[i])!=-1){
                    hasNum=true;
                }
            }
            if(!hasChar){
                swal("Oops","密码要包含字母","error");
            }else if(!hasNum){
                swal("Oops","密码要包含数字","error");
            }else if (!hasSym){
                swal("Oops","密码要包含特殊符号","error");
            }else{
                //密码通过
                $(".checkIcon").eq(1).css("display","initial")
                isLegPsw=true;
            }
        }
    })

    $("#checkPsw").on("input",function () {
        if(!isLegPsw){
            swal("Oops","请检查你的密码","error");
        }
    })
    //验证前后密码的一致
    $("#checkPsw").blur(function () {
        $(".checkIcon").eq(2).css("display","none")
        let checkPsw=$(this).val();
        let password=$("#password").val();
        if(checkPsw==password&&password!=""){
            isLegCheckPsw=true;
            $(".checkIcon").eq(2).css("display","initial")
        }else{
            swal("Oops","两次密码不一致","error");
        }
    })
    //注册逻辑的实现
    $("#loginBtn").click(function () {
        if(isLegName&&isLegPsw&&isLegCheckPsw){
            $.ajax({
                url:"/user/register",
                type: "post",
                dataType:"json",
                contentType:"application/json;charset=UTF-8",
                data:JSON.stringify({"username":$("#username").val(),"password":$("#password").val()}),
                success:function (values) {
                    console.log(values.msg);
                        if (values.msg){
                            swal("注册成功","","success")
                            window.setTimeout(3000,function () {
                                window.location.href="login";
                            })
                        }else{
                            swal("注册失败","","error")
                        }
                },
                error:function (data,status,e) {
                    console.log(data);
                    console.log(status);
                    console.log(e);
                }
            })
        }else{
            swal("请检查用户名或密码","","error");
        }
    })


})