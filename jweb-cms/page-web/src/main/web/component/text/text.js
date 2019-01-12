$(function() {
    $.ajax({
        url: "/web/api/user/user-info",
        dataType: "json"
    }).done(function(user) {
        if (user.authenticated) {
            var html = $($("#login-menu").html());
            html.find(".page-header__operation-avatar img").attr("src", user.imageURL);
            html.find(".page-header__operation-username").text(user.nickname);
            $(".header-account").html(html);
        }
    });

    $(".switch-language").click(function() {
        var language = $(this).data("language");
        $.get("/web/api/switch-language/" + language).done(function() {
            window.location.reload();
        });
    });
});