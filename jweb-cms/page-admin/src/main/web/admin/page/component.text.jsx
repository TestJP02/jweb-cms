import React from "react";
import PropTypes from "prop-types";
import {Button} from "element-react";
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
            content: props.component.attributes.content || "",
            editingIndex: null
        };
    }

    componentWillReceiveProps(nextProps) {
        const mode = nextProps.mode;
        this.setState({mode});
    }

    saveComponent() {
        const component = this.state.component;
        this.state.onChange(component);
    }

    changeContent(editorState) {
        const content = stateToHTML(editorState.getCurrentContent());
        const component = this.state.component;
        component.attributes.content = content;
        this.setState({
            content: content,
            component: component
        });
    }

    render() {
        return (
            <div className="page-default-component page-text-component">
                {this.state.mode === "edit" &&
                <div className="page-text-component__editor">
                    <Editor content={this.state.content} onChange={editorState => this.changeContent(editorState)}></Editor>
                    < Button type="primary" onClick={() => this.saveComponent()}>{i18n.t("page.save")}</Button>
                </div>
                }

                {this.state.mode !== "edit" &&
                <div className="page-default-component__content">
                    <div dangerouslySetInnerHTML={{__html: this.state.content}}></div>
                </div>
                }

            </div>
        );
    }
}

PageTextComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};