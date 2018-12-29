import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Layout} from "element-react";

const i18n = window.i18n;
export default class PageTextComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            component: props.component,
            onChange: props.onChange,
            mode: props.mode,
            editingIndex: null
        };
    }

    componentWillReceiveProps(nextProps) {
        const mode = nextProps.mode;
        this.setState({mode});
    }

    formChange(key, val) {
        const component = this.state.component;
        component.attributes[key] = val;
        this.setState({component});
    }

    saveComponent() {
        const component = this.state.component;
        this.state.onChange(component);
    }

    render() {
        return (
            <div className="page-default-component">
                {this.state.mode === "edit" &&
                    <Dialog
                        visible={true}
                        onCancel={() => this.saveComponent()}
                    >
                        <Dialog.Body>
                            <Form ref={(form) => {
                                this.form = form;
                            }} model={this.state.component.attributes} labelWidth="120" rules={this.state.formRules}>
                                <Layout.Row>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.name")} props="name">
                                            <Input type="text" value={this.state.component.attributes.name} onChange={val => this.formChange("name", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.link")} props="name">
                                            <Input type="text" value={this.state.component.attributes.link} onChange={val => this.formChange("link", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.content")} props="name">
                                            <Input type="textarea" value={this.state.component.attributes.content} onChange={val => this.formChange("content", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                </Layout.Row>
                            </Form>
                        </Dialog.Body>

                        <Dialog.Footer className="dialog-footer">
                            <Button type="primary" onClick={() => this.saveComponent()}>{i18n.t("page.save")}</Button>
                        </Dialog.Footer>
                    </Dialog>
                }
                <div className="page-default-component__name">{this.state.component.attributes.name}</div>
                <div className="page-default-component__content">
                    <div className="page-default-component__text">
                        {this.state.component.attributes.content}
                    </div>
                </div>
            </div>
        );
    }
}

PageTextComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};