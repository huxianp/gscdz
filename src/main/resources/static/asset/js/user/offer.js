window.onload=function(){
    let subject=document.getElementById("subject");
    let solution=document.getElementById("solution");
    //initialize
    let stmt_subject;
    let stmt_solution;
    let nobr_subject;
    let nobr_solution;
    function renderSubject(){
        nobr_subject=document.querySelector("#subject nobr");
        stmt_subject=nobr_subject.innerText;

    }
    function renderSolution(){
        nobr_solution=document.querySelector("#solution nobr");
        stmt_solution=nobr_solution.innerText;

    }
    subject.addEventListener('input', renderSubject, false);
    solution.addEventListener('input', renderSolution, false);
    //end of initialize

    initialMathJax();
    $("#submit-btn").click(function () {
        let subjectText=document.querySelector("#subject script").innerText;
        let subjectText1=stmt_subject;

        let solutionText=document.querySelector("#solution script").innerText;
        let solutionText1=stmt_solution;

        let subject=subjectText1==undefined?subjectText:subjectText1
        let solution=solutionText1==undefined?solutionText:solutionText1

        let analysis=document.getElementById("analysis").innerText;
        let tip=document.getElementById("tips").innerText;
        if (subject==null||subject==''){
            swal("题干不能为空","","warning")
        }
        else if (solution==null||solution==''){
            swal("答案不能为空","","warning")
        }
        else if (analysis==null||analysis ==''){
            swal("分析不能为空","","warning")
        } else if(tip==null||tip==""){
            swal("提示不能为空","","warning")
        }
        else{
            $.ajax({
                url:"/solvedProblem/offer",
                method:'post',
                dataType:'json',
                contentType:'application/json;charset=UTF-8',
                data:JSON.stringify({
                    subject:subject,
                    solution:solution,
                    analysis:analysis,
                    tips:tip
                }),
                success:function (res) {
                    if(res.code){
                        swal(res.msg,"","success")
                    }else{
                        swal(res.msg,"","error")
                    }
                },
                error:function () {
                    console.log("error")
                    swal("Oops！","请稍后重试","error");
                }
            })
        }

    })
}
