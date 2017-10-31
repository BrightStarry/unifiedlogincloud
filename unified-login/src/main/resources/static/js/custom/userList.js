var url = {
    userList : '/user/list/',//分页查询
    addUser : '/user/add',//新增用户
    searchUserByUsername : "/user/search/name/",//根据用户名搜索用户
    commonPre : "/user/",//通用前缀
    modifyPwdSuf : "/modify/pwd",//修改用户密码后缀,
    modifyStatusSuf : "/modify/status/",//修改用户状态后缀
    pageByRootPlatformSuf : "/rootPlatform/list/",//分页查询用户授权平台后缀
    getOrCancelRootSuf : "/root/",//获取或取消用户-平台间的授权后缀
    getRootByPlatformNameSuf : "/root/name/",//根据平台名获取授权后缀
    getAllRootSuf : "/getAllRoot",//获取所有授权后缀后缀
    cancelAllRootSuf : "/cancelAllRoot",//取消所有授权后缀


};
var tempUserId;//用作临时保存的用户id
var userList = {
    //分页查询
    page : function(pageNo) {
        $.get(url.userList + pageNo, function (result) {
            $('#tableContent').html(result);
        });
    },
    //新增用户
    addUser:function () {
        $.post(url.addUser,$('#addForm').serialize() ,function (result) {
            //提示
            if(result.code == '0000') {
                common.showMessage('成功');
                common.closeModal($('#addForm')[0],$('#addModal'));
                userList.page(1);
            }else {
                common.showMessage(result.message);
            }
        });
    },
    //根据用户名搜索用户
    searchUserByUsername : function (username) {
        if(username == ''){
            userList.page(1);
            return
        }
        $.get(url.searchUserByUsername + username,function (result) {
            $('#tableContent').html(result);
        });
    },
    //点击用户名搜索框，清空内容
    searchUserInputClean : function (input) {
        //当前内容
        var tempStr = input.val();
        //清空内容
        input.val("");
        //如果之前是有内容的，就重新加载第一页
        if(tempStr)
            userList.page(1);
    },
    //弹出修改密码模态框
    openModifyPwdModal : function (userId) {
        tempUserId = userId;
        $('#modifyPwdModal').modal({closeViaDimmer: 0, width: 600, height: 300});
    },
    //修改密码
    modifyPwd : function () {
        $.post(url.commonPre + tempUserId + url.modifyPwdSuf,{afterPwd:$('input[name="afterPwd"]').val()},function (result) {
            //提示
            if(result.code == '0000') {
                common.closeModal($('#modifyPwdForm')[0],$('#modifyPwdModal'));
                common.showMessage('成功');
            }else {
                common.showMessage(result.message);
            }
            userList.page(1);
        });
    },
    //修改状态,1:启用，0：停用
    modifyStatus : function (userId,status) {
        $.post(url.commonPre + userId + url.modifyStatusSuf + status, function (result) {
            if(result.code == '0000') {
                common.showMessage('成功');
                userList.page(1);
            }else{
                common.showMessage(result.message);
            }
        });
    },
    //打开某用户的授权平台列表模态框
    openRootPlatformListModal : function (userId) {
        userList.pageByRootPlatform(userId,1);
        $('#rootPlatformListModal').modal();
    },
    //分页查询某用户所有授权的平台
    pageByRootPlatform :function (userId,pageNo) {
        $.get(url.commonPre + userId + url.pageByRootPlatformSuf +  pageNo, function (result) {
            $('#rootPlatformTableContent').html(result);
        });
    },
    //获取/取消某用户和某平台间的授权
    getOrCancelRoot : function (userId, platformId, status) {
        //拼接路径
        var getOrCancelRootPath = url.commonPre + userId + url.getOrCancelRootSuf + platformId + "/" + status;
        $.post(getOrCancelRootPath, function (result) {
            if(result.code == '0000') {
                common.showMessage('成功');
                userList.pageByRootPlatform(userId, 1);
            }else{
                common.showMessage(result.message);
            }
        })
    },
    //打开新增授权模态框
    openAddRootModal : function (userId) {
        tempUserId = userId;
        $('#addRootModal').modal({closeViaDimmer: 0, width: 600, height: 300});
    },
    //新增授权
    addRoot : function () {
        $.post(url.commonPre + tempUserId + url.getRootByPlatformNameSuf + $('#platformName').val(), function (result) {
            if(result.code == '0000') {
                common.showMessage('成功');
                common.closeModal($('#addRootForm')[0],$('#addRootModal'))
            }else{
                common.showMessage(result.message);
            }
        });
    },
    //给用户授权所有平台
    addAllRoot : function (userId) {
        $.post(url.commonPre + userId + url.getAllRootSuf, function (result) {
            if(result.code == '0000') {
                common.showMessage('成功');
            }else{
                common.showMessage(result.message);
            }
        });
    },
    //取消授权所有平台
    cancelAllRoot : function (userId) {
        $.post(url.commonPre + userId + url.cancelAllRootSuf, function (result) {
            if(result.code == '0000') {
                common.showMessage('成功');
            }else{
                common.showMessage(result.message);
            }
        });
    }
};