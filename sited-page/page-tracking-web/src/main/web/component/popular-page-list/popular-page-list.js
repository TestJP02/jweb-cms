$(function() {
    var tabs = $(".page-rank__tab");
    var lists = $(".page-rank__list");
    tabs.hover(function(event) {
        lists.hide();
        lists.eq($(event.currentTarget).index()).show();
    }, function() {
        lists.hide();
        lists.eq($(".page-rank__tab.active").index()).show();
    });

    tabs.click(function(event) {
        tabs.removeClass("active");
        $(event.currentTarget).addClass("active");
    });

});