//临时存储单元文本内容
var u_titleCache = "";
var u_contentCache = "";

//模态框关闭清除数据
$('#addUnit-form').on('hidden.bs.modal', function () {
    $(this).find(".form-control").val("");
});
//图片验证
function validate_img(ele){
    var file = ele.value;

    if(!/.(gif|jpg|jpeg|png|GIF|JPG|bmp)$/.test(file)){
        layer.msg("图片类型必须是.gif,jpeg,jpg,png,bmp中的一种", {icon: 2});
        return false;
    }else{
        if(((ele.files[0].size).toFixed(2))>=(2*1024*1024)){
            layer.msg("请上传小于2M的图片", {icon: 2});
            return false;
        }
    }
    return true;
}

//图片点击放大显示
$(".unit-img").click(function(){
    $("#imgShow").find("img").attr("src", $(this).attr("src"));
    $("#imgShow").modal("show");
});
//按钮隐藏
$(".unit-item").hover(function(){
    $(this).children(".unit-btns").animate({opacity: '1'});
}, function(){
    $(this).children(".unit-btns").animate({opacity: '0'});
});

//编辑样式变换
$("span.title-content").focus(function(){
    $(this).siblings(".fa-seedling").fadeOut().fadeIn();
});

//div回车限制
$("span.title-content"). on( 'keydown', function(event){
    //获取keyCode
    var keyCode = event. keyCode;
    if( keyCode === 13){event. preventDefault();}
});

//单元添加
$("#btn-unit-submit").click(function(){
    var formData = new FormData($("#add-form")[0]);
    formData.append("u_page", $("#p_id").text());

    $.ajax({
        url : 'pageEdit/addUnit',//这里写你的url
        type : 'POST',
        data : formData,
        contentType: false,// 当有文件要上传时，此项是必须的，否则后台无法识别文件流的起始位置
        processData: false,// 是否序列化data属性，默认true(注意：false时type必须是post)
        dataType: 'json',//这里是返回类型，一般是json,text等
        clearForm: true,//提交后是否清空表单数据
        success: function(result) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
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
            //添加新单元节点
            var unit = $(".modal-zone").children("div.unit-item").clone(true);
            unit.find(".title-content").text(formData.get("u_title"));
            unit.find(".unit-content").text(formData.get("u_content"));
            unit.find(".u_id").text(result.u_id);
            if (result.u_img != null && result.u_img != "")
            {
                unit.find(".unit-img").attr("src", "/recordProject/img/" + result.u_img);
            }

            $(".unit-list").append(unit);
            if($("#no-result").hasClass("d-none") == false)
            {
                $("#no-result").removeClass("d-flex");
                $("#no-result").addClass("d-none");
            }
            $("#addUnit-form").modal('hide');
            layer.msg('添加成功', {icon: 1});
        },
        error: function(result, status, e) {  //提交失败自动执行的处理函数。
            console.error(e);
            layer.msg("提交失败", {icon: 2});
        }
    });
});
//单元内容修改
$(".btn-unit-fix").click(function(){
    //获取unit-item
    var unit_item =  $(this).parents(".unit-item");
    u_titleCache = unit_item.find(".title-content").text();
    u_contentCache = unit_item.find(".unit-content").text();

    $(".textEdit").attr("contenteditable", "false");

    //触发地区可编辑
    unit_item.find("span.textEdit").attr("contenteditable", "true");
    unit_item.find("span.title-content").focus().select();
});

//取消编辑
$(".textEdit").keydown(function(e){
    if(e.keyCode == 27){
        $(this).parents("div.unit-item").find("span.textEdit").attr("contenteditable", "false");
        //导出存储区数据
        $(this).parents("div.unit-item").find(".title-content").text(u_titleCache);
        $(this).parents("div.unit-item").find(".unit-content").text(u_contentCache);

        //腾空存储区
        u_titleCache = "";
        u_contentCache = "";
    }
});

//保存修改
$(".textEdit").keydown(function(e){
    if( e.ctrlKey  == true && e.keyCode == 83 ){
        var textEdit = $(this).parents(".unit-item").find(".textEdit");
        var u_title = textEdit.filter(".title-content");
        var u_content = textEdit.filter(".unit-content");
        var u_id =$(this).parents(".unit-item").find(".u_id").text();

        //拦截网页保存操作
        event. preventDefault();

        //触发弹窗
        layer.confirm('确定保存？', {icon: 3, title:'确认'}, function(index){
            //发动ajax请求
            $.post("pageEdit/editUnit",{u_title:u_title.text(),u_content:u_content.text(), u_id:u_id}, function (result){
                if (result.status !== "0")
                {
                    var message = "修改失败";
                    if (result.message != null && result.message != "")
                    {
                        message = result.message;
                    }
                    layer.msg(message, {icon: 2});
                    return;
                }
                //腾空存储区
                u_titleCache = "";
                u_contentCache = "";
                //请求成功关闭可编辑状态
                textEdit.attr("contenteditable", "false");
                layer.msg("修改成功", {icon: 1});
            });
            layer.close(index);
        });
    }
});


//删除单元
$(".btn-close").click(function(){
    var u_id = $(this).siblings(".u_id").text();
    var unit_item = $(this).parents(".unit-item");

    layer.confirm('确定删除？', {icon: 3, title:'确认'}, function(index){
        //发动ajax请求
        $.post("pageEdit/delUnit",{u_id:u_id}, function (result) {
            if (result.status !== "0") {
                var message = "删除失败";
                if (result.message != null && result.message != "") {
                    message = result.message;
                }
                layer.msg(message, {icon: 2});
                return;
            }
            //请求成功删除触发节点
            unit_item.remove();
            layer.msg("删除成功", {icon: 1});
        });

        layer.close(index);
    });
});

//图片上传控件
$(".btn-unit-img").click(function(){
    var u_id = $(this).siblings(".u_id").text();
    $("#fileUpload").attr("u_id",u_id);
    $("#fileUpload").click();
});

$("#fileUpload").change(function(){
    // 创建formdata对象
    var formData = new FormData();
    var u_id = $(this).attr("u_id");

    if(!validate_img(this))
    {
        $(this).val("");
        $(this).attr("u_id", "");
        return false;
    }
    // 给formData对象添加<input>标签,注意与input标签的ID一致
    formData.append('u_id', u_id);
    formData.append('u_imgFile', $(this)[0].files[0]);

    $.ajax({
        url : 'pageEdit/changeImg',//这里写你的url
        type : 'POST',
        data : formData,
        contentType: false,// 当有文件要上传时，此项是必须的，否则后台无法识别文件流的起始位置
        processData: false,// 是否序列化data属性，默认true(注意：false时type必须是post)
        dataType: 'json',//这里是返回类型，一般是json,text等
        clearForm: true,//提交后是否清空表单数据
        success: function(result) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
            if (result.status !== "0")
            {
                var message = "修改失败";
                if (result.message != null && result.message != "")
                {
                    message = result.message;
                }
                layer.msg(message, {icon: 2});
                return;
            }
            $('.u_id:contains('+u_id+')').parents(".unit-item").find("img.unit-img").attr("src", '/recordProject/img/'+result.u_img);
            layer.msg("图片修改成功", {icon: 1});
        },
        error: function(data, status, e) {  //提交失败自动执行的处理函数。
            layer.msg("提交失败", {icon: 2});
        }
    });

});