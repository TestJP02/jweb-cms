$.prototype.serializeToObject = function() {
    var array = $(this).serializeArray(),
        object = {},
        i = 0;
    for (; i < array.length; i += 1) {
        var data = array[i];
        object[data.name] = data.value;
    }
    return object;
};

$.prototype.invalidFields = function(fields) {
    for (var i = 0; i < fields.length; i += 1) {
        this.invalid(fields[i].path);
    }
};
$.prototype.invalid = function(path) {
    var node = $(this).find("[name='" + path + "']");
    node.addClass("error");
    node.change(function() {
        node.removeClass("error");
        node.off("change");
    });
};

$.prototype.transInClass = function(cls) {
    this.css("animation", "transformIcon 0.4s linear");
    setTimeout(function() {
        this.addClass(cls);
    }.bind(this), 200);
    setTimeout(function() {
        this.css("animation", "");
    }.bind(this), 450);
};

$.prototype.transOutClass = function(cls) {
    this.css("animation", "transformIconBack 0.4s linear");
    setTimeout(function() {
        this.removeClass(cls);
    }.bind(this), 200);
    setTimeout(function() {
        this.css("animation", "");
    }.bind(this), 450);
};


$(function() {
    Comment.init();
});

var pageId = $("#pageId").val();
var parentTemplate = $("#template-parent").html();
var childTemplate = $("#template-child").html();
var moreTemplate = $("#template-more").html();
Comment = {
    $form: $(".user-comment-form"),
    $container: $(".comment-container"),
    forbidden: $(".user-comment__forbidden"),
    loginButton: $(".user-comment-login"),
    init: function() {
        this.$container.on("click", ".user-comment-submit", function(e) {
            this.submit($(e.currentTarget));
        }.bind(this));
        this.$container.on("click", ".comment-more-btn", function(e) {
            this.more($(e.currentTarget));
        }.bind(this));
        this.$container.on("click", ".comment-reply-btn", function(e) {
            if ($("#allow").val() === "false") {
                window.location.href = "/login";
                return;
            }
            this.reply($(e.currentTarget));
        }.bind(this));
        this.$container.on("click", ".reply-cancel", function(e) {
            var $e = $(e.currentTarget);
            $e.parents(".reply-content").hide();
        });
        this.$container.on("click", ".reply-submit", function(e) {
            this.submit($(e.currentTarget));
        }.bind(this));
        this.loginButton.on("click", function(e) {
            setCookie("_from_url", window.location.href, 3600);
            window.location.href = "/user/login";
        }.bind(this));
    },

    submit: function($e) {
        var $commentText = $e.siblings(".user-comment-text"),
            request = {
                parentId: $commentText.data("id"),
                content: $commentText.val(),
                pageId: pageId
            };
        $.ajax({
            url: "/web/api/comment",
            method: "POST",
            data: JSON.stringify(request),
            contentType: "application/json",
            dataType: "json"
        }).then(function(response) {
            $e.parents(".reply-content").hide();
            var html = replyHtml(response);
            if (response.comment.parentId) {
                $e.parents(".comment-content").find(">.comment-reply>ul").prepend(html);
            } else {
                $(".comment-list").prepend(html);
            }
            $commentText.val("");
        }.bind(this)).fail(function(response) {
            if (response.status === 401) {
                window.location.href = "/login";
            } else if (response.status === 403) {
                var forbidden = this.forbidden;
                forbidden.slideDown();
                setTimeout(function() {
                    forbidden.fadeOut(2000);
                }, 3000);
            }
        }.bind(this));
    },

    reply: function($e) {
        var $replyContent = $e.siblings(".reply-content");
        $replyContent.find("textarea").val();
        $replyContent.show();
    },

    more: function($e) {
        var request = {
            firstParentId: $e.data("firstParentId"),
            page: $e.data("page") + 1
        };
        $.ajax({
            url: "/web/api/comment/page/" + pageId,
            method: "PUT",
            data: JSON.stringify(request),
            contentType: "application/json",
            dataType: "json"
        }).then(function(response) {
            var remainNum = response.total - ((response.page - 1) * response.limit + response.items.length),
                $parent = $e.parent("li"),
                html = "";
            for (var i = 0; i < response.items.length; i++) {
                var item = response.items[i],
                    comment = item.comment;
                if (comment.parentId) {
                    html += replyHtml(comment);
                } else {
                    html += parentHtml(item);
                }
            }
            $parent.before(html);
            if (remainNum <= 0) {
                $parent.remove();
            } else {
                $e.find("span").html(remainNum);
            }
            $e.data("page", response.page);
        }).fail(function() {
            alert("Load failed");
        });
    }
};

function parentHtml(parent) {
    var children = parent.children,
        html = $(parentTemplate);
    if (parent.comment.user.imageURL) {
        html.find(".comment-author-avatar").html("<img src='" + parent.comment.user.imageURL + "' alt=\"\">");
    }
    html.find(".comment-author-name").text(parent.comment.user.username);
    html.find(".comment-author-date>span").text(dateString(parent.comment.createdTime));
    html.find(".comment-content-text>span").text(parent.comment.content);
    html.find(".comment-reply-btn").data("id", parent.comment.id);
    var listHtml = $("<ul></ul>");
    for (var j = 0; j < parent.children.items.length; j++) {
        var comment = children.items[j];
        listHtml.append(replyHtml(comment));
    }
    if (parent.remainNum > 0) {
        var moreBtn = $(moreTemplate);
        moreBtn.find(".comment-more__count").test(parent.remainNum);
        moreBtn.data("first-parent-id", parent.comment.id);
        moreBtn.data("page", parent.children.page);
        listHtml.append(moreBtn);
    }
    html.find(".comment-reply").html(listHtml);
    return html;
}

function replyHtml(comment) {
    var html = $(childTemplate);
    if (comment.user.imageURL) {
        html.find(".comment-author-avatar").html("<img src='" + comment.user.imageURL + "' alt=\"\">");
    }
    html.find(".comment-author-name>span").text(comment.user.nickname);
    html.find(".comment-content-text>span").text(comment.comment.content);
    html.find(".comment-reply-btn").data("id", comment.comment.id);
    html.find(".user-comment-text").data("id", comment.comment.id);
    return html;
}

function dateString(date) {
    var date = new Date(date);
    return date.getFullYear() + "-" + (date.getMonth() < 9 ? "0" : "") + (date.getMonth() + 1) +
        "-" + (date.getDate() < 9 ? "0" : "") + date.getDate() +
        " " + (date.getHours() < 9 ? "0" : "") + date.getHours() +
        ":" + (date.getHours() < 9 ? "0" : "") + date.getMinutes() +
        ":" + (date.getHours() < 9 ? "0" : "") + date.getSeconds();
}

function setCookie(cookieName, value, expireSeconds) {
    var expireTime = new Date().getTime() + expireSeconds * 1000;
    document.cookie = cookieName + "=" + decodeURI(value) + ((expireSeconds == null) ? "" : ";expires=" + new Date(expireTime).toGMTString());
}