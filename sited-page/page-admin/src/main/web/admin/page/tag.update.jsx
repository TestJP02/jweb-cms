import React from "react";
import {Breadcrumb, Button, Card, Form, Input} from "element-react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";

const i18n = window.i18n;
export default class TagUpdate extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id: this.props.match.params.id,
            form: {
                name: null,
                path: []
            },
            rules: {
                name: [{
                    required: true,
                    message: i18n.t("page.nameRequired"),
                    trigger: "blur"
                }]
            }
        };
    }

    componentWillMount() {
        if (this.state.id) {
            fetch("/admin/api/page/tag/" + this.state.id)
                .then((response) => {
                    this.setState({form: response});
                });
        }
    }

    onChange(key, value) {
        this.setState(
            {form: Object.assign(this.state.form, {[key]: value})}
        );
    }

    save() {
        this.formRef.validate((valid) => {
            if (valid) {
                fetch("/admin/api/page/tag", {
                    method: "post",
                    body: JSON.stringify(this.state.form)
                }).then(() => {
                    this.props.history.push("/admin/page/tag/list");
                });
            } else {
                return false;
            }
        });
    }

    update() {
        this.formRef.validate((valid) => {
            if (valid) {
                fetch("/admin/api/page/tag/" + this.state.id, {
                    method: "put",
                    body: JSON.stringify(this.state.form)
                }).then(() => {
                    this.props.history.push("/admin/page/tag/list");
                });
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
                            <Breadcrumb.Item><Link to="/admin">{i18n.t("page.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/page/tag/list">{i18n.t("page.tags")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? i18n.t("page.updateTag") : i18n.t("page.createTag")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.id ? <span>
                            <Button type="primary" onClick={() => this.update()}>{i18n.t("page.save")}</Button>
                        </span> : <Button type="primary" onClick={() => this.save()}>{i18n.t("page.save")}</Button>}
                        <Button type="button"><Link to="/admin/page/tag/list">{i18n.t("page.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form model={this.state.form} rules={this.state.rules} ref={(c) => {
                            this.formRef = c;
                        }} labelWidth="150">
                            <Form.Item label={i18n.t("page.tagName")} prop="name">
                                <Input value={this.state.form.name} onChange={value => this.onChange("name", value)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("page.tagPath")} prop="path">
                                <Input value={this.state.form.path} onChange={value => this.onChange("path", value)}/>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

TagUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};