//路径对象
var url ={
    login : "account/login",//登录
};
var login = {
    init : function () {
        /**
         * 单击登录按钮
         */
        $('#loginButton').click(function(){
            //账号校验
            //密码校验
            //ajax提交
            $.post(url.login, {username:$('#username').val(), password:$('#password').val()}, function (result) {
                //登录成功
                if(result.code == '0000'){
                    window.location.href="/user/list";
                    return;
                }
                //登录失败
                $('#warnMessage').text(result.message);
                $('#warnDiv').show();
            });
        });

        /**
         * 单击回车
         */
        $(document).keydown(function (event) {
            if (event.keyCode == 13) {
                $('#loginButton').click();
            }
        });
    }

};


