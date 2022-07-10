var findListBoard = {

    init: function () {
        var _this = this;

        // 글등록 버튼 클릭시
        $("#createBoardBtn").on('click', function () {
            _this.loginCheck();
        });
    },
    // 로그인 체크
    loginCheck: function () {
        var loginId = $("#user").text();

        if(loginId == '' || loginId == null) {
            alert('로그인이 필요합니다.');

        } else {
            window.location.href = '/createBoard';
        }
    }
}

findListBoard.init();
