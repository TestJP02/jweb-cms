import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Layout} from "element-react";

const i18n = window.i18n;
export default class PageFooterComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            component: props.component,
            onChange: props.onChange,
            mode: props.mode
        };
    }

    componentWillMount() {
        const component = this.state.component;
        if (!component.attributes.links) {
            component.attributes.links = [];
        }
        this.setState({component});
    }

    componentWillReceiveProps(nextProps) {
        const mode = nextProps.mode;
        this.setState({mode});
    }

    formChange(key, value) {
        const component = this.state.component;
        component.attributes = Object.assign({}, this.state.component.attributes, {[key]: value});
        this.setState({component: component});
    }

    save() {
        this.state.onChange(this.state.component);
    }

    addLink() {
        const component = this.state.component;
        component.attributes.links.push({});
        this.setState({component});
    }

    removeLink(index) {
        const component = this.state.component;
        component.attributes.links.splice(index, 1);
        this.setState({component});
    }

    changeLink(key, val, index) {
        const component = this.state.component;
        const link = component.attributes.links[index];
        link[key] = val;
        this.setState({component});
    }

    render() {

        return (
            <div className={(this.state.mode === "edit" ? "editing " : "") + "page-footer"}>
                {this.state.mode === "edit"
                    ? <Dialog
                        visible={true}
                        onCancel={() => this.save()}
                    >
                        <Dialog.Body>
                            <Form>
                                <Layout.Row>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.footerCopyrights")} labelWidth="120">
                                            <Input value={this.state.component.attributes.copyrights}
                                                onChange={val => this.formChange("copyrights", val)}></Input>
                                        </Form.Item>
                                    </Layout.Col>
                                </Layout.Row>
                                <Layout.Row>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.footerLinks")} labelWidth="120">
                                            {this.state.component.attributes.links.length === 0 &&
                                            <Button type="primary" icon="plus"
                                                onClick={() => this.addLink()}></Button>
                                            }
                                            {this.state.component.attributes.links.map((link, index) =>
                                                <Layout.Row key={index} gutter="20" className="page-footer__link">
                                                    <Layout.Col span="6" className="page-footer__link-name">
                                                        <Form.Item>
                                                            <Input placeholder={i18n.t("page.displayName")}
                                                                value={link.displayName}
                                                                onChange={val => this.changeLink("displayName", val, index)}></Input>
                                                        </Form.Item>
                                                    </Layout.Col>
                                                    <Layout.Col span="12">
                                                        <Form.Item>
                                                            <Input placeholder={i18n.t("page.link")}
                                                                value={link.link}
                                                                onChange={val => this.changeLink("link", val, index)}></Input>
                                                        </Form.Item>
                                                    </Layout.Col>
                                                    <Layout.Col span="3">
                                                        <Form.Item>
                                                            <Button type="danger" icon="minus"
                                                                onClick={() => this.removeLink(index)}></Button>
                                                        </Form.Item>
                                                    </Layout.Col>
                                                    {index === this.state.component.attributes.links.length - 1 &&
                                                    <Layout.Col span="3">
                                                        <Form.Item>
                                                            <Button type="primary" icon="plus"
                                                                onClick={() => this.addLink()}></Button>
                                                        </Form.Item>
                                                    </Layout.Col>
                                                    }
                                                </Layout.Row>
                                            )}
                                        </Form.Item>
                                    </Layout.Col>
                                </Layout.Row>
                            </Form>
                        </Dialog.Body>

                        <Dialog.Footer className="dialog-footer">
                            <Button type="primary" onClick={() => this.save()}>{i18n.t("page.save")}</Button>
                        </Dialog.Footer>
                    </Dialog>
                    : <div className="page-footer__preview">
                        <div className="page-footer-preview__links">
                            {this.state.component.attributes.links.length === 0 &&
                            <span className="page-footer-preview__link">{i18n.t("page.footerLinks")}</span>
                            }
                            {this.state.component.attributes.links.map((link, index) =>
                                <a key={index}
                                    href={link.link} className="page-footer-preview__link">
                                    {link.displayName}
                                </a>
                            )}
                        </div>
                        <div className="page-footer-preview__copyright">
                            {this.state.component.attributes.copyrights || i18n.t("page.footerCopyrights")}
                        </div>
                    </div>
                }
            </div>
        );
    }
}

PageFooterComponent.defaultProps = {mode: "preview"};

PageFooterComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};