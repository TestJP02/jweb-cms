import React from "react";
import {Button, Dialog, Form, Input, Layout, MessageBox as dialog, Notification as notification, Pagination, Select} from "element-react";
import PropTypes from "prop-types";
import "./page.comment.list.css";

const i18n = window.i18n;
export default class PageCommentList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            pageId: props.match.params.pageId,
            query: {
                title: "",
                status: null,
                page: 1,
                limit: 10
            },
            data: {
                total: 0,
                page: 1,
                limit: 0,
                items: []
            },
            limitOptions: [10, 20, 50],
            statusOptions: [
                {
                    label: i18n.t("comment.statusActive"),
                    value: "ACTIVE"
                },
                {
                    label: i18n.t("comment.statusInactive"),
                    value: "INACTIVE"
                },
                {
                    label: i18n.t("comment.statusAuditing"),
                    value: "AUDITING"
                }
            ],
            keyColumn: {
                label: i18n.t("comment.comment"),
                prop: "content"
            },
            columns: [
                {
                    label: i18n.t("comment.status"),
                    render: function(data) {
                        return (
                            <span>
                                {data.status === "ACTIVE" && i18n.t("comment.statusActive")}
                                {data.status === "INACTIVE" && i18n.t("comment.statusInactive")}
                                {data.status === "AUDITING" && i18n.t("comment.statusAuditing")}
                            </span>
                        );
                    }
                },
                {
                    label: i18n.t("comment.createdTime"),
                    render: function(data) {
                        return (
                            <ElementUI.DateFormatter date={data.createdTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("comment.updatedTime"),
                    render: function(data) {
                        return (
                            <ElementUI.DateFormatter date={data.updatedTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("comment.action"),
                    fixed: "right",
                    width: 200,
                    render: function(current) {
                        if (current.status === "ACTIVE") {
                            return <span>
                                <Button type="text" onClick={() => this.openComment(current.id)}>{i18n.t("comment.reply")}</Button>
                                <Button type="text" onClick={e => this.delete(current, e)}>{i18n.t("comment.delete")}</Button>
                            </span>;
                        }
                        if (current.status === "INACTIVE") {
                            return <Button type="text" onClick={() => this.updateStatus(current, "ACTIVE")}>{i18n.t("comment.revert")}</Button>;
                        }
                        if (current.status === "AUDITING") {
                            return <Button type="text" onClick={() => this.updateStatus(current, "ACTIVE")}>{i18n.t("comment.audit")}</Button>;
                        }
                    }.bind(this)
                }
            ],
            selected: [],
            addComment: false,
            comment: {
                imageURLs: [],
                attachmentURLs: [],
                linkURLs: []
            },
            directoryPath: "/upload/comment/"
        };
    }

    componentWillMount() {
        this.find();
    }

    find() {
        fetch("/admin/api/comment/tree/page/" + this.state.pageId, {
            method: "PUT",
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    queryChange(key, value) {
        this.setState(
            {query: Object.assign(this.state.query, {[key]: value})}
        );
    }

    statusChange(value) {
        let status = value;
        if (!value) {
            status = null;
        }
        this.setState({query: Object.assign(this.state.query, {status: status})}, () => {
            this.find();
        });
    }

    select(selected) {
        this.setState({selected: selected});
    }

    updateStatus(comment, status) {
        fetch("/admin/api/comment/" + comment.id + "/status", {
            method: "PUT",
            body: JSON.stringify({status: status})
        }).then(() => {
            this.find();
        });
    }

    delete(data, e) {
        e.preventDefault();
        dialog.confirm(i18n.t("comment.commentDeleteContent"), "Hint", {type: "warning"})
            .then(() => {
                fetch("/admin/api/comment/batch-delete", {
                    method: "PUT",
                    body: JSON.stringify({ids: [data.id]})
                }).then(() => {
                    notification({
                        title: i18n.t("comment.successTitle"),
                        type: "success",
                        message: i18n.t("comment.deleteSuccessContent")
                    });
                    this.find();
                });
            })
            .catch(() => {
                notification({
                    title: i18n.t("comment.errorTitle"),
                    type: "error",
                    message: i18n.t("comment.deleteCancelContent")
                });
            });
    }

    batchDelete() {
        const list = this.state.selected,
            ids = [];
        if (list.length === 0) {
            return;
        }
        for (let i = 0; i < list.length; i += 1) {
            ids.push(list[i].id);
        }
        dialog.confirm(i18n.t("comment.commentBatchDeleteContent"), "Hint", {type: "warning"}).then(() => {
            fetch("/admin/api/comment/delete", {
                method: "PUT",
                body: JSON.stringify({ids: ids})
            }).then(() => {
                notification({
                    title: i18n.t("comment.successTitle"),
                    type: "success",
                    message: i18n.t("comment.deleteSuccessContent")
                });
                this.find();
            }).catch(() => {
                notification({
                    title: i18n.t("comment.errorTitle"),
                    type: "error",
                    message: i18n.t("comment.deleteCancelContent")
                });
            });
        });
    }

    newComment(parentId) {
        return {
            pageId: this.state.pageId,
            parentId: parentId,
            title: null,
            content: null,
            imageURLs: [],
            attachmentURLs: [],
            linkURLs: []
        };
    }

    formChange(key, value) {
        this.setState({comment: Object.assign({}, this.state.comment, {[key]: value})});
    }

    openComment(parentId) {
        const comment = this.newComment(parentId);
        this.setState({
            comment: comment,
            addComment: true
        });
    }

    addComment() {
        fetch("/admin/api/comment", {
            method: "POST",
            body: JSON.stringify(this.state.comment)
        }).then((response) => {
            notification({
                title: "Success",
                message: i18n.t("comment.updateSuccessMessage"),
                type: "success"
            });
            this.setState({addComment: false}, () => this.find());
        });
    }

    beforeAvatarUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 2;
        const type = file.type;
        let fileTypeValid = true;
        if (type === "image/jpeg" || type === "image/bmp" || type === "image/gif" || type === "image/png") {
            fileTypeValid = true;
        } else {
            notification({
                title: i18n.t("comment.errorTitle"),
                type: "info",
                message: i18n.t("page.imageTypeInvalid")
            });
            fileTypeValid = false;
        }
        if (!isLt2M) {
            notification({
                title: i18n.t("comment.errorTitle"),
                type: "info",
                message: i18n.t("page.imageSizeInvalid")
            });
        }
        return fileTypeValid && isLt2M;
    }

    uploadSuccess(response, file) {
        const images = this.state.comment.imageURLs;
        images.push(response.path);
        this.setState({
            comment: Object.assign({}, this.state.comment, {imageURLs: images}),
            uploading: false
        });
    }

    uploadAttachmentSuccess(response, file) {
        const attachments = this.state.comment.attachmentURLs;
        attachments.push(response.path);
        this.setState({
            comment: Object.assign({}, this.state.comment, {attachmentURLs: attachments}),
            uploading: false
        });
    }

    uploadFail(err, response, file) {
        this.setState({uploading: false});
        return err;
    }

    removeImage(index) {
        const images = this.state.comment.imageURLs;
        images.splice(index, 1);
        this.setState({comment: Object.assign({}, this.state.comment, {imageURLs: images})});
    }

    removeAttachment(e, index) {
        if (e) {
            e.preventDefault();
        }
        const attachments = this.state.comment.attachmentURLs;
        attachments.splice(index, 1);
        this.setState({comment: Object.assign({}, this.state.comment, {attachmentURLs: attachments})});
    }

    linkChange(val, index) {
        const links = this.state.comment.linkURLs;
        links[index] = val;
        this.setState({comment: Object.assign({}, this.state.comment, {linkURLs: links})});
    }

    removeLink(index) {
        const links = this.state.comment.linkURLs;
        links.splice(index, 1);
        this.setState({comment: Object.assign({}, this.state.comment, {linkURLs: links})});
    }

    addLink() {
        const links = this.state.comment.linkURLs;
        links[links.length] = null;
        this.setState({comment: Object.assign({}, this.state.comment, {linkURLs: links})});
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Form inline={true} model={this.state.title}>
                            <Form.Item>
                                <Select value={this.state.query.status} clearable={true}
                                    placeholder={i18n.t("comment.statusAll")}
                                    onChange={value => this.statusChange(value)}>
                                    {
                                        this.state.statusOptions.map(el => <Select.Option key={el.value}
                                            label={el.label} value={el.value}/>)
                                    }
                                </Select>
                            </Form.Item>
                            <Form.Item>
                                <Input icon="fa fa-search" value={this.state.query.title} onChange={value => this.queryChange("title", value)}/>
                            </Form.Item>
                            <Form.Item>
                                <Button onClick={() => this.find()}>{i18n.t("comment.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="primary" nativeType="button" onClick={() => this.openComment(null)}>
                            {i18n.t("comment.create")}
                        </Button>
                    </div>
                </div>
                <div className="body body--full">
                    <ElementUI.TreeTable treeData={this.state.data} keyColumn={this.state.keyColumn} treeColumns={this.state.columns}/>
                    <Dialog
                        title={i18n.t("comment.comment")}
                        visible={this.state.addComment}
                        onCancel={() => this.setState({addComment: false})}
                    >
                        <Dialog.Body>
                            <div className="page">
                                <div className="body">
                                    <Layout.Row>
                                        <Layout.Col span="18">
                                            <Form ref={(form) => {
                                                this.form = form;
                                            }} model={this.state.comment} labelWidth="150">
                                                <Form.Item label={i18n.t("comment.content")} prop="content">
                                                    <Input type="textarea" value={this.state.comment.content}
                                                        onChange={val => this.formChange("content", val)}/>
                                                </Form.Item>
                                            </Form>
                                        </Layout.Col>
                                        <Layout.Col span="6" style={{textAlign: "right"}}>
                                        </Layout.Col>
                                    </Layout.Row>
                                </div>
                            </div>
                        </Dialog.Body>
                        <Dialog.Footer>
                            <Button onClick={() => this.setState({addComment: false})}>{i18n.t("comment.cancel")}</Button>
                            <Button type="primary" onClick={() => this.addComment()}>{i18n.t("comment.confirm")}</Button>
                        </Dialog.Footer>
                    </Dialog>
                </div>
                <div className="footer">
                    <Pagination layout="total,sizes,prev,pager,next,jumper" total={this.state.data.total}
                        pageSizes={this.state.limitOptions} pageSize={this.state.query.limit}
                        currentPage={this.state.query.page}
                        onSizeChange={(limit) => {
                            this.queryChange("page", 1);
                            this.queryChange("limit", limit);
                            this.find();
                        }}
                        onCurrentChange={(page) => {
                            this.queryChange("page", page);
                            this.find();
                        }}/>
                </div>
            </div>
        );
    }
}

PageCommentList.propTypes = {
    match: PropTypes.object.match,
    pageId: PropTypes.object.string
};