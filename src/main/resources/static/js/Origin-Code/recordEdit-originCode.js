//请求失败显示
$(document).ajaxError(function (){
    layer.msg("请求失败", {icon: 2});
});

//点击标题跳转到笔记本控制页
$("#list-title").click(function (){
    window.location.href = $(this).attr("jump");
});
//列表展开图标控制
$(".btn-section").click(function(){
    var isexpand = $(this).attr("aria-expanded");
    if(isexpand == "true")
    {
        $(this).find(".fa-caret-down").removeClass("fa-flip-vertical");
    }
    else
    {
        $(".fa-caret-down").removeClass("fa-flip-vertical");
        $(this).find(".fa-caret-down").addClass("fa-flip-vertical");
    }
});
//功能按钮显示控制
$(".section-item").hover(function(){
    $(this).children(".section-btns").animate({opacity: '1'});
}, function(){
    $(this).children(".section-btns").animate({opacity: '0'});
});
//页iframe显示
$(".pageItem").click(function(){
    var p_id = $(this).find(".p_id").text();
    $(".page-show").children().attr("src", "pageEdit?p_id="+p_id);
    $(".pageItem").removeClass("pageItem-active");
    $(this).addClass("pageItem-active");
});
//菜单隐藏控件(固定按钮)
$("#btn-list").click(function(){
    if($("#section-list").css("left") == "-240px")
    {
        $("#section-list").animate({left:'0px'});
    }
    else
    {
        $("#section-list").animate({left:'-240px'});
    }

});

//弹出层控件
//添加分区(固定按钮)
$(".btn-section-add").click(function(){
    var r_id = $("#r_id").text();
    layer.prompt({
        title:'添加分区',
        value:'请填写分区名',
        "success": function(){
            $(".layui-layer-input").click(function(){
                this.select();
            });
        }
    }, function(value, index, elem){
        $.post("recordEdit/addSection",{s_title:value,s_record:r_id}, function (result){
            if (result.status !== "0")
            {
                var message = "添加失败";
                if (result.message != null && result.message != "")
                {
                    message = result.message;
                }
                layer.msg(message, {icon: 2});
                return;
            }

            var s_id = result.s_id;
            //ajax请求成功后添加页至指定分区
            var section = $(".modal-zone").children(".section").clone(true);
            //替换文本信息
            section.find("span.section-title").text(value);
            section.find("span.s_id").text(s_id);
            //替换属性信息
            section.find("[id*='{s_id}']").attr("id", function(i, origValue){
                if($(this).attr("aria-labelledby") == "{s_id}")
                {
                    $(this).attr("aria-labelledby", "S"+s_id);
                }
                return origValue.replace("{s_id}", "S"+s_id);
            });
            section.find(".btn-section").attr("data-target", "#S"+s_id+"-collapse").attr("aria-controls", "S"+s_id+"-collapse");

            //关闭无查询结果显示
            if($("#no-result").hasClass("d-none") == false)
            {
                $("#no-result").removeClass("d-flex");
                $("#no-result").addClass("d-none");
            }
            $("#section-list").append(section);
            layer.msg('添加成功', {icon: 1});

        });

        layer.close(index);
    });
});
//删除确认弹出
$(".btn-close").click(function(){
    var s_id = $(this).siblings(".s_id").text();
    var sectionItem = $(this).parents("div.card");

    layer.confirm('确定删除？', {icon: 3, title:'确认'}, function(index){
        //发动ajax请求
        $.post("recordEdit/delSection",{s_id:s_id}, function (result) {
            if (result.status != "0") {
                var message = "删除失败";
                if (result.message != null && result.message != "") {
                    message = result.message;
                }
                layer.msg(message, {icon: 2});
                return;
            }
            //请求成功删除触发节点
            sectionItem.remove();
            layer.msg('删除成功', {icon: 1});

            //无剩余分区时显示无查询结果
            if($("#section-list").has("div.card") <= 0)
            {
                $("#no-result").removeClass("d-none");
                $("#no-result").addClass("d-flex");
            }
        });
        layer.close(index);
    });
});
//修改分区
$(".btn-fix").click(function(){
    var s_id = $(this).siblings(".s_id").text();
    var s_name = $(this).parent().parent().find(".section-title");
    layer.prompt({
        title:'分区名修改',
        value: s_name.text(),
        "success": function(){
            $(".layui-layer-input").click(function(){
                this.select();
            });
        }
    }, function(value, index, elem){
        //发动ajax请求
        $.post("recordEdit/editSection",{s_title:value,s_id:s_id}, function (result){
            if (result.status != "0")
            {
                var message = "修改失败";
                if (result.message != null && result.message != "")
                {
                    message = result.message;
                }
                layer.msg(message, {icon: 2});
                return;
            }
            //请求成功修改触发节点
            s_name.text(value);
            layer.msg('修改成功', {icon: 1});
        });
        layer.close(index);
    });
});

//添加页
$(".btn-add").click(function(){
    var s_id = $(this).siblings(".s_id").text();
    var listGroup = $(this).parent().parent().parent().find("ul.list-group");
    layer.prompt({
        title:'添加页',
        value:'请填写页名',
        "success": function(){
            $(".layui-layer-input").click(function(){
                this.select();
            });
        }
    }, function(value, index, elem){
        $.post("recordEdit/addPage",{p_section:s_id, p_title:value}, function (result) {
            if (result.status !== "0")
            {
                var message = "添加失败";
                if (result.message != null && result.message != "")
                {
                    message = result.message;
                }
                layer.msg(message, {icon: 2});
                return;
            }
            var p_id = result.p_id;
            //ajax请求成功后添加页至指定分区
            var pageItem = $(".modal-zone").children(".pageItem").clone(true);
            var btn_section = listGroup.parents(".section").find(".btn-section");
            pageItem.find(".p_id").text(p_id);
            pageItem.find(".p_name").text(value);
            listGroup.append(pageItem);
            layer.msg('添加成功', {icon: 1});
            //执行列表展开
            if(btn_section.attr("aria-expanded") == "false")
            {
                btn_section.click();
            }

        });

        layer.close(index);
    });
});

//删除页
$(".btn-page-close").click(function(){
    var p_id = $(this).siblings(".p_id").text();
    var pageItem = $(this).parents("li.pageItem");
    var listGroup = $(this).parents("ul.list-group");
    var btn_section = $(this).parents("div.card").find("button.btn-section");

    layer.confirm('确定删除？', {icon: 3, title:'确认'}, function(index){
        //发动ajax请求
        $.post("recordEdit/delPage",{p_id:p_id}, function (result) {
            if (result.status != "0") {
                var message = "删除失败";
                if (result.message != null && result.message != "") {
                    message = result.message;
                }
                layer.msg(message, {icon: 2});
                return;
            }
            //请求成功删除触发节点
            pageItem.remove();
            //当该分区无页项目是折叠列表
            if(listGroup.has("li.pageItem").length <= 0)
            {
                btn_section.click();
            }
            layer.msg('删除成功', {icon: 1});

        });
        //请求成功删除触发节点
        layer.close(index);
    });
});
//修改页
$(".btn-page-fix").click(function(){
    var p_id = $(this).siblings(".p_id").text();
    var p_name = $(this).parent().siblings(".p_name");
    layer.prompt({
        title:'页名修改',
        value: p_name.text(),
        "success": function(){
            $(".layui-layer-input").click(function(){
                this.select();
            });
        }
    }, function(value, index, elem){
        //发动ajax请求
        $.post("recordEdit/editPage",{p_title:value,p_id:p_id}, function (result){
            if (result.status != "0")
            {
                var message = "修改失败";
                if (result.message != null && result.message != "")
                {
                    message = result.message;
                }
                layer.msg(message, {icon: 2});
                return;
            }
            //请求成功修改触发节点
            p_name.text(value);
            layer.msg('修改成功', {icon: 1});
        });

        //请求成功修改触发节点
        layer.close(index);
    });
});
