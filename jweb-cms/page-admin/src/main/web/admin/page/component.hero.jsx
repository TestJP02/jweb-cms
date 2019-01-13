import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Layout} from "element-react";
import FileBrowser from "./page.file.browser";
import "./component.hero.css";

const i18n = window.i18n;
export default class PageHeroComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            component: props.component,
            onChange: props.onChange,
            mode: props.mode,
            editingImage: null
        };
    }

    componentWillMount() {
        const component = this.state.component;
        if (!component.attributes.btn1) {
            component.attributes.btn1 = {};
        }
        if (!component.attributes.btn2) {
            component.attributes.btn2 = {};
        }
        this.setState({component});
    }

    componentWillReceiveProps(nextProps) {
        const mode = nextProps.mode;
        this.setState({mode});
    }

    saveComponent() {
        const component = this.state.component;
        this.setState({component});
        this.state.onChange(component);
    }

    formChange(key, val) {
        const keys = key.split(".");
        const component = this.state.component;
        let data = component.attributes;
        keys.forEach((thisKey, index) => {
            if (index === keys.length - 1) {
                data[thisKey] = val;
                return;
            }
            if (!data[thisKey]) {
                data[thisKey] = {};
            }
            data = data[thisKey];
        });
        this.setState({component});
    }

    files() {
        const files = [];
        files.push({src: this.state.component.attributes[this.state.editingImage]});
        return files;
    }

    changeImage(type) {
        this.setState({editingImage: type});
    }

    onFileChange(files) {
        if (files && files.length > 0) {
            this.saveImage(files);
        } else {
            this.deleteImage();
        }
        // this.saveComponent();
    }

    saveImage(files) {
        const component = this.state.component;
        component.attributes[this.state.editingImage] = files[0].path;
        this.setState({
            component,
            editingImage: null
        });
    }

    deleteImage() {
        this.setState({image: {}});
        const component = this.state.component;
        component.attributes[this.state.editingImage] = null;
        this.setState({
            component,
            editingImage: null
        });
    }

    editor() {
        if (this.state.editingImage) {
            return <FileBrowser files={this.files()}
                multiple={false}
                visible={true}
                onChange={files => this.onFileChange(files)}/>;
        }
        return (
            <Dialog
                visible={true}
                onCancel={() => this.saveComponent()}>
                <Dialog.Body>
                    <Form ref={(form) => {
                        this.form = form;
                    }} model={this.state.image} labelWidth="80" rules={this.state.formRules}>
                        <Layout.Row>
                            <Layout.Col span="12">
                                <Form.Item label={i18n.t("page.title")} props="title">
                                    <Input value={this.state.component.attributes.title} onChange={val => this.formChange("title", val)}/>
                                </Form.Item>
                            </Layout.Col>
                            <Layout.Col span="12">
                                <Form.Item label={i18n.t("page.subTitle")} props="subTitle">
                                    <Input value={this.state.component.attributes.subtitle} onChange={val => this.formChange("subtitle", val)}/>
                                </Form.Item>
                            </Layout.Col>
                        </Layout.Row>
                        <Layout.Row>
                            <Layout.Col span="12">
                                <Form.Item label={i18n.t("page.btn.title") + 1} props="btn1.title">
                                    <Input value={this.state.component.attributes.btn1.title} onChange={val => this.formChange("btn1.title", val)}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("page.btn.url") + 1} props="btn1.url">
                                    <Input value={this.state.component.attributes.btn1.url} onChange={val => this.formChange("btn1.url", val)}/>
                                </Form.Item>
                            </Layout.Col>
                            <Layout.Col span="12">
                                <Form.Item label={i18n.t("page.btn.title") + 2} props="btn2.title">
                                    <Input value={this.state.component.attributes.btn2.title} onChange={val => this.formChange("btn2.title", val)}/>
                                </Form.Item>
                                <Form.Item label={i18n.t("page.btn.url") + 2} props="btn2.url">
                                    <Input value={this.state.component.attributes.btn2.url} onChange={val => this.formChange("btn2.url", val)}/>
                                </Form.Item>
                            </Layout.Col>
                        </Layout.Row>
                        <Layout.Row>
                            <Layout.Col span="12">
                                <Form.Item label={i18n.t("page.background")} props="background">
                                    <div onClick={() => this.changeImage("background")}>
                                        {this.state.component.attributes.background
                                            ? <img src={this.state.component.attributes.background} alt="" className="hero-component__edit-image"/>
                                            : <Button>{i18n.t("page.background")}</Button>
                                        }
                                    </div>
                                </Form.Item>
                            </Layout.Col>
                            <Layout.Col span="12">
                                <Form.Item label={i18n.t("page.clip")} props="clip">
                                    <div onClick={() => this.changeImage("clip")}>
                                        {this.state.component.attributes.clip
                                            ? <img src={this.state.component.attributes.clip} alt="" className="hero-component__edit-image"/>
                                            : <Button>{i18n.t("page.clip")}</Button>
                                        }
                                    </div>
                                </Form.Item>
                            </Layout.Col>
                        </Layout.Row>


                    </Form>
                </Dialog.Body>

                <Dialog.Footer className="dialog-footer">
                    <Button type="primary" onClick={() => this.saveComponent()}>{i18n.t("page.save")}</Button>
                </Dialog.Footer>
            </Dialog>
        );
    }

    render() {
        return (
            <div className={(this.state.mode === "edit" ? "editing " : "page-component") + " image-component"}>
                {this.state.mode === "edit" && this.editor()}
                <div className="hero-component">
                    <div className="hero-component__background">
                        <img className="hero-component__background" src={this.state.component.attributes.background} alt=""/>
                    </div>
                    <div className="container">

                        <div className="row">
                            <div className="hero-title col-sm-7 col-lg-6">
                                <h1>{this.state.component.attributes.title}</h1>
                                <p>{this.state.component.attributes.subtitle}</p>

                                <div className="hero-form-button-group">
                                    {this.state.component.attributes.btn1 && this.state.component.attributes.btn1.title &&
                                    <a className="btn hero-form-button">{this.state.component.attributes.btn1.title}</a>
                                    }
                                    {this.state.component.attributes.btn2 && this.state.component.attributes.btn2.title &&
                                    <a className="btn hero-form-button">{this.state.component.attributes.btn2.title}</a>
                                    }
                                </div>

                            </div>
                            <div className="hero-clip col-sm-5 col-lg-6">
                                <div className="hero-image">
                                    <img src={this.state.component.attributes.clip}/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

PageHeroComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};