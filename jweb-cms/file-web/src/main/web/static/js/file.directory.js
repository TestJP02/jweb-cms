window.Directory = {
    panel: $(".file-directory"),
    form: $("#form"),
    init: function () {
        $(document).on("click", ".directory-create-handler", function (event) {
            this.show();
        }.bind(this));
        $(".file-directory__close").click(function () {
            this.hide();
        }.bind(this));
        $("#submit").click(function () {
            this.submit(this.serializeToObject());
        }.bind(this));
    },
    submit: function (data) {
        if (!Directory.validate(data)) {
            return;
        }
        $.ajax({
            url: "/web/api/directory",
            method: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json"
        }).then(function () {
            window.location.reload();
        }).fail(function (result) {
            alert("Save failed");
        });
    },
    validate: function (data) {
        var path = data.path;
        if (path.indexOf("/") !== 0 || path.lastIndexOf("/") !== path.length - 1) {
            alert("path is not legal, example: /XXX/");
            return false;
        }
        if (!data.displayName) {
            alert("please enter displayName");
            return false;
        }
        return true;
    },
    serializeToObject: function () {
        var array = Directory.form.serializeArray(),
            object = {},
            i = 0;
        for (; i < array.length; i += 1) {
            const data = array[i];
            if (data.name.indexOf(".") !== -1) {
                var nameArr = data.name.split(".");
                if (object[nameArr[0]] === undefined) {
                    object[nameArr[0]] = {};
                }
                if (object[nameArr[0]][nameArr[1]] === undefined) {
                    object[nameArr[0]][nameArr[1]] = [];
                }
                object[nameArr[0]][nameArr[1]].push(data.value);
            } else {
                object[data.name] = data.value;
            }
        }
        return object;
    },
    show: function () {
        this.panel.addClass("show");
        this.expand();
    },
    hide: function () {
        this.panel.removeClass("show");
    },
    expand: function () {
        this.panel.addClass("expand");
    },
};

Directory.init();