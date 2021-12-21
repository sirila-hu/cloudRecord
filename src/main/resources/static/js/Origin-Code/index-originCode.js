var authority = /*[[${#authentication.authorities.toArray()[0].getAuthority()}]]*/ 'ROLE_ANONYMOUS';

//模态框关闭清除数据
$('.modal').on('hidden.bs.modal', function () {
    $(this).find(".form-control").val("");
});

//执行登陆
$("#btn-login-submit").click(function(){
    var formData = new FormData($("#login-form").find("form")[0]);

    $.ajax({
        url : '/authentication/login',//这里写你的url
        type : 'POST',
        data : formData,
        contentType: false,// 当有文件要上传时，此项是必须的，否则后台无法识别文件流的起始位置
        processData: false,// 是否序列化data属性，默认true(注意：false时type必须是post)
        dataType: 'json',//这里是返回类型，一般是json,text等
        clearForm: true,//提交后是否清空表单数据
        success: function(result) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
            layer.msg('登录成功', {icon: 1});
            authority = result.authorities[0].authority;
            $("#login").addClass("d-none");
            $("#logout").removeClass("d-none");
            $("#login-form").modal('hide');
        },
        error: function(result, status, e) {  //提交失败自动执行的处理函数。
            var message = "请求失败";

            if (result.responseJSON.message != null && result.responseJSON.message != '')
            {
                message = result.responseJSON.message;
            }
            layer.msg(message, {icon: 2});
        }
    });
});

//执行登出
$("#logout").click(function(){
    layer.confirm('确认登出?', function(index){
        window.location.replace('/logout');
        layer.close(index);
    });
});

//账户管理触发
$('#userMan').click(function () {
    if (authority == "ROLE_ANONYMOUS")
    {
        layer.msg("请先完成登陆", {icon: 2});
        return;
    }
    $("#userMan-form").modal('show');
});

//密码修改
$("#btn-pass-submit").click(function(){
    var formData = new FormData($("#userMan-form").find("form")[0]);

    $.ajax({
        url : '/userMan/changePassword',//这里写你的url
        type : 'POST',
        data : formData,
        contentType: false,// 当有文件要上传时，此项是必须的，否则后台无法识别文件流的起始位置
        processData: false,// 是否序列化data属性，默认true(注意：false时type必须是post)
        dataType: 'json',//这里是返回类型，一般是json,text等
        clearForm: true,//提交后是否清空表单数据
        success: function(result) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
            if (result.status == 1)
            {
                layer.msg(result.message, {icon: 2});
                return;
            }

            layer.msg('修改成功', {icon: 1});
            $("#userMan-form").modal('hide');
            window.location.replace('/logout');
        },
        error: function(result, status, e) {  //提交失败自动执行的处理函数。
            var message = "请求失败";
            layer.msg(message, {icon: 2});
        }
    });
});