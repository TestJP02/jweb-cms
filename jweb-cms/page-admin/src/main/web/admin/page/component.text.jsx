import React from "react";
import PropTypes from "prop-types";
import {Button, ColorPicker,  Dialog, Form, Input, Layout, Select} from "element-react";
import Editor from "./content.editor.jsx";
import {stateToHTML} from "draft-js-export-html";

import "./component.text.css";

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
    
    changeContent(editorState) {
        this.formChange("content", stateToHTML(editorState.getCurrentContent()))
    }

    render() {
        const attributes = this.state.component.attributes;
        const textareaStyle = Object.assign({}, {
            fontSize: attributes.fontSize + attributes.fontSizeUnit,
            lineHeight: attributes.lineHeight + attributes.lineHeightUnit,
            color: attributes.color
        });
        return (
            <div className="page-default-component page-text-component">
                {this.state.mode === "edit" &&
                    <Dialog
                        visible={true}
                        onCancel={() => this.saveComponent()}
                    >
                        <Dialog.Body>
                            <div className="page-text-component__editor">
                                <Editor content={this.state.component.attributes.content} onChange={editorState=>this.changeContent(editorState)}></Editor>
                            </div>
                        </Dialog.Body>

                        <Dialog.Footer className="dialog-footer">
                            <Button type="primary" onClick={() => this.saveComponent()}>{i18n.t("page.save")}</Button>
                        </Dialog.Footer>
                    </Dialog>
                }
                <div className="page-default-component__content">
                    <div dangerouslySetInnerHTML={{__html: this.state.component.attributes.content}}>
                        
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