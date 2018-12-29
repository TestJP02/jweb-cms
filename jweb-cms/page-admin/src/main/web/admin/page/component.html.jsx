import React from "react";
import PropTypes from "prop-types";
import {ContentState, convertFromHTML, Editor, EditorState, RichUtils} from "draft-js";
import {stateToHTML} from "draft-js-export-html";

export default class PageHtmlComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            component: props.component,
            onChange: props.onChange,
            readOnly: props.readOnly,
            editor: null,
            editorState: this.editorState(props.component)
        };
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.component.attributes.content !== stateToHTML(this.state.editorState.getCurrentContent())) {
            this.setState({
                component: nextProps.component,
                editorState: this.editorState(nextProps.component),
                readOnly: nextProps.readOnly,
                onChange: nextProps.onChange
            });
        }
    }

    editorState(component) {
        const html = component.attributes.content ? component.attributes.content : "";
        if (component.attributes.content) {
            const blocksFromHTML = convertFromHTML(html);
            const contentState = ContentState.createFromBlockArray(blocksFromHTML.contentBlocks, blocksFromHTML.entityMap);
            return EditorState.createWithContent(contentState);
        }
        return EditorState.createEmpty();
    }

    getEditorState() {
        return this.state.editorState;
    }

    toggleInlineStyle(style) {
        this.setState({editorState: RichUtils.toggleInlineStyle(this.state.editorState, style)});
    }

    onChange(editorState) {
        const component = this.state.component;
        component.attributes.content = stateToHTML(editorState.getCurrentContent());
        this.setState({
            component,
            editorState
        });
    }

    save() {
        this.props.onChange(this.state.component);
    }

    edit() {
        this.state.editor.focus();
    }

    handleKeyCommand(command, editorState) {
        const newState = RichUtils.handleKeyCommand(editorState, command);
        if (newState) {
            this.onChange(newState);
            return true;
        }
        return false;
    }

    onTab(e) {
        const maxDepth = 4;
        this.onChange(RichUtils.onTab(e, this.state.editorState, maxDepth));
    }

    render() {
        return (
            <Editor editorState={this.state.editorState}
                onChange={value => this.onChange(value)}
                onBlur={value => this.save()}
                handleKeyCommand={(command, editorState) => this.handleKeyCommand(command, editorState)}
                onTab={e => this.onTab(e)}
                spellCheck={true}
                ref={(editor) => {
                    if (!this.state.editor) {
                        this.setState({editor: editor});
                    }
                }}/>
        );
    }
}
PageHtmlComponent.propTypes = {
    component: PropTypes.object,
    readOnly: PropTypes.bool,
    onChange: PropTypes.func
};