import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Form, Input, Notification as notification} from "element-react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class EmailTemplate extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.match.params.id,
            original: {},
            form: {
                name: null,
                subject: null,
                content: null
            },
            rules: {
                name: [
                    {
                        required: true,
                        message: i18n.t("email.nameRequired"),
                        trigger: "blur"
                    }
                ],
                subject: [
                    {
                        required: true,
                        message: i18n.t("email.subjectRequired"),
                        trigger: "blur"
                    }
                ],
                content: [
                    {
                        required: true,
                        message: i18n.t("email.contentRequired"),
                        trigger: "blur"
                    }
                ]
            }
        };
    }

    componentWillMount() {
        if (this.state.id) {
            fetch("/admin/api/email/template/" + this.state.id)
                .then((response) => {
                    this.setState({
                        form: response,
                        original: JSON.parse(JSON.stringify(response))
                    });
                });
        }
    }

    formChange(key, value) {
        this.setState(
            {form: Object.assign(this.state.form, {[key]: value})}
        );
    }

    save() {
        if (this.state.id) {
            fetch("/admin/api/email/template/" + this.state.id, {
                method: "PUT",
                body: JSON.stringify(this.state.form)
            }).then((response) => {
                this.setState({form: response});
                notification({
                    title: i18n.t("email.successTitle"),
                    type: "success",
                    message: i18n.t("email.saveSuccessMessage")
                });
                this.props.history.push("/admin/email/template/list");
            }).catch(() => {
                notification({
                    title: i18n.t("email.errorTitle"),
                    type: "error",
                    message: i18n.t("email.saveFailMessage")
                });
            });
        } else {
            fetch("/admin/api/email/template", {
                method: "POST",
                body: JSON.stringify(this.state.form)
            }).then((response) => {
                this.setState({form: response});
                notification({
                    title: i18n.t("email.successTitle"),
                    type: "success",
                    message: i18n.t("email.saveSuccessMessage")
                });
                this.props.history.push("/admin/email/template/list");
            }).catch(() => {
                notification({
                    title: i18n.t("email.errorTitle"),
                    type: "error",
                    message: i18n.t("email.saveFailMessage")
                });
            });
        }
    }

    reset(e) {
        e.preventDefault();
        this.setState({form: this.state.original});
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to={{pathname: "/admin/"}}>{i18n.t("email.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to={{pathname: "/admin/email/template/list"}}>{i18n.t("email.templateList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? i18n.t("email.updateTemplate") : i18n.t("email.createTemplate")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="primary" nativeType="button" onClick={() => this.save()}>{i18n.t("email.save")}</Button>
                        <Button nativeType="button" onClick={e => this.reset(e)}>{i18n.t("email.reset")}</Button>
                        <Button nativeType="button" onClick={() => this.props.history.push("/admin/email/template/list")}>{i18n.t("email.cancel")}</Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form labelPosition="left" labelWidth="150" model={this.state.form} rules={this.state.rules}>
                            {this.state.id && <Form.Item label={i18n.t("email.id")} prop="id"><span>{this.state.id}</span></Form.Item>}
                            <Form.Item label={i18n.t("email.name")} prop="name">
                                <Input placeholder={i18n.t("email.namePlaceHolder")} value={this.state.form.name} onChange={value => this.formChange("name", value)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("email.subject")} prop="subject">
                                <Input placeholder={i18n.t("email.subjectPlaceHolder")} value={this.state.form.subject} onChange={value => this.formChange("subject", value)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("email.content")} prop="content">
                                <Input type="textarea" placeholder={i18n.t("email.contentPlaceHolder")} value={this.state.form.content} onChange={value => this.formChange("content", value)}/>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

EmailTemplate.propTypes = {
    match: PropTypes.object.match,
    history: PropTypes.object.history
};