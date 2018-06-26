$(function() {
    app.user().then(function(data) {
        if (data.authenticated) {
            var html = $($("#login-menu").html());
            html.find(".page-header__operation-avatar img").attr("src", data.imageURL);
            html.find(".page-header__operation-username").text(data.nickname);
            $(".header-account").html(html);
        }
    });
    var body = $("body");
    var header = $(".page-header");
    var search = header.find(".page-header__search");
    var searchInput = header.find(".page-header__search-input");
    var searchMask = header.find(".page-header__search-mask");
    search.click(function() {
        header.addClass("searching");
        search.css("width", header.width() + "px");
    });
    searchMask.click(function() {
        searchInput.val("");
        header.removeClass("searching");
        search.css("width", "");
    });
    $(".page-header__operation-toggle").click(function() {
        header.toggleClass("shown-links");
        body.toggleClass("fixed");
    });
    $(".page-header__nav-list-mask").click(function() {
        header.removeClass("shown-links");
        body.removeClass("fixed");
    });
    $(".page-nav__link").click(function(event) {
        $(event.currentTarget).parent().toggleClass("shown");
    });

});