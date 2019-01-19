window.Uploader = {
    panel: $(".file-uploader"),
    fileList: $(".file-uploader__table-body"),
    loadingBar: $(".file-uploader__row-progress"),
    failMessage: $(".file-uploader__message-inner-fail"),
    successMessage: $(".file-uploader__message--success"),
    fileQueue: [],
    isUploadHold: false,
    running: false,
    path: "test",
    uploadingNumber: $("#uploading_number"),
    uploadedNumber: $("#uploaded_number"),
    failedNumber: $(".failed_number"),
    successNumber: $(".success_number"),
    init: function () {
        $(document).on("change", ".file-upload-handler", function (event) {
            var files = event.target.files;
            if (!files || !files[0]) {
                return;
            }
            this.addQueue(files);
            this.renderNodes();
            this.startUpload();
        }.bind(this));
        $(".file-uploader__toggle").click(function () {
            this.expand();
        }.bind(this));
        $(".file-uploader__close").click(function () {
            this.hide();
        }.bind(this))
    },
    startUpload: function () {
        this.show();
        if (this.running) {
            return;
        }
        if (this.fileQueue.length > 0 && !this.isUploadHold) {
            this.running = true;
            this.successMessage.removeClass("show");
            this.failMessage.removeClass("show");
            this.upload(this.nextFile());
        }
    },
    nextFile: function () {
        for (var i = 0; i < this.fileQueue.length; i += 1) {
            if (!this.fileQueue[i].uploaded) {
                return this.fileQueue[i];
            }
        }
        this.finishUpload();
    },
    finishUpload: function () {
        this.running = false;
        var success = 0;
        var fail = 0;
        for (var i = 0; i < this.fileQueue.length; i += 1) {
            if (this.fileQueue[i].status === "success") {
                success += 1;
            } else {
                fail += 1;
            }
        }
        this.failedNumber.text(fail);
        this.successNumber.text(success);
        if (fail > 0) {
            this.failMessage.addClass("show");
        } else {
            this.successMessage.addClass("show");
        }
        if (success > 0) {
            window.location.reload();
        }
    },
    upload: function (file) {
        if (!file) {
            return;
        }

        var formData = new FormData();
        formData.append("file", file.file);
        $.ajax({
            type: 'POST',
            url: "/web/api/file/upload?directoryId=" + $("#directoryId").val(),
            contentType: false,
            accept: "application/json",
            processData: false,
            data: formData,
            xhr: function () {
                var xhr = new window.XMLHttpRequest();
                //Upload progress
                xhr.upload.addEventListener("progress", function (evt) {
                    if (evt.lengthComputable) {
                        var progress = evt.loaded / evt.total * 100;
                        Uploader.progress(progress, file);
                    }
                }, false);
                //Download progress
                xhr.addEventListener("progress", function (evt) {
                }, false);
                return xhr;
            },
            success: function (data) {
                file.status = "success";
                Uploader.finish(file);
                Uploader.upload(Uploader.nextFile());
            },
            error: function (data) {
                file.status = "error";
                Uploader.finish(file);
                Uploader.upload(Uploader.nextFile());
            }
        });
    },
    progress: function (progress, file) {
        this.loadingBar.fadeIn();
        this.loadingBar.css("top", file.index * 44 + "px");
        this.loadingBar.css("width", progress + "%");
        $("#uploading-index-" + file.index).find(".file-uploader__status").text(progress + "%");
    },
    finish: function (file) {
        file.uploaded = true;
        var row = $("#uploading-index-" + file.index),
            status = row.find(".file-uploader__status");
        if (file.status === "success") {
            status.html("<i class='iconfont icon-zhengque'>");
        } else {
            status.html("<i class='iconfont icon-tishi'>");
        }
        this.loadingBar.fadeOut(function () {
            this.loadingBar.css("width", 0);
        }.bind(this));
        this.uploadedNumber.text(this.calcUploadedNumber());
    },
    calcUploadingNumber: function () {
        var num = 0;
        for (var i = 0; i < this.fileQueue.length; i += 1) {
            if (!this.fileQueue[i].uploaded) {
                num += 1;
            }
        }
        return num;
    },
    calcUploadedNumber: function () {
        var num = 0;
        for (var i = 0; i < this.fileQueue.length; i += 1) {
            if (this.fileQueue[i].uploaded) {
                num += 1;
            }
        }
        return num;
    },
    renderNodes: function () {
        this.fileQueue.forEach(function (file) {
            if (!file.rendered) {
                this.renderNode(file);
            }
        }.bind(this));
    },
    renderNode: function (file) {
        var node = $("<tr class='file-uploader__row' id='uploading-index-" + file.index + "'></tr>");
        node.append("<td class='file-uploader__col' style='width:210px;'>" + file.file.name + "</td>");
        node.append("<td class='file-uploader__col' style='width:100px;text-align: center'>" + file.file.size + "</td>");
        node.append("<td class='file-uploader__col' style='width:100px;'>" + this.path + "</td>");
        node.append("<td class='file-uploader__col' style='width:50px;'><div class='file-uploader__status'></div></td>");
        this.fileList.append(node);
        file.rendered = true;
    },
    addQueue: function (files) {
        this.holdUpload();
        for (var index = 0; index < files.length; index += 1) {
            this.fileQueue.push({
                index: this.fileQueue.length,
                file: files[index],
                rendered: false,
                uploaded: false
            });
        }
        this.uploadingNumber.text(this.calcUploadingNumber());
        this.unHoldUpload();
    },
    holdUpload: function () {
        this.isUploadHold = true;
    },
    unHoldUpload: function () {
        this.isUploadHold = false;
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
    fold: function () {
        this.panel.removeClass("expand");
    },
};

Uploader.init();