import React from "react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Form, Input, Message as notification, Select, Upload} from "element-react";

const i18n = window.i18n;
export default class CategoryUpdate extends React.Component {
    constructor(props) {
        super(props);
        const isCreateRoot = props.match.path === "/admin/page/category/create/root";
        this.state = {
            isCreateRoot: isCreateRoot,
            id: props.match.params.id,
            form: {
                parentId: props.match.params.parentId,
                tags: [],
                keywords: [],
                templatePath: "template/category.html",
                displayOrder: 0
            },
            formRules: {
                templatePath: [
                    {
                        required: true,
                        message: i18n.t("page.templatePathRequired"),
                        trigger: "blur"
                    }
                ],
                displayName: [
                    {
                        required: true,
                        message: i18n.t("page.nameRequired"),
                        trigger: "blur"
                    }
                ],
                path: [
                    {
                        required: true,
                        message: i18n.t("page.pathRequired"),
                        trigger: "blur"
                    },
                    {
                        validator: (rule, value, callback) => {
                            if (value[value.length - 1] !== "/") {
                                return callback(new Error(i18n.t("page.shouldEndWithSlash")));
                            }
                            if (!isCreateRoot && value === "/") {
                                return callback(new Error(i18n.t("page.shouldNotBeSlash")));
                            }
                            return callback();
                        },
                        trigger: "blur"
                    }
                ],
                displayOrder: [
                    {
                        required: true,
                        validator: (rule, value, callback) => {
                            if (value === null) {
                                return callback(new Error(i18n.t("page.displayOrderRequired")));
                            }
                            if (isNaN(Number(value)) || value % 1 !== 0) {
                                return callback(new Error(i18n.t("page.displayOrderInteger")));
                            }
                            return callback();
                        },
                        trigger: "blur"
                    }
                ]
            },
            tagInputVisible: false,
            tagInputValue: "",
            fileList: [],
            uploading: false,
            directoryPath: "/upload/page/",
            templateOptions: [],
            userOptions: [],
            userGroupOptions: [],
            rolesOptions: []
        };
    }

    componentWillMount() {
        if (this.state.id) {
            this.get(this.state.id);
        } else if (this.state.form.parentId) {
            fetch("/admin/api/page/category/" + this.state.form.parentId)
                .then((response) => {
                    const form = this.state.form;
                    this.setState({
                        parent: response,
                        form: form
                    });
                });
        }
        fetch("/admin/api/page/template", {method: "GET"})
            .then((response) => {
                this.setState({templateOptions: response});
            });
        fetch("/admin/api/user/find", {
            method: "PUT",
            body: JSON.stringify({
                status: "ACTIVE",
                page: 1,
                limit: 999999
            })
        }).then((response) => {
            this.setState({userOptions: response.items});
        });
        fetch("/admin/api/user/group/find", {
            method: "PUT",
            body: JSON.stringify({
                status: "ACTIVE",
                page: 1,
                limit: 999999
            })
        }).then((response) => {
            this.setState({userGroupOptions: response.items});
        });
    }

    get(id) {
        fetch("/admin/api/page/category/" + id).then((response) => {
            if (response.parentId) {
                fetch("/admin/api/page/category/" + response.parentId)
                    .then((r) => {
                        this.setState({
                            form: Object.assign({}, this.state.form, response),
                            parent: r
                        });
                    });
            } else {
                this.setState({form: Object.assign({}, this.state.form, response)});
            }
        });
    }

    onTagKeyUp(e) {
        if (e.keyCode === 13) {
            this.addTag();
        }
    }

    onTagChanged(value) {
        this.setState({tagInputValue: value});
    }

    onTagClosed(index) {
        this.state.form.tags.splice(index, 1);
    }

    showTagInput() {
        this.setState({tagInputVisible: true});
    }

    addTag() {
        const inputValue = this.state.tagInputValue;
        if (inputValue) {
            this.state.form.tags.push(inputValue);
        }
        this.setState({
            tagInputVisible: false,
            tagInputValue: ""
        });
    }

    uploading(event, file, fileList) {
        this.setState({uploading: true});
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

    uploadSuccess(response, file, fileList) {
        this.formChange("imageURL", response.path);
        this.setState({uploading: false});
    }

    uploadFail(err, response, file) {
        this.setState({uploading: false});
        return err;
    }

    formChange(key, value) {
        this.setState({form: Object.assign({}, this.state.form, {[key]: value})});
    }

    pathChange(value) {
        const path = value.startsWith("/") ? value : "/" + value;
        this.setState({form: Object.assign({}, this.state.form, {path: path})});
    }

    cancel() {
        this.props.history.push("/admin/page/category/list");
    }

    submit() {
        this.form.validate((valid) => {
            if (valid) {
                if (this.state.id) {
                    fetch("/admin/api/page/category/" + this.state.id, {
                        method: "PUT",
                        body: JSON.stringify(this.state.form)
                    }).then((response) => {
                        this.props.history.push("/admin/page/category/" + response.id + "/update");
                        notification({
                            title: "Success",
                            message: i18n.t("page.updateSuccessMessage"),
                            type: "success"
                        });
                    }).catch((err) => {
                        if (err.fields) {
                            notification({
                                title: i18n.t("page.errorTitle"),
                                type: "error",
                                message: err.fields[0].message
                            });
                        } else {
                            notification({
                                title: i18n.t("page.errorTitle"),
                                type: "error",
                                message: JSON.stringify(err)
                            });
                        }
                    });
                } else {
                    fetch("/admin/api/page/category", {
                        method: "POST",
                        body: JSON.stringify(this.state.form)
                    }).then((response) => {
                        notification({
                            title: "Success",
                            message: i18n.t("page.createSuccessMessage"),
                            type: "success"
                        });
                        this.props.history.push("/admin/page/category/list");
                    }).catch((err) => {
                        notification({
                            title: i18n.t("page.errorTitle"),
                            type: "error",
                            message: err.fields[0].message
                        });
                    });
                }
            } else {
                notification({
                    title: i18n.t("page.errorTitle"),
                    type: "error",
                    message: i18n.t("page.errorSubmit")
                });
                return false;
            }
        });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin/">{i18n.t("page.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/page/category/list">{i18n.t("page.categoryList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? i18n.t("page.updateCategory") : i18n.t("page.createCategory")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="primary" nativeType="button" onClick={() => this.submit()}>
                            {i18n.t("page.save")}
                        </Button>
                        <Button nativeType="button" onClick={() => this.cancel()}>
                            {i18n.t("page.cancel")}
                        </Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form ref={(form) => {
                            this.form = form;
                        }} model={this.state.form} labelWidth="150" rules={this.state.formRules}>
                            {this.state.form.parentId || this.state.isCreateRoot
                                ? <Form.Item label={i18n.t("page.path")} prop="path">
                                    <Input value={this.state.form.path} onChange={val => this.pathChange(val)}/>
                                </Form.Item>
                                : ""}
                            <Form.Item label={i18n.t("page.name")} prop="displayName">
                                <Input type="text" value={this.state.form.displayName}
                                    onChange={val => this.formChange("displayName", val)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("page.pageTemplate")} prop="templatePath" labelPosition="top">
                                <Select value={this.state.form.templatePath} onChange={val => this.formChange("templatePath", val)}
                                    placeholder={i18n.t("page.layoutSelect")} clearable={true}>
                                    {this.state.templateOptions.map(el => <Select.Option key={el.path} label={el.displayName + "(" + el.path + ")"}
                                        value={el.path}/>)}
                                </Select>
                            </Form.Item>
                            <Form.Item label={i18n.t("page.displayOrder")} prop="displayOrder">
                                <Input type="number" value={this.state.form.displayOrder}
                                    onChange={val => this.formChange("displayOrder", val)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("page.imageUrl")} prop="imageURL">
                                <Upload
                                    className="upload-demo"
                                    action={"/admin/api/file/upload?directoryPath=" + this.state.directoryPath}
                                    withCredentials={true}
                                    showFileList={false}
                                    fileList={this.state.fileList}
                                    beforeUpload={file => this.beforeAvatarUpload(file)}
                                    onSuccess={(response, file, fileList) => this.uploadSuccess(response, file, fileList)}
                                    onProgress={(event, file, fileList) => this.uploading(event, file, fileList)}
                                    onError={(err, response, file) => this.uploadFail(err, response, file)}
                                >
                                    {
                                        this.state.form.imageURL &&
                                        <div className="el-form-upload-preview">
                                            <img src={this.state.form.imageURL}/>
                                            <div className="el-form-upload-preview-delete-wrap"
                                                onClick={() => {
                                                    this.setState({form: Object.assign({}, this.state.form, {imageURL: null})});
                                                }}>
                                                <Button type="text" className="el-form-upload-preview-delete">
                                                    <i className="iconfont icon-icon_delete"/>
                                                </Button>
                                            </div>
                                        </div>
                                    }
                                    {this.state.form.imageURL ? ""
                                        : <Button className="el-form-upload-button" size="large">
                                            <i className="el-icon-plus"/>
                                        </Button>}
                                </Upload>
                            </Form.Item>
                            <Form.Item label={i18n.t("page.description")}>
                                <Input type="textarea" value={this.state.form.description}
                                    onChange={val => this.formChange("description", val)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("page.keywords")}>
                                <ElementUI.TagList list={this.state.form.keywords} onChange={val => this.formChange("keywords", val)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("page.tags")}>
                                <ElementUI.TagList list={this.state.form.tags} onChange={val => this.formChange("tags", val)}/>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

CategoryUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};