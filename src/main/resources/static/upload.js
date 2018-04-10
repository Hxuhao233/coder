
$(function () {
    console.log("start");
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#upload").click(function () {
        console.log("upload");
        var formData = new FormData($('form')[0]);
        $.ajax({
            url: '/source/upload',
            type: 'POST',
            data: formData,
            success:function(data){
                console.log(data);
                alert(data.msg);
            },
            error:function(){
                alert("上传失败！");
            }
        })
    });
});

