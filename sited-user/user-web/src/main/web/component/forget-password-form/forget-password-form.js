(function (window, document) {
    $.prototype.invalid = function (path) {
        var node = $(this).find("[name='" + path + "']");
        node.addClass("error");
        node.change(function () {
            node.removeClass("error");
            node.off("change");
        });
    };

    var ForgetPassword = {
        form: $("#user-forget-password-form"),
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

                if (!ForgetPassword.validate(object)) {
                    return false;
                }
                ForgetPassword.submit(object);
            }.bind(this));
        },
        validate: function (data) {
            var userNameStrategy = $("#userNameStrategy").val();
            if (userNameStrategy === "EMAIL") {
                return this.validateEmail(data.username);
            }
            if (userNameStrategy === "PHONE") {
                return this.validatePhone(data.username);
            }
            return true;
        },
        validatePhone: function (username) {
            var result = (/^1[3578]\d{9}$/).test(username);
            if (!result) {
                this.form.invalid("username");
            }
            return result;
        },
        validateEmail: function (username) {
            var result = (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/).test(username);
            if (!result) {
                this.form.invalid("username");
            }
            return result;
        },
        submit: function (data) {
            $.ajax({
                url: "/web/api/user/reset-password",
                method: "POST",
                data: JSON.stringify(data),
                contentType: "application/json",
                dataType: "json"
            }).done(function () {
                window.location.href = "/password/forget/sent";
            }).fail(function (result) {
                ForgetPassword.form.invalid(result.responseJSON.field);
            });
        }
    };
    ForgetPassword.init();
})(this, this.document);
