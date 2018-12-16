import React from "react";
import {Button, Form, Dialog, Input} from "element-react";
import PropTypes from "prop-types";
import FileBrowser from "./page.file.browser";

const i18n = window.i18n;
export default class PageDownloadComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            directoryId: null,
            component: props.component,
            onChange: props.onChange,
            mode: props.mode,
            fileURL: props.component.attributes.src,
            uploading: false,
            fileName: null
        };
    }

    delete() {
        const component = this.state.component;
        if (component.attributes) {
            component.attributes.src = "";
        }
        this.setState({component: component});
    }

    formChange(key, value) {
        const component = this.state.component;
        component.attributes = Object.assign({}, this.state.component.attributes, {[key]: value});
        this.setState({component: component});
    }

    onChange() {
        this.state.onChange(this.state.component);
    }

    onFileChange(files) {
        if (files && files.length > 0) {
            const component = this.state.component;
            if (component.attributes) {
                component.attributes.src = files[0].path;
            } else {
                component.attributes = {src: files[0].path};
            }
            this.setState({component: component});
        } else {
            this.delete();
        }
    }

    isFileSelected() {
        return this.state.component.attributes && this.state.component.attributes.src;
    }

    files() {
        const files = [];
        if (this.state.image && this.state.image.src) {
            files.push(this.state.image);
        }
        return files;
    }

    editor() {
        if (!this.state.component.attributes.src) {
            return <FileBrowser files={this.files()}
                multiple={false}
                visible={true}
                onChange={files => this.onFileChange(files)}/>;
        }
        return (
            <Dialog
                visible={true}
                onCancel={() => this.onChange()}
            >
                <Dialog.Body>
                    <Form ref={(form) => {
                        this.form = form;
                    }} model={this.state.component.attributes} labelWidth="80" rules={this.state.formRules}>
                        <Form.Item label={i18n.t("page.name")} props="name">
                            <Input disabled={!this.isFileSelected()} value={this.state.component.attributes.name} onChange={val => this.formChange("name", val)}/>
                        </Form.Item>
                        <Form.Item label={i18n.t("page.description")} props="description">
                            <Input disabled={!this.isFileSelected()} type="textarea" value={this.state.component.attributes.description} onChange={val => this.formChange("description", val)}/>
                        </Form.Item>
                    </Form>
                </Dialog.Body>

                <Dialog.Footer className="dialog-footer">
                    <Button type="primary" onClick={() => this.onChange()}>{i18n.t("page.save")}</Button>
                </Dialog.Footer>
            </Dialog>
        );
    }

    render() {
        return (
            <div className={(this.state.mode === "edit" ? "editing " : "") + "page-image"}>
                {this.state.mode === "edit"
                    ? this.editor()
                    : <div className="page-image__preview">
                        <a href={this.state.component.attributes.src} target="_blank">{this.state.component.attributes.name || "your download link will be here"}</a>
                    </div>
                }
            </div>
        );
    }
}

PageDownloadComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};