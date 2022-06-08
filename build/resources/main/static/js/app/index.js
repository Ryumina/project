var main = {
    init : function () {
        var _this = this;

        // 등록 버튼 클릭시
        $("#btn-save").on('click', function () {
            _this.save();
        });

        // 수정 완료 버튼 클릭시
        $("#btn-update").on('click', function () {
            _this.update();
        });

        // 삭제 버튼 클릭시
        $("#btn-delete").on('click', function () {
            _this.delete();
        });
    },
    save : function () {
        var data = {
            title: $("#title").val(),
            author: $("#author").val(),
            content: $("#content").val()
        };

        $.ajax({
           type: 'POST',
           url: '/api/v1/posts',
           dataType: 'json',
           contentType: 'application/json; charset=utf-8',
           data: JSON.stringify(data)
        }).done(function () {
            // ajax 호출이 끝난 후
            alert('글이 등록되었습니다.');

            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update: function () {
        var data = {
            title: $("#title").val(),
            content: $("#content").val()
        };

        var id = $("#id").val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert("글이 수정되었습니다.");
            window.location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    delete: function () {
        var id = $("#id").val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert("게시글이 삭제되었습니다.");
            window.location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();