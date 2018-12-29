import React from "react";
import PropTypes from "prop-types";
import ReactMde from "react-mde";
import {ContentState, EditorState} from "draft-js";
import Showdown from "showdown";
import CustomCommands from "./commands/CustomCommand";
import "react-mde/lib/styles/css/react-mde-all.css";

export default class PageMDComponent extends React.Component {
    constructor(props) {
        super(props);
        CustomCommands.call(dom => this.showDialog(dom));
        CustomCommands.onChange(state => this.onDraftChange(state));
        this.converter = new Showdown.Converter({
            tables: true,
            simplifiedAutoLink: true,
            extensions: [{
                type: "lang",
                regex: /\/file\/upload\//g,
                replace: "/admin/file/upload/"
            }]
        });
        const mdeState = {html: ""};
        mdeState.markdown = props.content;
        mdeState.draftEditorState = EditorState.createWithContent(ContentState.createFromText(props.content));

        this.state = {
            content: props.content,
            mdeState
        };
    }

    componentDidUpdate(prevProps) {
        if (this.props.content && this.props.content !== this.state.content) {
            const mdeState = {
                html: "",
                markdown: this.props.content,
                draftEditorState: EditorState.createWithContent(ContentState.createFromText(this.props.content))
            };
            this.setState({
                content: this.props.content,
                mdeState
            });
        }
    }

    showDialog(dom) {
        this.setState({commandDom: dom});
    }

    onDraftChange(draftEditorState) {
        const mdeState = this.state.mdeState;
        mdeState.draftEditorState = draftEditorState;
        this.setState({mdeState: mdeState});
    }

    onContentChange(mdeState) {
        this.props.onChange(mdeState.markdown);
        this.setState({mdeState});
    }

    render() {
        return [
            <ReactMde key="reactmde-container"
                onChange={mdeState => this.onContentChange(mdeState)}
                editorState={this.state.mdeState}
                commands={CustomCommands.commands()}
                generateMarkdownPreview={markdown => Promise.resolve(this.converter.makeHtml(markdown))}
            />,
            <div className="command-container" key="command-container">
                {this.state.commandDom}
            </div>
        ];
    }
}


PageMDComponent.propTypes = {
    content: PropTypes.string,
    onChange: PropTypes.func
};