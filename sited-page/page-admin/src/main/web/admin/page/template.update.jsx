import React from "react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Form, Input, Layout, Message as notification} from "element-react";
import LayoutGridSection from "./template.grid";

const i18n = window.i18n;
export default class TemplateUpdate extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            layoutOptions: [],
            path: null,
            form: {
                id: props.match.params.id,
                displayName: null,
                path: null,
                sections: null
            },
            formRules: {
                displayName: [
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
            this.setState({form});
        }
    }

    get() {
        fetch("/admin/api/page/template/" + this.state.form.id, {method: "GET"})
            .then((response) => {
                const path = response.path.substring(9).substring(0, response.path.length - 5);
                this.setState({
                    path: path,
                    form: response
                });
            });
    }

    cancel() {
        this.props.history.push("/admin/page/template/list");
    }

    pathChange(path) {
        const processedPath = path;
        this.setState({
            form: Object.assign({}, this.state.form, {path: "template/" + processedPath + ".html"}),
            path: processedPath
        });
    }

    formChange(key, value) {
        this.setState({form: Object.assign({}, this.state.form, {[key]: value})});
    }

    layout() {
        return {
            "gridColumns": 24,
            "screenWidths": ["xs", "sm", "md", "lg"]
        };
    }

    submit() {
        const form = this.state.form;
        form.type = "CUSTOMIZED";

        this.form.validate((valid) => {
            if (valid) {
                if (form.id) {
                    fetch("/admin/api/page/template/" + form.id, {
                        method: "PUT",
                        body: JSON.stringify(form)
                    }).then((response) => {
                        notification({
                            title: "Success",
                            message: i18n.t("page.updateSuccessMessage"),
                            type: "success"
                        });
                        this.props.history.push("/admin/page/template/list");
                    });
                } else {
                    fetch("/admin/api/page/template", {
                        method: "POST",
                        body: JSON.stringify(form)
                    }).then((response) => {
                        notification({
                            title: "Success",
                            message: i18n.t("page.createSuccessMessage"),
                            type: "success"
                        });
                        this.props.history.push("/admin/page/template/list");
                    });
                }
            } else {
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
                            <Breadcrumb.Item><Link
                                to="/admin/page/template/list">{i18n.t("page.templates")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.form.id ? i18n.t("page.updateLayout") : i18n.t("page.createLayout")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button className="toolbar-button" type="primary" nativeType="button"
                            onClick={() => this.submit()}>{i18n.t("page.save")}</Button>
                        <Button className="toolbar-button" nativeType="button"
                            onClick={() => this.cancel()}>{i18n.t("page.cancel")}</Button>
                    </div>
                </div>
                <div className="body">
                    <Layout.Row className="el-form-group" gutter="30">
                        <Layout.Col span="16">
                            {this.state.form.sections &&
                            <Card>
                                <LayoutGridSection sections={this.state.form.sections}
                                    onChange={(sections) => {
                                        const form = this.state.form;
                                        form.sections = sections;
                                        this.setState({form});
                                    }}/>
                            </Card>
                            }
                        </Layout.Col>
                        <Layout.Col span="8">
                            <Form className="page-update-form" ref={(form) => {
                                this.form = form;
                            }} model={this.state.form} labelPosition="top" rules={this.state.formRules}>
                                <Layout.Row className="el-form-group" gutter="80">
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.displayName")} prop="displayName">
                                            <Input value={this.state.form.displayName} onChange={val => this.formChange("displayName", val)}/>
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.path")} prop="path">
                                            {
                                                this.state.form.id ? <Input value={this.state.path} prepend="template/" append=".html" onChange={val => this.pathChange(val)} disabled/>
                                                    : <Input value={this.state.path} prepend="template/" append=".html" onChange={val => this.pathChange(val)}/>
                                            }
                                        </Form.Item>
                                    </Layout.Col>
                                </Layout.Row>
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