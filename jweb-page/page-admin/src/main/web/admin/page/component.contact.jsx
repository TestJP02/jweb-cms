import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Layout} from "element-react";
import FileBrowser from "./page.file.browser";

const i18n = window.i18n;
export default class PageContactComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            component: props.component,
            onChange: props.onChange,
            mode: props.mode,
            isChangingFile: false
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


    files() {
        const files = [];
        if (this.state.component.attributes.imageURL) {
            files.push({path: this.state.component.attributes.imageURL});
        }
        return files;
    }

    changeImage(files) {
        if (files && files.length > 0) {
            const component = this.state.component;
            component.attributes.imageURL = files[0].path;
            this.setState({
                isChangingFile: false,
                component
            });
        }
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
                                        <Form.Item label={i18n.t("page.map")} >
                                            {!this.state.component.attributes.imageURL &&
                                                <div className="page-contact__image-add">
                                                    <Button type="text" size="small"
                                                        className="page-contact__image-add-btn"
                                                        onClick={() => this.setState({isChangingFile: true})}>+</Button>
                                                </div>
                                            }
                                            {this.state.component.attributes.imageURL &&
                                                <div className="page-contact__map-preview"
                                                    onClick={() => this.setState({isChangingFile: true})}>
                                                    <img src={this.state.component.attributes.imageURL} alt="" />
                                                </div>
                                            }
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.address")} props="address">
                                            <Input type="text" value={this.state.component.attributes.address} onChange={val => this.formChange("address", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.phone")} props="phone">
                                            <Input type="text" value={this.state.component.attributes.phone} onChange={val => this.formChange("phone", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.email")} props="email">
                                            <Input type="number" value={this.state.component.attributes.email} onChange={val => this.formChange("email", val)} />
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
                {this.state.isChangingFile &&
                    <FileBrowser files={this.files()}
                        multiple={false}
                        visible={true}
                        onChange={files => this.changeImage(files)} />
                }
                <div className="page-contact-preview">
                    <Layout.Row gutter="60">
                        <Layout.Col span="8">
                            <div>&nbsp;</div>
                            {this.state.component.attributes.imageURL &&
                                <img src={this.state.component.attributes.imageURL} alt=""
                                    style={{width: "100%"}} />
                            }
                        </Layout.Col>
                        <Layout.Col span="16">
                            <h3>{i18n.t("page.information")}</h3>
                            <h4>{i18n.t("page.address")}</h4>
                            <p>{this.state.component.attributes.address ? this.state.component.attributes.address : i18n.t("page.empty")}</p>
                            <h4>{i18n.t("page.phone")}</h4>
                            <p>{this.state.component.attributes.phone ? this.state.component.attributes.phone : i18n.t("page.empty")}</p>
                            <h4>{i18n.t("page.email")}</h4>
                            <p>{this.state.component.attributes.email ? this.state.component.attributes.email : i18n.t("page.empty")}</p>
                        </Layout.Col>
                    </Layout.Row>
                </div>
            </div>
        );
    }
}

PageContactComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};