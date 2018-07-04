$(function() {
    $.ajax({
        url: "/web/api/user/user-info"
    }).done(function(data) {
        if (data.authenticated) {
            var html = $($("#login-menu").html());
            html.find(".page-header__operation-avatar img").attr("src", data.imageURL);
            html.find(".page-header__operation-username").text(data.nickname);
            $(".header-account").html(html);
        }
    });

    $(".switch-language").click(function () {
        var language = $(this).data("language");
        $.get("/web/api/switch-language/" + language).done(function () {
            window.location.reload();
        });
    });
});