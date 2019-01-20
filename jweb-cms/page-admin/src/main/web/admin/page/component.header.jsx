import React from "react";
import PropTypes from "prop-types";
import {Button, Checkbox, Form, Input, Layout} from "element-react";
import FileBrowser from "./page.file.browser";

import "./component.header.css";

const i18n = window.i18n;
export default class PageHeaderComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            component: props.component,
            onChange: props.onChange,
            mode: props.mode,
            isChangingFile: false
        };
    }

    componentWillMount() {
        const component = this.state.component;
        if (!component.attributes.userEnabled) {
            component.attributes.userEnabled = false;
        }
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
        component.attributes[key] = value;
        this.setState({component});
    }

    toggleUserEnabled() {
        const component = this.state.component;
        component.attributes.userEnabled = !component.attributes.userEnabled;

        window.console.log(component);
        this.setState({component});
    }

    toggleSearchEnabled() {
        const component = this.state.component;
        component.attributes.searchEnabled = !component.attributes.searchEnabled;
        this.setState({component});
    }

    save(e) {
        this.state.onChange(this.state.component);
        e.preventDefault();
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

    toggleLinkGroup(index) {
        const component = this.state.component;
        const link = component.attributes.links[index];
        if (link.children) {
            link.children = null;
        } else {
            link.children = [{}];
        }
        this.setState({component});
    }

    renderLink(link, index) {
        const moveUp = () => {
            if (index === 0) {
                return;
            }
            move(index, index - 1);
        };
        const moveDown = () => {
            if (index === this.state.component.attributes.links.length - 1) {
                return;
            }
            move(index, index + 1);
        };

        const move = (from, to) => {
            const component = this.state.component;
            const links = component.attributes.links;
            const current = links.splice(from, 1)[0];
            links.splice(to, 0, current);
            this.setState({component});
        };
        return (
            <Layout.Col span="24">
                <Form.Item label={index === 0 ? i18n.t("page.headerLinks") : "  "} labelWidth="120">
                    <Layout.Row key={index} gutter="10" className="page-header__link">
                        <Layout.Col span="5" className="page-header__link-name">
                            <Form.Item>
                                <Input placeholder={i18n.t("page.displayName")}
                                    value={link.displayName}
                                    onChange={val => this.changeLink("displayName", val, index)}></Input>
                            </Form.Item>
                        </Layout.Col>
                        <Layout.Col span="8">
                            <Form.Item>
                                <Input placeholder={i18n.t("page.link")}
                                    value={link.link}
                                    disabled={link.children}
                                    onChange={val => this.changeLink("link", val, index)}></Input>
                            </Form.Item>
                        </Layout.Col>
                        <Layout.Col span="3">
                            <Form.Item>
                                <Button type={link.children ? "success" : "cancel"}
                                    onClick={() => this.toggleLinkGroup(index)}>{i18n.t("page.subMenu")}</Button>
                            </Form.Item>
                        </Layout.Col>
                        <Layout.Col span="2">
                            <Form.Item>
                                <Button type="danger" icon="minus"
                                    onClick={() => this.removeLink(index)}></Button>
                            </Form.Item>
                        </Layout.Col>
                        <Layout.Col span="2">
                            <Form.Item>
                                {index !== this.state.component.attributes.links.length - 1 && <Button icon="caret-bottom"
                                    onClick={() => moveDown()}></Button>}
                            </Form.Item>
                        </Layout.Col>
                        <Layout.Col span="2">
                            <Form.Item>
                                {index !== 0 && <Button icon="caret-top"
                                    onClick={() => moveUp()}></Button>}
                            </Form.Item>
                        </Layout.Col>
                        {index === this.state.component.attributes.links.length - 1 &&
                        <Layout.Col span="2">
                            <Form.Item>
                                <Button type="primary" icon="plus"
                                    onClick={() => this.addLink()}></Button>
                            </Form.Item>
                        </Layout.Col>
                        }
                        {link.children && this.linkGroup(link)}

                    </Layout.Row>

                </Form.Item>
            </Layout.Col>
        );
    }

    linkGroup(root) {
        const addLink = () => {
            const component = this.state.component;
            root.children.push({});
            this.setState({component});
        };

        const removeLink = (index) => {
            const component = this.state.component;
            root.children.splice(index, 1);
            this.setState({component});
        };

        const changeLink = (key, val, index) => {
            const component = this.state.component;
            const link = root.children[index];
            link[key] = val;
            this.setState({component});
        };

        const moveUp = (index) => {
            if (index === 0) {
                return;
            }
            move(index, index - 1);
        };
        const moveDown = (index) => {
            if (index === root.children.length - 1) {
                return;
            }
            move(index, index + 1);
        };

        const move = (from, to) => {
            const component = this.state.component;
            const link = root.children.splice(from, 1)[0];
            root.children.splice(to, 0, link);
            this.setState({component});
        };

        return <div className="page-header__link-subcontainer">
            {root.children.map((link, index) =>
                <div className="page-header__link-sublink" key={index}>
                    <Layout.Col span="5" className="page-header__link-name" style={{marginLeft: "-5px"}}>
                        <Form.Item>
                            <Input placeholder={i18n.t("page.displayName")}
                                value={link.displayName}
                                onChange={val => changeLink("displayName", val, index)}></Input>
                        </Form.Item>
                    </Layout.Col>
                    <Layout.Col span="11">
                        <Form.Item>
                            <Input placeholder={i18n.t("page.link")}
                                value={link.link}
                                disabled={link.children}
                                onChange={val => changeLink("link", val, index)}></Input>
                        </Form.Item>
                    </Layout.Col>
                    {root.children.length !== 1 &&
                    <Layout.Col span="2">
                        <Form.Item>
                            <Button type="danger" icon="minus"
                                onClick={() => removeLink(index)}></Button>
                        </Form.Item>
                    </Layout.Col>
                    }

                    <Layout.Col span="2">
                        <Form.Item>
                            {index !== root.children.length - 1 && <Button icon="caret-bottom"
                                onClick={() => moveDown(index)}></Button>}
                        </Form.Item>
                    </Layout.Col>
                    <Layout.Col span="2">
                        <Form.Item>
                            {index !== 0 && <Button icon="caret-top"
                                onClick={() => moveUp(index)}></Button>}
                        </Form.Item>
                    </Layout.Col>
                    {index === root.children.length - 1 &&
                    <Layout.Col span="2">
                        <Form.Item>
                            <Button type="primary" icon="plus"
                                onClick={() => addLink()}></Button>
                        </Form.Item>
                    </Layout.Col>
                    }
                </div>
            )}
        </div>;
    }


    files() {
        const files = [];
        if (this.state.component.attributes && this.state.component.attributes.logoImageURL) {
            files.push({src: this.state.component.attributes.logoImageURL});
        }
        return files;
    }

    onFileChange(files) {
        if (files && files.length > 0) {
            const component = this.state.component;
            component.attributes.logoImageURL = files[0].path;
            this.setState({
                component,
                isChangingFile: false
            });
        } else {
            this.deleteLogo();
        }
    }

    deleteLogo() {
        const component = this.state.component;
        component.attributes.logoImageURL = null;
        this.setState({
            component,
            isChangingFile: false
        });
    }

    editor() {
        if (this.state.isChangingFile) {
            return <FileBrowser visible={true}
                files={this.files()}
                multiple={false}
                onChange={files => this.onFileChange(files)}/>;
        }
        return <Form>
            <Layout.Row>
                <Layout.Col span="12">
                    <Form.Item label={i18n.t("page.logoText")} labelWidth="120">
                        <Input value={this.state.component.attributes.logoText}
                            onChange={value => this.formChange("logoText", value)}></Input>
                    </Form.Item>
                </Layout.Col>
                <Layout.Col span="12">
                    <Form.Item label={i18n.t("page.headerLogo")} labelWidth="50">
                        {!this.state.component.attributes.logoImageURL &&
                        <div className="page-image-slider__image-container page-image-slider__image-add">
                            <Button type="text" size="small"
                                className="page-file-browser__add-button"
                                onClick={() => this.setState({isChangingFile: true})}>+</Button>
                        </div>
                        }
                        <FileBrowser buttonType="button" buttonSize="large"
                            files={this.files()}
                            multiple={false}
                            onChange={files => this.onFileChange(files)}/>
                        {this.state.component.attributes.logoImageURL &&
                        <div className="page-header-container__logo-preview">
                            <img src={this.state.component.attributes.logoImageURL} alt=""/>
                            <Button icon="close" onClick={() => this.deleteLogo()}></Button>
                        </div>
                        }
                    </Form.Item>
                </Layout.Col>
                {this.state.component.attributes.links.length === 0 &&
                <Layout.Col span="24">
                    <Form.Item label={i18n.t("page.headerLinks")} labelWidth="120">
                        <Button type="primary" icon="plus"
                            onClick={() => this.addLink()}></Button>
                    </Form.Item>
                </Layout.Col>
                }
                {this.state.component.attributes.links.map((link, index) => this.renderLink(link, index))}
            </Layout.Row>
            <Button type="primary" onClick={(e) => this.save(e)}>{i18n.t("page.save")}</Button>
        </Form>;
    }

    render() {
        return (
            <div className={(this.state.mode === "edit" ? "editing " : "") + "page-header"}>
                {this.state.mode === "edit"
                    ? this.editor()
                    : <div className="page-header__preview">
                        <div className="page-header-preview__logo">
                            {this.state.component.attributes.logoImageURL && <img src={this.state.component.attributes.logoImageURL} alt=""/>}
                            {!this.state.component.attributes.logoImageURL && "LOGO"}
                        </div>
                        {this.state.component.attributes.userEnabled &&
                        <div className="page-header-preview__user">
                            <div className="page-header-preview__user-link">{i18n.t("page.headerLogin")}</div>
                            <div className="page-header-preview__user-link">{i18n.t("page.headerRegister")}</div>
                        </div>
                        }
                        {this.state.component.attributes.searchEnabled &&
                        <div className="page-header-preview__search">
                            <div className="page-header-preview__search-inner">{i18n.t("page.headerSearch")}</div>
                        </div>
                        }
                        <div className="page-header-preview__links">
                            {this.state.component.attributes.links.length === 0 &&
                            <span className="page-header-preview__link">{i18n.t("page.headerLinks")}</span>
                            }
                            {this.state.component.attributes.links.map((link, index) =>
                                <a key={index}
                                    href={link.link} className="page-header-preview__link">
                                    {link.displayName}
                                    &nbsp;
                                    {link.children && <i className="fa fa-angle-down"></i>}
                                </a>
                            )}
                        </div>
                    </div>
                }
            </div>
        );
    }
}

PageHeaderComponent.defaultProps = {mode: "preview"};

PageHeaderComponent.propTypes = {
    component: {
        attributes: {
            links: PropTypes.string,
            userEnabled: PropTypes.bool,
            searchEnabled: PropTypes.bool
        }
    },
    onChange: PropTypes.func,
    mode: PropTypes.string
};