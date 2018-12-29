import React, {Component} from "react";
import {Link} from "react-router-dom";
import PropTypes from "prop-types";
import {Breadcrumb, Button, Card, Cascader, Dialog, Form, Input, Layout, Message as notification, Select, Switch, Upload} from "element-react";
import PageMD from "./markdown/post.markdown";

import "./post.update.css";

const i18n = window.i18n;
export default class PostUpdate extends Component {
    constructor(props) {
        super(props);
        this.state = {
            pathname: props.match.path,
            id: props.match.params.id,
            postId: props.match.params.postId,
            categoryCascade: [],
            post: {content: ""},
            directoryPath: "/upload/post/",
            templateOptions: [],
            postRules: {
                title: [{
                    required: true,
                    message: i18n.t("post.titleRequired"),
                    trigger: "blur"
                }],
                path: [
                    {
                        required: true,
                        message: i18n.t("post.pathRequired"),
                        trigger: "blur"
                    },
                    {
                        required: true,
                        validator: (rule, value, callback) => {
                            if (value[0] !== "/") {
                                callback(new Error(i18n.t("post.pathStartWithError")));
                                return;
                            }
                            if (value[value.length - 1] === "/") {
                                return callback(new Error(i18n.t("post.shouldNotEndWithSlash")));
                            }
                            fetch("/admin/api/post/path/validate", {
                                method: "PUT",
                                body: JSON.stringify({
                                    draftId: this.state.post.id,
                                    path: value
                                })
                            }).then((response) => {
                                if (response.valid) {
                                    return callback();
                                }
                                return callback(new Error(i18n.t("post.duplicatePath")));
                            });
                        },
                        trigger: "blur"
                    },
                    {
                        required: true,
                        validator: (rule, value, callback) => {
                            if (value[0] !== "/") {
                                callback(new Error(i18n.t("post.pathStartWithError")));
                                return;
                            }
                            callback();
                        },
                        trigger: "blur"
                    }
                ]
            },
            categoryList: [],
            suggestPathEnabled: false,
            suggestPathRequest: null
        };

        if (this.state.postId) {
            fetch("/admin/api/post/" + this.state.postId + "/draft", {method: "GET"})
                .then((response) => {
                    this.setState({post: response});
                    this.setCategory();
                });
        } else if (this.state.id) {
            fetch("/admin/api/post/draft/" + this.state.id, {method: "GET"})
                .then((response) => {
                    this.setState({post: response});
                    this.setCategory();
                });
        } else {
            fetch("/admin/api/post/draft", {method: "GET"})
                .then((response) => {
                    this.setState({
                        post: response,
                        suggestPathEnabled: true
                    });
                    this.setCategory();
                });
        }

        fetch("/admin/api/post/template", {method: "GET"}).then((response) => {
            this.setState({templateOptions: response});
        });
    }

    componentDidMount() {
        if (this.postTitleInput) {
            this.postTitleInput.focus();
        }
    }

    setCategory() {
        fetch("/admin/api/post/category/tree", {
            method: "PUT",
            body: JSON.stringify({})
        }).then((response) => {
            const cascade = response;
            this.trimCascade(cascade);
            const post = this.state.post;
            const categorySelected = [];
            if (post.categoryId) {
                this.traversal(cascade, post.categoryId, categorySelected);
            }
            this.setState({
                categoryList: cascade,
                categorySelected: categorySelected,
                post: post
            });
        });
    }

    initCategory() {
        const categoryList = this.state.categoryList;
        if (categoryList) {
            return;
        }
        fetch("/admin/api/post/category/tree", {
            method: "PUT",
            body: JSON.stringify({})
        }).then((response) => {
            this.trimCascade(response);
            this.setState({categoryList: response});
        });
    }

    trimCascade(cascade) {
        for (let i = 0; i < cascade.length; i += 1) {
            if (cascade[i].children.length === 0) {
                cascade[i].children = null;
            } else {
                this.trimCascade(cascade[i].children);
            }
        }
    }

    traversal(list, id, categorySelected) {
        for (let i = 0; i < list.length; i += 1) {
            const node = list[i];
            if (node.id === id) {
                categorySelected.push(id);
                return true;
            }
            if (node.children !== null && this.traversal(node.children, id, categorySelected)) {
                categorySelected.splice(0, 0, node.id);
                return true;
            }
        }
        return false;
    }

    submit() {
        const validForm = this.form;
        validForm.validate((valid) => {
            if (!valid) {
                return false;
            }
            fetch("/admin/api/post/draft", {
                method: "POST",
                body: JSON.stringify(this.state.post)
            }).then((response) => {
                this.setState({post: response});
                this.notifySuccess(i18n.t("post.saveSuccessMessage"));
                this.props.history.push("/admin/post/list");
            });
        });
    }

    cancel() {
        this.props.history.push("/admin/post/list");
    }

    selectCategory(value) {
        const post = this.state.post;
        post.categoryId = value[value.length - 1];
        this.setState({
            post: post,
            categorySelected: value
        });
    }

    getAvailableCategorySelected(categoryList, origin) {
        const selected = [];
        for (let i = 0; i < origin.length; i += 1) {
            const c = this.getCategory(categoryList, origin[i]);
            selected.push(c.id);
        }
        return selected;
    }

    getCategory(list, id) {
        for (let i = 0; i < list.length; i += 1) {
            if (list[i].id === id) {
                return list[i];
            }
            if (list[i].children) {
                const c = this.getCategory(list[i].children, id);
                if (c !== null) {
                    return c;
                }
            }
        }
        return null;
    }

    postChange(key, val) {
        const post = this.state.post;
        post[key] = val;
        this.setState({post});
    }

    postTitleChange(val) {
        const post = this.state.post;
        post.title = val;
        this.setState({post: post}, () => {
            if (this.state.suggestPathEnabled) {
                this.suggestPath(post.title);
            }
        });
    }

    postPathChange(value) {
        const path = value.startsWith("/") ? value : "/" + value;
        const post = this.state.post;
        post.path = path;
        this.setState({
            post: post,
            suggestPathEnabled: false
        });
    }

    suggestPath(title, callback) {
        if (this.state.suggestPathRequest && !callback) {
            const suggestPathRequest = {title: title};
            this.setState({suggestPathRequest});
            return;
        }

        const suggestPathRequest = {title: title};
        this.setState({suggestPathRequest});

        setTimeout(() => {
            fetch("/admin/api/post/path/suggest", {
                method: "PUT",
                body: JSON.stringify({title: title})
            }).then((response) => {
                if (this.state.suggestPathEnabled && this.state.suggestPathRequest.title === title) {
                    const post = this.state.post;
                    post.path = "/" + response.path;
                    this.setState({post});
                }

                if (this.state.suggestPathRequest.title === title) {
                    this.setState({suggestPathRequest: null});
                } else {
                    this.suggestPath(this.state.suggestPathRequest.title, true);
                }
            });
        }, 500);
    }

    postTagChange(value) {
        const post = this.state.post;
        post.tags = value;
        // post[index].tagSuggestEnabled = value === null || value.length === 0;
        this.setState({post});
    }

    suggestTag(title) {
        fetch("/admin/api/post/tag/suggest", {
            method: "PUT",
            body: JSON.stringify({title: title})
        }).then((response) => {
            const post = this.state.post;
            post.tags = response.tags;
            this.setState({post});
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
                title: i18n.t("post.errorTitle"),
                type: "info",
                message: i18n.t("post.imageTypeInvalid")
            });
            fileTypeValid = false;
        }
        if (!isLt2M) {
            notification({
                title: i18n.t("post.errorTitle"),
                type: "info",
                message: i18n.t("post.imageSizeInvalid")
            });
        }
        return fileTypeValid && isLt2M;
    }

    uploadSuccess(response, file) {
        const post = this.state.post;
        post.imageURL = response.path;
        this.setState({
            post: post,
            uploading: false
        });
    }

    uploadFail(err, response, file) {
        this.setState({uploading: false});
        return err;
    }

    notifySuccess(message) {
        notification({
            title: "Success",
            message: i18n.t("post.saveSuccessMessage"),
            type: "success"
        });
    }

    previewURL() {
        return "/admin/draft/" + this.state.post.id + "/preview";
    }

    addField() {
        const post = this.state.post;
        const number = Object.entries(post.fields).length + 1;
        post.fields["filed " + number] = "value " + number;
        this.setState({post});
    }

    removeField(key) {
        const post = this.state.post;
        delete post.fields[key];
        this.setState({post});
    }

    changeFieldKey(newKey, prevKey) {
        const post = this.state.post;
        const fields = {};
        for (const key in post.fields) {
            if (key === prevKey) {
                fields[newKey] = post.fields[prevKey];
            } else {
                fields[key] = post.fields[key];
            }
        }
        post.fields = fields;
        this.setState({post});
    }

    changeFieldValue(value, key) {
        const post = this.state.post;
        post.fields[key] = value;
        this.setState({post});
    }

    onContentChange(content) {
        const post = this.state.post;
        post.content = content;
        this.setState({post});
    }

    render() {
        return (
            <div className="page">

                <div className="toolbar" style={{overflow: "hidden"}}>
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin/">{i18n.t("post.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/post/list">{i18n.t("post.postList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.post.id ? i18n.t("post.createPage") : i18n.t("post.updatePage")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.post.id
                            ? <Button className="toolbar-button" type="primary" nativeType="button">
                                <a target="_blank" href={this.previewURL()}>
                                    {i18n.t("post.preview")}
                                </a>
                            </Button>
                            : ""}
                        <Button className="toolbar-button" type="primary" nativeType="button"
                            onClick={() => this.submit()}>{i18n.t("post.save")}</Button>
                        <Button className="toolbar-button" nativeType="button"
                            onClick={() => this.cancel()}>{i18n.t("post.cancel")}</Button>
                    </div>
                </div>
                <div className="body body--bordered">
                    <Dialog visible={this.state.deleteDraft} title="Delete Page" size="tiny"
                        onCancel={() => this.cancelDeleteDraft()}>
                        <Dialog.Body>
                            <span>{i18n.t("post.deleteLocaleHint")}</span>
                        </Dialog.Body>
                        <Dialog.Footer>
                            <span style={{float: "right"}}>
                                <Button className="toolbar-button" nativeType="button"
                                    onClick={() => this.cancelDeleteDraft()}>{i18n.t("post.cancel")}</Button>
                                <Button className="toolbar-button" type="primary" nativeType="button"
                                    onClick={() => this.confirmDeleteDraft()}>{i18n.t("post.delete")}</Button>
                            </span>
                        </Dialog.Footer>
                    </Dialog>
                    <Layout.Row className="el-form-group">
                        <Layout.Col span="16">
                            <Card>
                                <div className="page-title">
                                    <Input autoFocus={true} value={this.state.post.title}
                                        className="page-title__input"
                                        onChange={val => this.postTitleChange(val)}
                                        size="large"
                                        placeholder={i18n.t("post.titlePlaceholder")}/>
                                </div>


                                <div className="page-editor-wrapper">
                                    <PageMD content={this.state.post.content} onChange={content => this.onContentChange(content)}/>
                                </div>
                            </Card>
                        </Layout.Col>
                        <Layout.Col span="8">

                            <Form className="page-update-form" ref={(form) => {
                                this.form = form;
                            }} model={this.state.post} labelWidth="150" rules={this.state.postRules} labelPosition="top">
                                <Form.Item label={i18n.t("post.category")} prop="categoryId" labelPosition="top">
                                    <Cascader
                                        options={this.state.categoryList ? this.state.categoryList : []}
                                        value={this.state.categorySelected}
                                        changeOnSelect={false}
                                        onChange={value => this.selectCategory(value)}
                                        props={{
                                            value: "id",
                                            label: "displayName"
                                        }}
                                        showAllLevels={true}
                                        clearable={true}
                                    />
                                </Form.Item>
                                <Form.Item label={i18n.t("post.path")} prop="path">
                                    <Input value={this.state.post.path}
                                        onChange={val => this.postPathChange(val)}
                                        icon={this.state.suggestPathRequest ? "fa el-icon-loading" : ""}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("post.postTemplate")} prop="templatePath" labelPosition="top">
                                    <Select value={this.state.post.templatePath} onChange={val => this.postChange("templatePath", val)}
                                        placeholder={i18n.t("post.layoutSelect")} clearable={true}>
                                        {this.state.templateOptions.map(el => <Select.Option key={el.path} label={el.displayName + "(" + el.path + ")"}
                                            value={el.path}/>)}
                                    </Select>
                                </Form.Item>
                                <Form.Item label={i18n.t("post.topFixed")}>
                                    <Switch
                                        value={this.state.post.topFixed}
                                        onChange={val => this.postChange("topFixed", val)}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("post.description")} style={{position: "relative"}}>
                                    <span style={{
                                        float: "right",
                                        position: "absolute",
                                        right: "0",
                                        top: "-34px",
                                        fontSize: "12px",
                                        color: "#999999"
                                    }}>
                                        {i18n.t("post.current")}:{this.state.post.description && this.state.post.description.length ? this.state.post.description.length : 0}{i18n.t("post.words")}
                                    </span>
                                    <Input type="textarea" value={this.state.post.description} onChange={val => this.postChange("description", val)} autosize={{
                                        minRows: 3,
                                        maxRows: 5
                                    }}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("post.cover")} prop="imageURL">
                                    <Upload
                                        action={"/admin/api/file/upload?directoryPath=" + this.state.directoryPath}
                                        withCredentials={true} showFileList={false}
                                        onProgress={() => this.setState({uploading: true})}
                                        beforeUpload={file => this.beforeAvatarUpload(file)}
                                        onSuccess={(response, file) => this.uploadSuccess(response, file)}
                                        onError={(err, response, file) => this.uploadFail(err, response, file)}>
                                        {this.state.post.imageURL
                                            ? <div className="el-form-upload-preview">
                                                <img src={"/admin" + this.state.post.imageURL}/>
                                                <div className="el-form-upload-preview-delete-wrap"
                                                    onClick={() => {
                                                        const post = this.state.post;
                                                        post.imageURL = null;
                                                        this.setState({post});
                                                    }}>
                                                    <Button type="text" className="el-form-upload-preview-delete">
                                                        <i className="iconfont icon-icon_delete"/>
                                                    </Button>
                                                </div>
                                            </div>
                                            : <Button className="el-form-upload-button" size="large">
                                                <i className="el-icon-plus"/>
                                            </Button>}
                                    </Upload>
                                </Form.Item>
                                <Form.Item label={i18n.t("post.keywords")}>
                                    <ElementUI.TagList list={this.state.post.keywords} onChange={val => this.postChange("keywords", val)}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("post.tags")}>
                                    <ElementUI.TagList list={this.state.post.tags} onChange={val => this.postTagChange(val)}/>
                                </Form.Item>
                                {
                                    this.state.post.fields && <Form.Item label={i18n.t("post.fields")}>
                                        {Object.entries(this.state.post.fields).map((field, index) =>
                                            <Form.Item className="page-update__field" key={field.key}>
                                                <Input value={field[1]}
                                                    placeholder={i18n.t("post.fieldValue")}
                                                    prepend={<Input value={field[0]} placeholder={i18n.t("post.fieldName")} onChange={val => this.changeFieldKey(val, field[0])}/>}
                                                    onChange={val => this.changeFieldValue(val, field[0])}
                                                    append={<Button size="small" onClick={() => this.removeField(field[0])} icon="minus"></Button>}/>
                                            </Form.Item>
                                        )}
                                        <Button onClick={() => this.addField()} icon="plus"></Button>
                                    </Form.Item>
                                }
                            </Form>
                        </Layout.Col>
                    </Layout.Row>
                </div>
            </div>
        );
    }
}

PostUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};
