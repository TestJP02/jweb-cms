$(function () {
    app.user().then(function (data) {
        if (data.authenticated) {
            $(".page-author__author-name").text(data.nickname);
            $(".page-author__author-avatar").html("<img src='" + data.imageURL + "'>");
        }
    });

});