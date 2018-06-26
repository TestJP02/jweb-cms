import React from "react";
import {Button, Input, Select} from "element-react";
import PropTypes from "prop-types";

import "./page.variable.field.css";

const i18n = window.i18n;
export default class VariableField extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            field: props.field,
            variableFieldTypeOptions: [{
                label: i18n.t("page.variableFieldLong"),
                value: "LONG"
            }, {
                label: i18n.t("page.variableFieldInteger"),
                value: "INTEGER"
            }, {
                label: i18n.t("page.variableFieldDouble"),
                value: "DOUBLE"
            }, {
                label: i18n.t("page.variableFieldString"),
                value: "STRING"
            }, {
                label: i18n.t("page.variableFieldBoolean"),
                value: "BOOLEAN"
            }, {
                label: i18n.t("page.variableFieldExpression"),
                value: "EXPRESSION"
            }]
        };
    }

    onChange(key, value) {
        const field = this.state.field;
        field[key] = value;
        this.setState({field});
        this.props.onChange(field);
    }

    onDelete(field) {
        this.props.onDelete(field);
    }

    render() {
        return (
            <div className="page-variable">
                <Input className="page-variable__name" value={this.state.field.name} placeholder={i18n.t("page.variableNamePlaceholder")} onChange={value => this.onChange("name", value)}/>
                <Select className="page-variable__type" onChange={type => this.onChange("type", type)} value={this.state.field.type}>
                    {
                        this.state.variableFieldTypeOptions.map(el => <Select.Option key={el.value} label={el.label} value={el.value}/>)
                    }
                </Select>
                <Input className="page-variable__value" value={this.state.field.value} placeholder={i18n.t("page.variableValuePlaceholder")} onChange={value => this.onChange("value", value)}/>
                <Button onClick={() => this.onDelete(this.state.field)}><i className="fa fa-close"></i></Button>
            </div>
        );
    }
}

VariableField.propTypes = {
    field: PropTypes.object,
    onChange: PropTypes.func,
    onDelete: PropTypes.func
};