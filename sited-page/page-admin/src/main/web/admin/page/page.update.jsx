import React, {Component} from "react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Cascader, Dialog, Form, Input, Layout, Message as notification, Select, Upload} from "element-react";
import PageMD from "./markdown/page.markdown";

import "./page.update.css";

const i18n = window.i18n;
export default class PageUpdate extends Component {
    constructor(props) {
        super(props);

        this.state = {
            pathname: props.match.path,
            id: props.match.params.id,
            pageId: props.match.params.pageId,
            categoryCascade: [],
            page: {},
            directoryPath: "/upload/page/",
            templateOptions: [],
            pageRules: {
                title: [{
                    required: true,
                    message: i18n.t("page.titleRequired"),
                    trigger: "blur"
                }],
                path: [
                    {
                        required: true,
                        message: i18n.t("page.pathRequired"),
                        trigger: "blur"
                    },
                    {
                        required: true,
                        validator: (rule, value, callback) => {
                            if (value[0] !== "/") {
                                callback(new Error(i18n.t("page.pathStartWithError")));
                                return;
                            }
                            if (value[value.length - 1] === "/") {
                                return callback(new Error(i18n.t("page.shouldNotEndWithSlash")));
                            }
                            fetch("/admin/api/page/path/validate", {
                                method: "PUT",
                                body: JSON.stringify({
                                    draftId: this.state.page.id,
                                    path: value
                                })
                            }).then((response) => {
                                if (response.valid) {
                                    return callback();
                                }
                                return callback(new Error(i18n.t("page.duplicatePath")));
                            });
                        },
                        trigger: "blur"
                    },
                    {
                        required: true,
                        validator: (rule, value, callback) => {
                            if (value[0] !== "/") {
                                callback(new Error(i18n.t("page.pathStartWithError")));
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

        if (this.state.pageId) {
            fetch("/admin/api/page/" + this.state.pageId + "/draft", {method: "GET"})
                .then((response) => {
                    this.setState({page: response});
                    this.setCategory();
                });
        } else if (this.state.id) {
            fetch("/admin/api/page/draft/" + this.state.id, {method: "GET"})
                .then((response) => {
                    this.setState({page: response});
                    this.setCategory();
                });
        } else {
            fetch("/admin/api/page/draft", {method: "GET"})
                .then((response) => {
                    this.setState({
                        page: response,
                        suggestPathEnabled: true
                    });
                    this.setCategory();
                });
        }

        fetch("/admin/api/page/template", {method: "GET"}).then((response) => {
            this.setState({templateOptions: response});
        });
    }

    componentDidMount() {
        if (this.pageTitleInput) {
            this.pageTitleInput.focus();
        }
    }

    setCategory() {
        fetch("/admin/api/page/category/tree", {
            method: "PUT",
            body: JSON.stringify({})
        }).then((response) => {
            const cascade = response;
            this.trimCascade(cascade);
            const page = this.state.page;
            const categorySelected = [];
            if (page.categoryId) {
                this.traversal(cascade, page.categoryId, categorySelected);
            }
            this.setState({
                categoryList: cascade,
                categorySelected: categorySelected,
                page: page
            });
        });
    }

    initCategory() {
        const categoryList = this.state.categoryList;
        if (categoryList) {
            return;
        }
        fetch("/admin/api/page/category/tree", {
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
            fetch("/admin/api/page/draft", {
                method: "POST",
                body: JSON.stringify(this.state.page)
            }).then((response) => {
                this.setState({page: response});
                this.notifySuccess(i18n.t("page.saveSuccessMessage"));
                this.props.history.push("/admin/page/list");
            });
        });
    }

    cancel() {
        this.props.history.push("/admin/page/list");
    }

    selectCategory(value) {
        const page = this.state.page;
        page.categoryId = value[value.length - 1];
        this.setState({
            page: page,
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

    pageChange(key, val) {
        const page = this.state.page;
        page[key] = val;
        this.setState({page});
    }

    pageTitleChange(val) {
        const page = this.state.page;
        page.title = val;
        this.setState({page: page}, () => {
            if (this.state.suggestPathEnabled) {
                this.suggestPath(page.title);
            }
        });
    }

    pagePathChange(value) {
        const path = value.startsWith("/") ? value : "/" + value;
        const page = this.state.page;
        page.path = path;
        this.setState({
            page: page,
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
            fetch("/admin/api/page/path/suggest", {
                method: "PUT",
                body: JSON.stringify({title: title})
            }).then((response) => {
                if (this.state.suggestPathEnabled && this.state.suggestPathRequest.title === title) {
                    const page = this.state.page;
                    page.path = "/" + response.path;
                    this.setState({page});
                }

                if (this.state.suggestPathRequest.title === title) {
                    this.setState({suggestPathRequest: null});
                } else {
                    this.suggestPath(this.state.suggestPathRequest.title, true);
                }
            });
        }, 500);
    }

    pageTagChange(value) {
        const page = this.state.page;
        page.tags = value;
        // pages[index].tagSuggestEnabled = value === null || value.length === 0;
        this.setState({page});
    }

    suggestTag(title) {
        fetch("/admin/api/page/tag/suggest", {
            method: "PUT",
            body: JSON.stringify({title: title})
        }).then((response) => {
            const page = this.state.page;
            page.tags = response.tags;
            this.setState({page});
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
                title: i18n.t("page.errorTitle"),
                type: "info",
                message: i18n.t("page.imageTypeInvalid")
            });
            fileTypeValid = false;
        }
        if (!isLt2M) {
            notification({
                title: i18n.t("page.errorTitle"),
                type: "info",
                message: i18n.t("page.imageSizeInvalid")
            });
        }
        return fileTypeValid && isLt2M;
    }

    uploadSuccess(response, file) {
        const page = this.state.page;
        page.imageURL = response.path;
        this.setState({
            page: page,
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
            message: i18n.t("page.saveSuccessMessage"),
            type: "success"
        });
    }

    previewURL() {
        return "/admin/draft/" + this.state.page.id + "/preview";
    }

    addField() {
        const page = this.state.page;
        const number = Object.entries(page.fields).length + 1;
        page.fields["filed " + number] = "value " + number;
        this.setState({page});
    }

    removeField(key) {
        const page = this.state.page;
        delete page.fields[key];
        this.setState({page});
    }

    changeFieldKey(newKey, prevKey) {
        const page = this.state.page;
        const fields = {};
        for (const key in page.fields) {
            if (key === prevKey) {
                fields[newKey] = page.fields[prevKey];
            } else {
                fields[key] = page.fields[key];
            }
        }
        page.fields = fields;
        this.setState({page});
    }

    changeFieldValue(value, key) {
        const page = this.state.page;
        page.fields[key] = value;
        this.setState({page});
    }

    onContentChange(content) {
        const page = this.state.page;
        page.content = content;
        this.setState({page});
    }

    render() {
        return (
            <div className="page">

                <div className="toolbar" style={{overflow: "hidden"}}>
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin/">{i18n.t("page.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/page/list">{i18n.t("page.pageList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.page.id ? i18n.t("page.createPage") : i18n.t("page.updatePage")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.page.id
                            ? <Button className="toolbar-button" type="primary" nativeType="button">
                                <a target="_blank" href={this.previewURL()}>
                                    {i18n.t("page.preview")}
                                </a>
                            </Button>
                            : ""}
                        <Button className="toolbar-button" type="primary" nativeType="button"
                            onClick={() => this.submit()}>{i18n.t("page.save")}</Button>
                        <Button className="toolbar-button" nativeType="button"
                            onClick={() => this.cancel()}>{i18n.t("page.cancel")}</Button>
                    </div>
                </div>
                <div className="body body--bordered">
                    <Dialog visible={this.state.deleteDraft} title="Delete Page" size="tiny"
                        onCancel={() => this.cancelDeleteDraft()}>
                        <Dialog.Body>
                            <span>{i18n.t("page.deleteLocaleHint")}</span>
                        </Dialog.Body>
                        <Dialog.Footer>
                            <span style={{float: "right"}}>
                                <Button className="toolbar-button" nativeType="button"
                                    onClick={() => this.cancelDeleteDraft()}>{i18n.t("page.cancel")}</Button>
                                <Button className="toolbar-button" type="primary" nativeType="button"
                                    onClick={() => this.confirmDeleteDraft()}>{i18n.t("page.delete")}</Button>
                            </span>
                        </Dialog.Footer>
                    </Dialog>
                    <Layout.Row className="el-form-group">
                        <Layout.Col span="16">
                            <Card>
                                <div className="page-title">
                                    <Input autoFocus={true} value={this.state.page.title}
                                        className="page-title__input"
                                        onChange={val => this.pageTitleChange(val)}
                                        size="large"
                                        placeholder={i18n.t("page.titlePlaceholder")}/>
                                </div>


                                <div className="page-editor-wrapper">
                                    <PageMD content={this.state.page.content} onChange={content => this.onContentChange(content)}/>
                                </div>
                            </Card>
                        </Layout.Col>
                        <Layout.Col span="8">

                            <Form className="page-update-form" ref={(form) => {
                                this.form = form;
                            }} model={this.state.page} labelWidth="150" rules={this.state.pageRules} labelPosition="top">
                                <Form.Item label={i18n.t("page.category")} prop="categoryId" labelPosition="top">
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
                                <Form.Item label={i18n.t("page.path")} prop="path">
                                    <Input value={this.state.page.path}
                                        onChange={val => this.pagePathChange(val)}
                                        icon={this.state.suggestPathRequest ? "fa el-icon-loading" : ""}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("page.pageTemplate")} prop="templatePath" labelPosition="top">
                                    <Select value={this.state.page.templatePath} onChange={val => this.pageChange("templatePath", val)}
                                        placeholder={i18n.t("page.layoutSelect")} clearable={true}>
                                        {this.state.templateOptions.map(el => <Select.Option key={el.path} label={el.displayName + "(" + el.path + ")"}
                                            value={el.path}/>)}
                                    </Select>
                                </Form.Item>
                                <Form.Item label={i18n.t("page.description")} style={{position: "relative"}}>
                                    <span style={{
                                        float: "right",
                                        position: "absolute",
                                        right: "0",
                                        top: "-34px",
                                        fontSize: "12px",
                                        color: "#999999"
                                    }}>
                                        {i18n.t("page.current")}:{this.state.page.description && this.state.page.description.length ? this.state.page.description.length : 0}{i18n.t("page.words")}
                                    </span>
                                    <Input type="textarea" value={this.state.page.description} onChange={val => this.pageChange("description", val)} autosize={{
                                        minRows: 3,
                                        maxRows: 5
                                    }}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("page.cover")} prop="imageURL">
                                    <Upload
                                        action={"/admin/api/file/upload?directoryPath=" + this.state.directoryPath}
                                        withCredentials={true} showFileList={false}
                                        onProgress={() => this.setState({uploading: true})}
                                        beforeUpload={file => this.beforeAvatarUpload(file)}
                                        onSuccess={(response, file) => this.uploadSuccess(response, file)}
                                        onError={(err, response, file) => this.uploadFail(err, response, file)}>
                                        {this.state.page.imageURL
                                            ? <div className="el-form-upload-preview">
                                                <img src={"/admin/file" + this.state.page.imageURL}/>
                                                <div className="el-form-upload-preview-delete-wrap"
                                                    onClick={() => {
                                                        const page = this.state.page;
                                                        page.imageURL = null;
                                                        this.setState({page});
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
                                <Form.Item label={i18n.t("page.keywords")}>
                                    <ElementUI.TagList list={this.state.page.keywords} onChange={val => this.pageChange("keywords", val)}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("page.tags")}>
                                    <ElementUI.TagList list={this.state.page.tags} onChange={val => this.pageTagChange(val)}/>
                                </Form.Item>
                                {
                                    this.state.page.fields && <Form.Item label={i18n.t("page.fields")}>
                                        {Object.entries(this.state.page.fields).map((field, index) =>
                                            <Form.Item className="page-update__field" key={field.key}>
                                                <Input value={field[1]}
                                                    placeholder={i18n.t("page.fieldValue")}
                                                    prepend={<Input value={field[0]} placeholder={i18n.t("page.fieldName")} onChange={val => this.changeFieldKey(val, field[0])}/>}
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

PageUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};
