(function (window, document) {
    $.prototype.invalid = function (path) {
        var node = $(this).find("[name='" + path + "']");
        node.addClass("error");
        node.change(function () {
            node.removeClass("error");
            node.off("change");
        });
    };

    $.prototype.invalidFields = function (fields) {
        for (var i = 0; i < fields.length; i += 1) {
            this.invalid(fields[i].path);
        }
    };
    $.prototype.invalidText = function (message) {
        if (!message) {
            $(".form-validation").remove();
            return;
        }
        var node = $("<div class='form-validation form-validation--error'><i class='fa fa-exclamation-circle'></i><span class='form-validation__text'>" + message + "</span></div>");
        $(this).prepend(node);
    };
    var Register = {
        submitBtn: $("#submit"),
        form: $("#user-register-form"),
        waitingPincode: false,
        waitingSubmit: false,
        pinCodeButton: $(".user-login__pincode"),
        init: function () {
            this.initEvent();
            var countDown = $("#countDown").text();
            if (countDown > 0) {
                this.countDownPincode(countDown);
            }
        },
        initEvent: function () {
            this.submitBtn.click(function () {
                this.form.invalidText();
                var array = this.form.serializeArray(),
                    object = {},
                    i = 0;
                for (; i < array.length; i += 1) {
                    var data = array[i];
                    object[data.name] = data.value;
                }

                if (!Register.validate(object)) {
                    return false;
                }
                if (object.username === undefined) {
                    object.username = object.email;
                }
                Register.submit(object);
            }.bind(this));

            this.pinCodeButton.click(function () {
                this.sendPinCode();
            }.bind(this));

        },
        sendPinCode: function () {
            if (this.waitingPincode) {
                return;
            }
            this.form.invalidText();
            var array = this.form.serializeArray(),
                formData = {},
                i = 0;
            for (; i < array.length; i += 1) {
                var data = array[i];
                formData[data.name] = data.value;
            }
            if (!this.validateUserName(formData.username)) {
                return;
            }
            var request = {
                email: formData.username
            };
            $.ajax({
                type: "POST",
                url: "/web/api/pincode",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(request),
                success: function (data) {
                    this.countDownPincode();
                }.bind(this),
                error: function (data) {
                    alert("upload failed");
                }
            });
        },
        countDownPincode: function (countDown) {
            this.waitingPincode = true;
            var interval = countDown ? countDown : 60;
            this.pinCodeButton.addClass("waiting");
            var count = function () {
                this.pinCodeButton.attr("countdown", interval);
                interval -= 1;
                if (interval > 0) {
                    setTimeout(count.bind(this), 1000);
                } else {
                    this.pinCodeButton.removeClass("waiting");
                    this.waitingPincode = false;
                }
            }.bind(this);
            count();
        },
        validate: function (data) {
            if (!this.validateUserName(data.username)) {
                this.form.invalid("username");
                return false;
            }
            if (!this.validatePassword(data.password)) {
                return false;
            }
            if (!data.pinCode) {
                this.form.invalid("pinCode");
                return false;
            }
            return true;
        },
        validateUserName: function (username) {
            var userNameStrategy = $("#userNameStrategy").val();
            var userInput = $("[name=username]");
            if (userNameStrategy === "EMAIL") {
                if (!this.validateUsernameRegex(username, "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$")) {
                    this.form.invalidText(userInput.data("ruleNeedemail"));
                    return false;
                }
            } else if (userNameStrategy === "PHONE") {
                if (!this.validateUsernameRegex(username, "^1[3578][0-9]{9}$")) {
                    this.form.invalidText(userInput.data("ruleNeedphone"));
                    return false;
                }
            } else if (userNameStrategy === "EMAIL_PHONE") {
                if (!this.validateUsernameRegex(username, "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$")
                    && !this.validateUsernameRegex(username, "^1[3578][0-9]{9}$")) {
                    this.form.invalidText(userInput.data("ruleNeedemailorphone"));
                    return false;
                }
            } else if (!this.validateUsernameRegex(username, "^[a-zA-Z](1)[a-zA-Z0-9]+$")) {
                this.form.invalidText(userInput.data("ruleNeedusername"));
                return false;
            }
            return true;
        },
        validateUsernameRegex: function (username, regex) {
            return new RegExp(regex).test(username);
        },
        validatePassword: function (password) {
            var min = 6;
            if (password.length < min) {
                this.form.invalid("password");
                this.form.invalidText($("[name=password]").data("ruleLimit").replace(/\d/, min));
                return false;
            }
            return true;
        },
        submit: function (data) {
            $.ajax({
                url: "/web/api/user/register",
                method: "POST",
                data: JSON.stringify(data),
                contentType: "application/json",
                dataType: "json"
            }).then(function () {
                window.location.href = "/";
            }).fail(function (result) {
                Register.form.invalidFields(result.responseJSON.field);
                Register.form.invalidText(result.responseJSON.message);
            });
        }
    };
    Register.init();
})(this, this.document);
