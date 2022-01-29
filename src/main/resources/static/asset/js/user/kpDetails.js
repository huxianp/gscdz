window.onload = function() {
//select
    let chapter='${chapter.title}';
    console.log(chapter);

    let subtitles = document.getElementsByClassName("subtitle");
    let chapterId=document.getElementById("chapterId").innerText;
    let kpId=document.getElementsByClassName("kpId");
    subtitles[0].onclick = function() {
        window.location.href = "/chapter/"+chapterId;
    }
    for (let i = 1; i < subtitles.length; ++i) {
        subtitles[i].onclick = function() {
            window.location.href = "/chapter/knowledgePoint/"+chapterId+"/"+kpId[i].innerHTML;
        }
    }
}