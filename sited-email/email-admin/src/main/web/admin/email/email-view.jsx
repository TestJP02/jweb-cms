import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Form} from "element-react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class Email extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.match.params.id,
            email: {},
            form: {status: null},
            statusOptions: [{
                value: null,
                label: i18n.t("email.statusAll")
            }, {
                value: "PROCESSING",
                label: i18n.t("email.statusProcessing")
            }, {
                value: "COMPLETED",
                label: i18n.t("email.statusCompleted")
            }, {
                value: "CLOSED",
                label: i18n.t("email.statusClosed")
            }, {
                value: "CANCEL",
                label: i18n.t("email.statusCancel")
            }]
        };
    }

    componentWillMount() {
        fetch("/admin/api/email/" + this.state.id, {method: "GET"})
            .then((response) => {
                this.setState({email: response});
            });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to={{pathname: "/admin/"}}>{i18n.t("email.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to={{pathname: "/admin/email/list"}}>{i18n.t("email.emailList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{i18n.t("email.detail")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button nativeType="button" onClick={() => this.props.history.push("/admin/email/list")}>{i18n.t("email.back")}</Button>
                    </div>
                </div>
                <div className="body">
                    <Form labelPosition="left" labelWidth="150" model={this.state.form}>
                        <Form.Item label={i18n.t("email.id")} prop="id">
                            <span>{this.state.email.id}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.fromUser")} prop="fromUser">
                            <span>{this.state.email.from}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.to")} prop="to">
                            <span>{this.state.email.to}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.replyTo")} prop="replyTo">
                            <span>{this.state.email.replyTo}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.subject")} prop="subject">
                            <span>{this.state.email.subject}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.content")} prop="content">
                            <span>{this.state.email.content}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.status")} prop="status">
                            <span>{this.state.email.status}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.result")} prop="content">
                            <span>{this.state.email.result}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.errorMessage")} prop="content">
                            <span>{this.state.email.errorMessage}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.createdTime")} prop="createdTime">
                            <span>{this.state.email.createdTime}</span>
                        </Form.Item>
                        <Form.Item label={i18n.t("email.createdBy")} prop="createdBy">
                            <span>{this.state.email.createdBy}</span>
                        </Form.Item>
                    </Form>
                </div>
            </div>
        );
    }
}

Email.propTypes = {
    match: PropTypes.object.match,
    history: PropTypes.object.history
};