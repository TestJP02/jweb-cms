import React from "react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Cascader, Form, Input, Layout, Message as notification} from "element-react";
import LayoutGridSection from "./template.grid";

const i18n = window.i18n;
export default class TemplateUpdate extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            layoutOptions: [],
            categoryCascade: [],
            categoryList: [],
            path: null,
            suggestPathEnabled: false,
            suggestPathRequest: null,
            form: {
                id: props.match.params.id,
                title: null,
                path: null,
                sections: null
            },
            formChanged: false,
            formRules: {
                title: [
                    {
                        required: true,
                        message: i18n.t("page.displayNameRequired"),
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
                        required: true,
                        validator: (rule, value, callback) => {
                            if (value[0] !== "/") {
                                callback(new Error(i18n.t("page.pathStartWithError")));
                                return;
                            }
                            fetch("/admin/api/page/path/validate", {
                                method: "PUT",
                                body: JSON.stringify({
                                    draftId: this.state.form.id,
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
            }
        };
    }

    componentWillMount() {
        const form = this.state.form;
        if (this.state.form.id) {
            this.get();
        } else if (!form.sections) {
            form.sections = [];
            this.setState({
                form: form,
                suggestPathEnabled: true
            });
            this.setCategory();
        }
        setTimeout(() => this.autoSave(), 10000);
    }

    pageTitleChange(val) {
        const form = this.state.form;
        form.title = val;
        this.setState({
            form: form,
            formChanged: true
        }, () => {
            if (this.state.suggestPathEnabled) {
                this.suggestPath(form.title);
            }
        });
    }

    pagePathChange(value) {
        const path = value.startsWith("/") ? value : "/" + value;
        const form = this.state.form;
        form.path = path;
        this.setState({
            formChanged: true,
            form: form,
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
                    const form = this.state.form;
                    form.path = "/" + response.path;
                    this.setState({
                        form: form,
                        formChanged: true
                    });
                }

                if (this.state.suggestPathRequest.title === title) {
                    this.setState({suggestPathRequest: null});
                } else {
                    this.suggestPath(this.state.suggestPathRequest.title, true);
                }
            });
        }, 500);
    }

    autoSave() {
        this.savePage((response) => {
            const form = this.state.form;
            if (!form.id) {
                form.id = response.id;
                this.setState({form});
            }
            notification({
                title: "Success",
                message: i18n.t("page.autoSaved"),
                type: "success"
            });
        });
        setTimeout(() => this.autoSave(), 30000);
    }

    savePage(callback, failedCallback) {
        if (this.form) {
            const form = this.state.form;
            this.form.validate((valid) => {
                if (valid) {
                    if (form.id) {
                        fetch("/admin/api/page/" + form.id, {
                            method: "PUT",
                            body: JSON.stringify(form)
                        }).then((response) => {
                            if (callback) {
                                return callback(response);
                            }
                        });
                    } else {
                        fetch("/admin/api/page", {
                            method: "POST",
                            body: JSON.stringify(form)
                        }).then((response) => {
                            if (callback) {
                                return callback(response);
                            }
                        });
                    }
                } else if (failedCallback) {
                    failedCallback();
                }
            });
        }
    }

    get() {
        fetch("/admin/api/page/" + this.state.form.id, {method: "GET"})
            .then((response) => {
                window.console.log(response);

                this.setState({form: response}, () => {
                    this.setCategory();
                });
            });
    }

    setCategory() {
        fetch("/admin/api/page/category/tree", {
            method: "PUT",
            body: JSON.stringify({})
        }).then((response) => {
            const cascade = response;
            this.trimCascade(cascade);
            const form = this.state.form;
            const categorySelected = [];
            if (form.categoryId) {
                this.traversal(cascade, form.categoryId, categorySelected);
            }
            this.setState({
                categoryList: cascade,
                categorySelected: categorySelected,
                form: form
            });
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

    selectCategory(value) {
        const form = this.state.form;
        form.categoryId = value[value.length - 1];
        this.setState({
            form: form,
            categorySelected: value
        });
    }

    cancel() {
        this.props.history.push("/admin/page/list");
    }

    formChange(key, value) {
        this.setState({
            form: Object.assign({}, this.state.form, {
                [key]: value,
                formChanged: true
            })
        });
    }

    layout() {
        return {
            "gridColumns": 24,
            "screenWidths": ["xs", "sm", "md", "lg"]
        };
    }

    preview() {
        const newTab = window.open("about:blank", "_blank");
        this.savePage((response) => {
            newTab.location.href = response.path + "?draft=true";
        }, () => {
            newTab.close();
        });
    }

    publish() {
        this.savePage((response) => {
            fetch("/admin/api/page/" + response.id + "/publish", {
                method: "POST",
                body: JSON.stringify({})
            }).then(() => {
                notification({
                    title: "Success",
                    message: i18n.t("page.publishSuccessMessage"),
                    type: "success"
                });
                this.props.history.push("/admin/page/list");
            });
        });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin/">{i18n.t("page.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link
                                to="/admin/page/list">{i18n.t("page.pageList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.form.id ? i18n.t("page.updateLayout") : i18n.t("page.createLayout")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button className="toolbar-button" type="primary" nativeType="button"
                            onClick={() => this.preview()}>{i18n.t("page.preview")}</Button>
                        <Button className="toolbar-button" type="primary" nativeType="button"
                            onClick={() => this.publish()}>{i18n.t("page.publish")}</Button>
                        <Button className="toolbar-button" nativeType="button"
                            onClick={() => this.cancel()}>{i18n.t("page.cancel")}</Button>
                    </div>
                </div>
                <div className="body">
                    <Layout.Row className="el-form-group" gutter="30">
                        <Layout.Col span="18">
                            {this.state.form.sections &&
                            <LayoutGridSection sections={this.state.form.sections}
                                onChange={(sections) => {
                                    const form = this.state.form;
                                    form.sections = sections;
                                    this.setState({form});
                                }}/>
                            }
                        </Layout.Col>
                        <Layout.Col span="6">
                            <Form className="page-update-form" ref={(form) => {
                                this.form = form;
                            }} model={this.state.form} labelPosition="top" rules={this.state.formRules}>
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
                                <Form.Item label={i18n.t("page.title")} prop="title">
                                    <Input value={this.state.form.title} onChange={val => this.pageTitleChange(val)}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("page.path")} prop="path">
                                    <Input value={this.state.form.path} onChange={val => this.pagePathChange(val)}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("page.description")} prop="description">
                                    <Input type="textarea" value={this.state.form.description} onChange={val => this.formChange("description", val)} autosize={{
                                        minRows: 3,
                                        maxRows: 5
                                    }}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("page.tags")} prop="tags">
                                    <ElementUI.TagList list={this.state.form.tags} onChange={val => this.formChange("tags", val)}/>
                                </Form.Item>
                            </Form>
                        </Layout.Col>
                    </Layout.Row>
                </div>
            </div>);
    }
}
TemplateUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};