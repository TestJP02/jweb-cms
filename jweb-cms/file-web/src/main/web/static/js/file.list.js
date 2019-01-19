$(function () {
    List.init();
});

var List = {
    directoryId: $("#directoryId").val(),
    roles: $("#roles").val(),
    page: {
        total: $("#total").val(),
        page: $("#page").val(),
        limit: $("#limit").val()
    },
    load: function () {

    },
    init: function () {
        $(".file-toolbar__input").change(function (e) {
            var query = $(e.currentTarget).val(),
                url = window.location.href;
            if (url.indexOf("?") < 0) {
                window.location.href = url + "?query=" + query;
                return;
            }
            var params = url.substring(url.indexOf("?") + 1, url.length).split("&"),
                containPathParam = false;
            url = url.substring(0, url.indexOf("?"));
            for (var i = 0; i < params.length; i++) {
                var split = params[i].split("=");
                if (split[0] === "query") {
                    split[1] = query;
                    containPathParam = true;
                }
                if (i === 0) {
                    url += "?";
                } else {
                    url += "&";
                }
                url += split[0] + "=" + split[1];
            }
            if (!containPathParam) {
                url += "&query=" + query;
            }
            window.location.href = url;
        });
        $(".file-table__path").click(function (e) {
            var $e = $(e.currentTarget),
                path = $e.html(),
                directory = $e.data("directory"),
                url = window.location.href;
            if (!directory) {
                window.location.href = "/file" + path;
                return;
            }
            if (url.indexOf("?") < 0) {
                window.location.href = url + "?path=" + path;
                return;
            }
            var params = url.substring(url.indexOf("?") + 1, url.length).split("&"),
                containPathParam = false;
            url = url.substring(0, url.indexOf("?"));
            for (var i = 0; i < params.length; i++) {
                var split = params[i].split("=");
                if (split[0] === "path") {
                    split[1] = path;
                    containPathParam = true;
                }
                if (i === 0) {
                    url += "?";
                } else {
                    url += "&";
                }
                url += split[0] + "=" + split[1];
            }
            if (!containPathParam) {
                url += "&path=" + path;
            }
            window.location.href = url;
        });
        $(".file__delete").click(function (e) {
            var $e = $(e.currentTarget),
                id = $e.data("id"),
                directory = $e.data("directory");
            if (directory) {
                $.ajax({
                    url: "/web/api/directory/" + id,
                    method: "DELETE"
                }).then(function () {
                    window.location.reload();
                });
            } else {
                $.ajax({
                    url: "/web/api/file/" + id,
                    method: "DELETE"
                }).then(function () {
                    window.location.reload();
                });
            }
        });
    }
};