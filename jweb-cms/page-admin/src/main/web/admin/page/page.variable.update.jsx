import React from "react";
import {Breadcrumb, Button, Card, Form, Input} from "element-react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import VariableField from "./page.variable.field";
import uuid from "react-native-uuid";

const i18n = window.i18n;
export default class VariableUpdate extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id: this.props.match.params.id,
            variable: {
                name: null,
                fields: [],
                status: null
            },
            statusOptions: [{
                label: i18n.t("page.statusActive"),
                value: "ACTIVE"
            }, {
                label: i18n.t("page.statusInactive"),
                value: "INACTIVE"
            }],
            rules: {
                name: [{
                    required: true,
                    message: i18n.t("page.nameRequired"),
                    trigger: "blur"
                }],
                status: [{
                    required: true,
                    message: i18n.t("page.statusRequired"),
                    trigger: "blur"
                }]
            }
        };
    }

    componentWillMount() {
        if (typeof this.state.id !== "undefined") {
            fetch("/admin/api/page/variable/" + this.state.id)
                .then((response) => {
                    const variable = response;
                    variable.fields.forEach((field) => {
                        field.id = uuid.v4();
                    });
                    this.setState({variable});
                });
        }
    }

    onChange(key, value) {
        this.setState(
            {variable: Object.assign(this.state.variable, {[key]: value})}
        );
    }

    addField() {
        const variable = this.state.variable;
        variable.fields.push({
            id: uuid.v4(),
            name: "",
            type: "STRING",
            value: ""
        });
        this.setState({variable: variable});
    }

    setField(field) {
        const variable = this.state.variable;
        for (let i = 0; i < variable.fields.length; i += 1) {
            if (variable.fields[i].id === field.id) {
                variable.fields[i] = field;
            }
        }
        this.setState({variable});
    }

    deleteField(field) {
        window.console.log(field);
        const variable = this.state.variable;
        for (let i = 0; i < variable.fields.length; i += 1) {
            if (variable.fields[i].id === field.id) {
                variable.fields.splice(i, 1);
            }
        }

        window.console.log(variable);
        this.setState({variable});
    }

    save() {
        this.variableForm.validate((valid) => {
            if (valid) {
                fetch("/admin/api/page/variable", {
                    method: "post",
                    body: JSON.stringify(this.state.variable)
                }).then(() => {
                    this.props.history.push("/admin/page/variable/list");
                });
            } else {
                return false;
            }
        });
    }

    update() {
        this.variableForm.validate((valid) => {
            if (valid) {
                fetch("/admin/api/page/variable/" + this.state.id, {
                    method: "put",
                    body: JSON.stringify(this.state.variable)
                }).then(() => {
                    this.props.history.push("/admin/page/variable/list");
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
                            <Breadcrumb.Item><Link to="/admin/page/variable/list">{i18n.t("page.variables")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? i18n.t("page.updateVariable") : i18n.t("page.createVariable")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.id ? <span>
                            <Button type="primary" onClick={() => this.update()}>{i18n.t("page.save")}</Button>
                        </span> : <Button type="primary" onClick={() => this.save()}>{i18n.t("page.save")}</Button>}
                        <Button type="button"><Link to="/admin/page/variable/list">{i18n.t("page.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form model={this.state.variable} rules={this.state.rules} ref={(c) => {
                            this.variableForm = c;
                        }} labelWidth="150">
                            <Form.Item label={i18n.t("page.name")} prop="name">
                                <Input value={this.state.variable.name} onChange={value => this.onChange("name", value)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("page.fields")}>
                                {
                                    this.state.variable.fields.map(
                                        (field, index) =>
                                            <div key={"field-" + field.id}>
                                                <VariableField field={field} onChange={data => this.setField(data)} onDelete={data => this.deleteField(data)}/>
                                            </div>
                                    )
                                }
                                <Button type="primary" onClick={() => this.addField()}>Add</Button>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

VariableUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};