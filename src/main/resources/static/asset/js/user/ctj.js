$(document).ready(function () {
    initialMathJax();
    let canvas=document.getElementsByClassName("canvas");
    let ctx;
    function draw(){
        ctx.strokeStyle="gray";
        ctx.moveTo(0,20);
        ctx.lineTo(15,0);
        ctx.lineTo(30,20);
        ctx.moveTo(0,30);
        ctx.lineTo(15,10);
        ctx.lineTo(30,30);
        ctx.stroke();
    }
    for(let i=0;i<canvas.length;++i){
        ctx=canvas[i].getContext("2d");
        draw();
    }
    let solAndAnas;

    $(".problemDetails").click(function () {
        $(this).parent().parent().next().removeAttr("hidden");
    })
    $("canvas").click(function () {
        $(this).parent().prop("hidden", "hidden");
    })
    $("#deleteIcon").click(function () {
        let id=$(this).parent().siblings(".id").html();
        console.log(id);
        window.location.href="/solvedProblem/delete/"+id;
    })
})