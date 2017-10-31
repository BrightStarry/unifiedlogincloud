var common = {
    //填充消息并提示
    showMessage : function (message) {
        $('#message').text(message);
        $('#messageModal').modal();
    },
    //清空表单并关闭窗口
    closeModal : function ($form,$modal) {
        $form.reset();
        $modal.modal('close');
    },

};