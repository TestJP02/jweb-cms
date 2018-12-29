(function (window, document) {
    $.prototype.invalid = function (path) {
        var node = $(this).find("[name='" + path + "']");
        node.addClass("error");
        node.change(function () {
            node.removeClass("error");
            node.off("change");
        });
    };

    $.prototype.invalidText = function (message) {
        if (!message) {
            $(".form-validation").remove();
            return;
        }
        var node = $("<div class='form-validation form-validation--error'><i class='fa fa-exclamation-circle'></i><span class='form-validation__text'>" + message + "</span></div>");
        $(this).prepend(node);
    };

    var Login = {
        submitBtn: $("#submit"),
        form: $("#user-login-form"),
        init: function () {
            this.initEvent();
        },
        initEvent: function () {
            this.submitBtn.click(function () {
                var array = this.form.serializeArray(),
                    object = {},
                    i = 0;
                for (; i < array.length; i += 1) {
                    var data = array[i];
                    object[data.name] = data.value;
                }

                Login.submit(object);
            }.bind(this));
        },
        submit: function (data) {
            Login.form.invalidText();
            $.ajax({
                url: "/web/api/user/login",
                method: "POST",
                data: JSON.stringify(data),
                contentType: "application/json",
                dataType: "json"
            }).then(function (loginResponse) {
                window.location.href = loginResponse.fromURL? loginResponse.fromURL: "/";
            }).fail(function (result) {
                Login.form.invalid(result.responseJSON.field);
                Login.form.invalidText(result.responseJSON.message);
            });
        }
    };
    Login.init();
})(this, this.document);
