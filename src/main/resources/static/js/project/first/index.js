
//3초 뒤에 로딩화면 사라짐 + 글씨 순서대로 사라짐
setTimeout(function() {
    $('.one').css({"opacity":"0"},200,"linear");
    $('.two').css({"opacity":"0"},200,"linear");
    $('.three').css({"opacity":"0"},200,"linear");
    $('.four').css({"opacity":"0"},200,"linear");
    $('.five').css({"opacity":"0"},200,"linear");
    $('.six').css({"opacity":"0"},200,"linear");
    $('.seven').css({"opacity":"0"},200,"linear");
    $('.content').css({"left":"-100%"});
},3000);

setTimeout(function() {
    $('.about_wrap').animate({"top":"12%"},1000,"easeInOutQuint");
},4000);
setTimeout(function() {
    $('.portfolio_wrap').animate({"left":"8%"},3000,"easeInOutQuint");
},5000);
setTimeout(function() {
    $('.contact_wrap').animate({"right":"8%"},3000,"easeInOutQuint");
},5300);

//nav bar
$(document).ready(function () {
    $(document).on('click', '.block', function () {
        $(this).toggleClass('active')
    })
});

//nav
$(function(){
    var a=0;

    $(".block").click(function(){
        a++;
        a=a%2;

        if(a==1){
            $("#nav_wrap").animate().stop();
            $("#nav_wrap").animate({"bottom":"0"},2000,"easeOutBounce");
        }else{
            $("#nav_wrap").animate().stop();
            $("#nav_wrap").animate({"bottom":"-100vh"},1500,"easeInOutQuint");
        }
    });
});

