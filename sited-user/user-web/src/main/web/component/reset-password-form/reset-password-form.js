(function (window, document) {
    $.prototype.invalid = function (path) {
        var node = $(this).find("[name='" + path + "']");
        node.addClass("error");
        node.change(function () {
            node.removeClass("error");
            node.off("change");
        });
    };
    var ResetPassword = {
        form: $("#reset-password-form"),
        submitBtn: $(".btn-submit"),
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

                if (!ResetPassword.validate(object)) {
                    return false;
                }
                ResetPassword.submit(object);
            }.bind(this));
        },
        validate: function (data) {
            return this.validateNewPassword(data.password, data.newPassword);
        },
        validateNewPassword: function (password, newPassword) {
            var result = password === newPassword;
            if (!result) {
                this.form.invalid("newPassword");
            }
            return result;
        },
        submit: function (data) {
            $.ajax({
                url: "/web/api/user/reset-password/apply",
                method: "POST",
                data: JSON.stringify(data),
                contentType: "application/json",
                dataType: "json"
            }).then(function () {
                window.location.href = "/login";
            }).fail(function (result) {
                ResetPassword.form.invalid(result.responseJSON.field);
            });
        }
    };
    ResetPassword.init();
})(this, this.document);
