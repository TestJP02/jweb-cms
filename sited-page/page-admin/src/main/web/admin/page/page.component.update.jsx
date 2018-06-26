import React from "react";
import uuid from "react-native-uuid";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Form, Input, Message as notification} from "element-react";
import DefaultComponent from "./component.default";
import ErrorComponent from "./component.error";
import ComponentSelector from "./component-selector";

const i18n = window.i18n;
const bundle = window.app.bundle("pageBundle");

export default class ComponentUpdate extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            form: {
                name: null,
                displayName: null,
                attributes: {},
                componentName: null
            },
            component: {},
            componentOptions: [],
            formRules: {
                displayName: [
                    {
                        required: true,
                        message: i18n.t("page.displayNameRequired"),
                        trigger: "blur"
                    }
                ],
                name: [
                    {
                        required: true,
                        message: i18n.t("page.nameRequired"),
                        trigger: "blur"
                    }
                ],
                componentName: [
                    {
                        required: true,
                        message: i18n.t("page.componentNameRequired"),
                        trigger: "blur"
                    }
                ]
            },
            editing: false
        };
    }

    componentWillMount() {
        if (this.state.id) {
            fetch("/admin/api/page/saved-component/" + this.state.id, {method: "GET"}).then((saveComponent) => {
                fetch("/admin/api/page/component/raw", {method: "GET"}).then((response) => {
                    this.setState({componentOptions: response}, () => {
                        const component = this.component(saveComponent.componentName);
                        component.attributes = saveComponent.attributes;
                        this.setState({
                            form: saveComponent,
                            component: component
                        });
                    });
                });
            });
        } else {
            fetch("/admin/api/page/component/raw", {method: "GET"}).then((response) => {
                this.setState({componentOptions: response});
            });
        }

    }

    formChange(key, value) {
        this.setState({form: Object.assign({}, this.state.form, {[key]: value})});
    }

    createComponent(component) {
        const form = this.state.form;
        const newComponent = Object.assign({
            id: uuid.v4(),
            isNewCreated: true
        }, component);
        form.componentName = component.name;
        this.setState({
            component: newComponent,
            form
        });
    }

    component(componentName) {
        for (let i = 0; i < this.state.componentOptions.length; i += 1) {
            if (this.state.componentOptions[i].name === componentName) {
                return this.state.componentOptions[i];
            }
        }
        return {};
    }

    updateComponent(component) {
        component.isNewCreated = false;
        this.setState({
            component: component,
            editing: false
        });
    }

    editComponent() {
        if (!this.state.editing) {
            this.setState({editing: true});
        }
    }

    isComponentAvailable(component) {
        return bundle.component(component.name);
    }

    isComponentEditable(component) {
        if (!this.isComponentAvailable(component)) {
            return false;
        }
        return component.name !== "page-list" &&
            component.name !== "page-category-title" &&
            component.name !== "page-title";
    }

    renderComponent(component) {
        if (this.isComponentAvailable(component)) {
            return React.createElement(bundle.component(component.name), {
                component: component,
                mode: this.state.editing ? "edit" : "preview",
                onChange: value => this.updateComponent(value)
            });
        }
        if (component.isNewCreated) {
            return <DefaultComponent component={component}/>;
        }
        return <ErrorComponent component={component}/>;
    }

    cancel() {
        this.props.history.push("/admin/page/component/list");
    }

    submit() {
        if (this.state.component.id) {
            const form = this.state.form;
            form.attributes = this.state.component.attributes;
            form.componentName = this.state.component.name;
            form.containers = this.state.component.containers;
            form.icon = this.state.component.icon;
            if (this.state.id) {
                fetch("/admin/api/page/saved-component/" + this.state.id, {
                    method: "PUT",
                    body: JSON.stringify(form)
                }).then(() => {
                    notification({
                        title: "Success",
                        message: i18n.t("page.saveSuccessMessage"),
                        type: "success"
                    });
                });
            } else {
                fetch("/admin/api/page/saved-component", {
                    method: "POST",
                    body: JSON.stringify(form)
                }).then((response) => {
                    notification({
                        title: "Success",
                        message: i18n.t("page.saveSuccessMessage"),
                        type: "success"
                    });
                    this.props.history.push("/admin/page/component/list");
                });
            }
        } else {
            notification({
                title: i18n.t("page.errorTitle"),
                type: "info",
                message: i18n.t("page.componentRequired")
            });
        }

    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin/">{i18n.t("page.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/page/component/list">{i18n.t("page.componentList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? i18n.t("page.updateComponent") : i18n.t("page.createComponent")}</Breadcrumb.Item>
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
                        }} model={this.state.form} labelWidth="200" rules={this.state.formRules}>
                            <Form.Item label={i18n.t("page.componentName")} prop="name">
                                <Input value={this.state.form.name} onChange={value => this.formChange("name", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("page.displayName")} prop="displayName">
                                <Input value={this.state.form.displayName} onChange={value => this.formChange("displayName", value)}/>
                            </Form.Item>

                            <Form.Item label={i18n.t("page.component")} prop="componentName">
                                <ComponentSelector componentOptions={this.state.componentOptions}
                                    onSelect={component => this.createComponent(component)}
                                    disabled={this.state.id}
                                    value={this.state.form.componentName}/>
                            </Form.Item>
                        </Form>
                        <div className={this.state.editing ? "component-editor component-editor__selected" : "component-editor"}>
                            {this.state.component.id &&
                            <div key={"component-" + this.state.component.id}
                                className={(this.state.component.name === "page-html" ? "page-component--html " : "") + "page-component"}
                                onClick={() => this.editComponent()}>
                                {this.renderComponent(this.state.component)}
                            </div>
                            }
                        </div>
                    </Card>
                </div>

            </div>
        );
    }
}

ComponentUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};