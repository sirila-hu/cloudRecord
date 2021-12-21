<!--关键字标记显示-->
var filed = /*[[${filed}]]*/"关键字";
$(document).ready(function(){
    $(".page-content").html(function(i,origText){
        var text = origText;
        var reg = new RegExp(filed,"g");//g,表示全部替换。
        text = text.replace(reg, "<span class='bg-secondary text-white ml-1 mr-1'>"+filed+"</span>");
        return text;
    });
});
<!--目录位置移动-->
$(".index-item").click(function(){
    var position = $(this).attr("position");
    var offset = $(position).offset();
    offset.top = offset.top - 20;
    $("html,body").animate({scrollTop:offset.top}, 100);
});