import React from "react";
import {MarkdownUtil, ReactMdeCommands, ReactMdeComponents, DraftUtil} from "react-mde";
import FileBrowser from "../../page.file.browser";

const CustomCommands = {
    callback: null,
    onChangeHandler: null,
    call(callback) {
        this.callback = callback;
    },
    onChange(handler) {
        this.onChangeHandler = handler;
    },
    commands() {
        return [
            [ReactMdeCommands.headerCommand, ReactMdeCommands.boldCommand, ReactMdeCommands.italicCommand],
            [ReactMdeCommands.linkCommand, ReactMdeCommands.quoteCommand, ReactMdeCommands.codeCommand],
            [ReactMdeCommands.unorderedListCommand, ReactMdeCommands.orderedListCommand],
            [ImageCommand]
        ];
    }
};
const ImageCommand = {
    buttonContent: <ReactMdeComponents.MdeToolbarIcon icon="image" />,
    buttonProps: {"aria-label": "Add bold text"},
    // children: [],
    openFileBrowser() {
        return <FileBrowser multiple={false}
            visible={true}
            onChange={files => this.changeImage(files)} />;
    },
    changeImage(files) {
        CustomCommands.callback("");
        if (files && files.length > 0) {
            const {text, selection} = DraftUtil.getMarkdownStateFromDraftState(this.state);
            const {newText, insertionLength} = MarkdownUtil.insertText(text, "![", selection.start);
            const finalText = MarkdownUtil.insertText(newText, `](${files[0].path})`, selection.end + insertionLength).newText;

            CustomCommands.onChangeHandler(DraftUtil.buildNewDraftState(this.state, {
                text: finalText,
                selection: {
                    start: selection.start + insertionLength,
                    end: selection.end + insertionLength
                }
            }));
        }
    },
    execute(state) {
        this.state = state;
        CustomCommands.callback(this.openFileBrowser());
        return DraftUtil.buildNewDraftState(state, DraftUtil.getMarkdownStateFromDraftState(state));
    }
};

export default CustomCommands;