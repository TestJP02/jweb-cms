import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Layout} from "element-react";
import FileBrowser from "./page.file.browser";
import "./component.image.css";

const i18n = window.i18n;
export default class PageImageComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            directoryId: null,
            component: props.component,
            onChange: props.onChange,
            mode: props.mode,
            image: props.component.attributes,
            uploading: false,
            index: null
        };
    }

    componentWillMount() {
        if (!this.state.image) {
            this.setState({image: {src: ""}});
        }
    }

    componentWillReceiveProps(nextProps) {
        const mode = nextProps.mode;
        this.setState({mode});
    }

    delete() {
        this.setState({image: {}});
        const component = this.state.component;
        component.attributes = {};
        this.saveComponent();
    }

    saveComponent() {
        const component = this.state.component;
        component.attributes = this.state.image;
        this.setState({component});
        this.state.onChange(component);
    }

    formChange(key, val) {
        const keys = key.split(".");
        const image = this.state.image;
        let data = image;
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
        this.setState({image: image});
    }

    files() {
        const files = [];
        if (this.state.image && this.state.image.src) {
            files.push(this.state.image);
        }
        return files;
    }

    onFileChange(files) {
        if (files && files.length > 0) {
            const image = this.state.image;
            image.src = files[0].path;
            this.setState({image});
        } else {
            this.delete();
        }
        this.saveComponent();
    }

    editor() {
        if (!this.state.image.src) {
            return <FileBrowser files={this.files()}
                multiple={false}
                visible={true}
                onChange={files => this.onFileChange(files)} />;
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
                            <Layout.Col span="24">
                                <Form.Item label={i18n.t("page.link")} props="link">
                                    <Input value={this.state.image.link} onChange={val => this.formChange("link", val)} />
                                </Form.Item>
                            </Layout.Col>
                            <Layout.Col span="12">
                                <Form.Item label={i18n.t("page.width")} props="width">
                                    <Input type="number" value={this.state.image.width}
                                        onChange={val => this.formChange("width", val)} />
                                </Form.Item>
                            </Layout.Col>
                            <Layout.Col span="12">
                                <Form.Item label={i18n.t("page.height")} props="height">
                                    <Input type="number" value={this.state.image.height}
                                        onChange={val => this.formChange("height", val)} />
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
            <div className={(this.state.mode === "edit" || !this.state.image.src ? "editing " : "page-component") + " image-component"}>
                {(this.state.mode === "edit" || !this.state.image.src) && this.editor()}
                <div className="image-component__image" onClick={(e) => {
                    e.stopPropagation();
                    e.nativeEvent.stopImmediatePropagation();
                }}>
                    {this.state.image.src &&
                        <img src={"/admin" + this.state.image.src} alt="" width={this.state.image.width + "px"} height={this.state.image.height + "px"} />
                    }
                </div>
            </div>
        );
    }
}

PageImageComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};