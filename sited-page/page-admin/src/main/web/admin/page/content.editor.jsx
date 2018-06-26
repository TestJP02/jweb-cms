import React from "react";
import PropTypes from "prop-types";
import {AtomicBlockUtils, CompositeDecorator, convertFromRaw, Editor, EditorState, getDefaultKeyBinding, Modifier, RichUtils, SelectionState} from "draft-js";
import "../../node_modules/draft-js/dist/Draft.css";
import "./content.editor.css";
import ComponentWrapper from "./component.wrapper.jsx";
import {Button, ColorPicker, Input} from "element-react";

const bundle = window.app.bundle("pageBundle");

export default class ContentEditor extends React.Component {
    constructor(props) {
        super(props);
        const styleMap = {
            "SUP": {
                "font-size": "0.75em",
                "line-height": "1",
                "display": "inline-block",
                "vertical-align": "text-top",
                "padding": "0 3px"
            },
            "SUB": {
                "font-size": "0.75em",
                "line-height": "1",
                "vertical-align": "text-bottom",
                "padding": "0 3px"
            }
        };
        const decorator = new CompositeDecorator([
            {
                strategy: this.linkStrategy,
                component: this.handleLink
            }
        ]);
        const editorState = this.props.content && this.props.content.length ? EditorState.createWithContent(convertFromRaw({
            blocks: props.content.map(
                (component) => {
                    const block = {
                        key: component.id,
                        type: this.type(component.name),
                        text: component.attributes && component.attributes.innerText ? component.attributes.innerText : "",
                        data: component.attributes ? component.attributes : {},
                        depth: component.attributes ? component.attributes.depth : 0,
                        inlineStyleRanges: component.attributes && component.attributes.inlineStyles ? component.attributes.inlineStyles : []
                    };
                    if (block.inlineStyleRanges) {
                        block.inlineStyleRanges.map((style) => {
                            if (style.style.indexOf("COLOR:") === 0) {
                                styleMap[style.style] = {color: style.style.substr(6)};
                            } else if (style.style.indexOf("BGCOLOR:") === 0) {
                                styleMap[style.style] = {"background-color": style.style.substr(8)};
                            }
                        });
                    }
                    return block;
                }
            ),
            entityMap: {}
        }), decorator) : EditorState.createEmpty(decorator);

        this.enterCount = 0;
        this.state = {
            readOnly: false,
            buttons: {
                hidden: true,
                open: false
            },
            bar: {
                hidden: true,
                currentBlockStyle: null,
                currentInlineStyles: []
            },
            editorState: editorState,
            editorBlockCommands: [
                {
                    label: "H1",
                    style: "header-one"
                },
                {
                    label: "H2",
                    style: "header-two"
                },
                {
                    label: "H3",
                    style: "header-three"
                },
                // {
                //     label: "H4",
                //     style: "header-four"
                // },
                // {
                //     label: "H5",
                //     style: "header-five"
                // },
                // {
                //     label: "H6",
                //     style: "header-six"
                // },
                {
                    icon: "fa fa-quote-right",
                    label: "Blockquote",
                    style: "blockquote"
                },
                {
                    icon: "fa fa-code",
                    label: "Code Block",
                    style: "code-block"
                },
                {
                    icon: "fa fa-list-ol",
                    label: "OL",
                    style: "ordered-list-item"
                },
                {
                    icon: "fa fa-list-ul",
                    label: "UL",
                    style: "unordered-list-item"
                }
            ],
            editorInlineCommands: [
                {
                    label: "Bold",
                    style: "BOLD",
                    icon: "fa fa-bold"
                },
                {
                    label: "Italic",
                    style: "ITALIC",
                    icon: "fa fa-italic"
                },
                {
                    label: "Underline",
                    style: "UNDERLINE",
                    icon: "fa fa-underline"
                },
                {
                    label: "Sup",
                    style: "SUP",
                    icon: "fa fa-superscript"
                },
                {
                    label: "Sub",
                    style: "SUB",
                    icon: "fa fa-subscript"
                },
                {
                    label: "Link",
                    style: "LINK",
                    icon: "fa fa-link"
                },
                {
                    label: "Color",
                    style: "COLOR"
                },
                {
                    label: "BgColor",
                    style: "BGCOLOR"
                }
            ],
            styleMap: styleMap
        };
    }

    type(name) {
        if (name === "h1") {
            return "header-one";
        } else if (name === "h2") {
            return "header-two";
        } else if (name === "h3") {
            return "header-three";
        } else if (name === "h4") {
            return "header-four";
        } else if (name === "h5") {
            return "header-five";
        } else if (name === "h6") {
            return "header-six";
        }
        return name;
    }

    hasInlineStyle(style, contains) {
        if (contains) {
            for (let i = 0; i < this.state.bar.currentInlineStyles.length; i += 1) {
                if (this.state.bar.currentInlineStyles[i].indexOf(style) >= 0) {
                    return true;
                }
            }
        }
        return this.state.bar.currentInlineStyles.includes(style);
    }

    executeBlockCommand(command) {
        const editorState = RichUtils.toggleBlockType(
            this.state.editorState,
            command
        );
        const bar = this.state.bar;
        bar.hidden = true;
        this.setState({
            editorState: editorState,
            bar: bar
        });
        this.props.onChange(editorState);
    }

    executeInlineCommand(originCommand, value) {
        let command = originCommand;
        if (originCommand === "COLOR") {
            const styleMap = this.state.styleMap;
            command = originCommand + ":" + value;
            styleMap[command] = {color: value};
            this.setState({styleMap});
        } else if (originCommand === "BGCOLOR") {
            const styleMap = this.state.styleMap;
            command = originCommand + ":" + value;
            styleMap[command] = {"background-color": value};
            this.setState({styleMap});
        } else if (originCommand === "LINK") {
            command = originCommand + ":" + value;
        }
        const editorState = RichUtils.toggleInlineStyle(
            this.state.editorState,
            command
        );
        const bar = this.state.bar;
        bar.hidden = true;
        this.setState({
            editorState: editorState,
            bar: bar
        });
        this.props.onChange(editorState);
    }

    onChange(editorState) {
        this.setState({editorState: editorState}, () => this.displayAction(editorState));
        this.props.onChange(editorState);
    }

    displayAction(editorState) {
        const contentState = editorState.getCurrentContent();
        const selectionState = editorState.getSelection();
        const block = contentState.getBlockForKey(selectionState.getAnchorKey());
        this.findCurrentColor(block, selectionState);
        if (this.shouldDisplayButtons(block)) {
            const domNode = this.getSelectedBlockElement();
            if (domNode) {
                const position = this.position(domNode);
                const buttons = this.state.buttons;
                buttons.hidden = false;
                buttons.top = position.y;
                buttons.left = position.x;
                this.setState({buttons});
            }
        } else if (this.shouldDisplayBar(block, selectionState)) {
            const domNode = this.getSelectedBlockElement();
            if (domNode) {
                const position = this.position(domNode);
                const bar = this.state.bar;
                bar.hidden = false;
                bar.top = position.y;
                bar.left = position.x;
                bar.currentBlockStyle = block.getType();
                bar.currentInlineStyles = this.inlineStyles(block, selectionState);

                this.setState({bar});
            }
        } else {
            const buttons = this.state.buttons;
            buttons.hidden = true;
            const bar = this.state.bar;
            bar.hidden = true;
            this.setState({
                buttons: buttons,
                bar: bar
            });
        }
    }

    findCurrentColor(block, selectionState) {
        const styles = block.getInlineStyleAt(selectionState.getStartOffset());
        let currentColor = null;
        let currentBgColor = null;
        styles.map((style) => {
            if (style.indexOf("COLOR:") === 0) {
                currentColor = style.substr(6);
            } else if (style.indexOf("BGCOLOR:") === 0) {
                currentBgColor = style.substr(8);
            }
        });
        this.setState({
            currentColor,
            currentBgColor
        });
    }

    position(domNode) {
        const containerPosition = this.container.getBoundingClientRect();
        const position = domNode.getBoundingClientRect();
        return {
            x: position.left - containerPosition.left,
            y: position.top - containerPosition.top
        };
    }

    shouldDisplayButtons(block) {
        return block && this.isNativeBlockType(block.type) && block.text === "";
    }

    shouldDisplayBar(block, selectionState) {
        const start = selectionState.getStartOffset();
        const end = selectionState.getEndOffset();
        return start !== end && block && this.isNativeBlockType(block.getType());
    }

    inlineStyles(block, selectionState) {
        const startStyles = block.getInlineStyleAt(selectionState.getStartOffset()).toJS();
        const endStyles = block.getInlineStyleAt(selectionState.getEndOffset() - 1).toJS();
        return startStyles.filter(n => endStyles.includes(n));
    }

    isNativeBlockType(type) {
        return type === "header-one" ||
            type === "header-two" ||
            type === "header-three" ||
            type === "header-four" ||
            type === "header-five" ||
            type === "header-six" ||
            type === "blockquote" ||
            type === "code-block" ||
            type === "atomic" ||
            type === "unordered-list-item" ||
            type === "ordered-list-item" ||
            type === "unstyled";
    }

    toggleButtons() {
        const buttons = this.state.buttons;
        buttons.open = !buttons.open;
        this.setState({buttons});
    }

    getSelectedBlockElement() {
        const selection = window.getSelection();
        if (selection.rangeCount === 0) {
            return null;
        }
        let node = selection.getRangeAt(0).startContainer;
        do {
            if (node.getAttribute && node.getAttribute("data-block") === "true") {
                return node;
            }
            node = node.parentNode;
        } while (!(node === null));
        return null;
    }

    onFocus() {
        if (this.editor) {
            this.editor.focus();
        }
    }

    onComponentFocus() {
        this.setState({readOnly: true});
    }

    onComponentBlur() {
        this.setState({readOnly: false});
    }

    onComponentChange(block, component) {
        const contentState = this.state.editorState.getCurrentContent();
        const blockKey = block.getKey();
        const targetRange = new SelectionState({
            anchorKey: blockKey,
            anchorOffset: 0,
            focusKey: blockKey,
            focusOffset: block.getLength()
        });
        const newContentState = Modifier.setBlockData(contentState, targetRange, component.data ? component.data : {});
        const newEditorState = EditorState.set(
            this.state.editorState,
            {currentContent: newContentState}
        );
        this.setState({editorState: newEditorState});
        this.props.onChange(newEditorState);
    }

    onComponentDelete(block) {
        const contentState = this.state.editorState.getCurrentContent();
        const blockKey = block.getKey();

        const targetRange = new SelectionState({
            anchorKey: blockKey,
            anchorOffset: 0,
            focusKey: blockKey,
            focusOffset: block.getLength()
        });

        let newContentState = Modifier.setBlockType(
            contentState,
            targetRange,
            "unstyled"
        );
        newContentState = Modifier.setBlockData(
            newContentState,
            targetRange,
            {});
        let newEditorState = EditorState.push(this.state.editorState, newContentState, "remove-range");
        newEditorState = EditorState.forceSelection(newEditorState, newContentState.getSelectionAfter());
        this.setState({
            editorState: newEditorState,
            readOnly: false
        });
        this.props.onChange(newEditorState);
    }

    onComponentCreate(component) {
        const contentState = this.state.editorState.getCurrentContent();
        const selectionState = this.state.editorState.getSelection();
        const block = contentState.getBlockForKey(selectionState.getAnchorKey());
        const blockKey = block.getKey();

        const targetRange = new SelectionState({
            anchorKey: blockKey,
            anchorOffset: 0,
            focusKey: blockKey,
            focusOffset: block.getLength()
        });

        let newContentState = Modifier.splitBlock(contentState, targetRange);
        newContentState = Modifier.setBlockType(newContentState, targetRange, component.name);
        newContentState = Modifier.setBlockData(newContentState, targetRange, component.data ? component.data : {});

        const newEditorState = EditorState.set(
            this.state.editorState,
            {currentContent: newContentState}
        );

        const buttons = this.state.buttons;
        buttons.hidden = true;
        buttons.open = false;
        this.setState({
            editorState: newEditorState,
            buttons: buttons
        });
        this.props.onChange(newEditorState);
    }

    deleteBefore(block) {

        const contentState = this.state.editorState.getCurrentContent();

        const beforeSelection = contentState.getSelectionBefore();
        const beforeBlock = contentState.getBlockForKey(beforeSelection.getAnchorKey());
        const targetRange = new SelectionState({
            anchorKey: beforeSelection.getAnchorKey(),
            anchorOffset: beforeBlock.getLength() - 1,
            focusKey: beforeSelection.getAnchorKey(),
            focusOffset: beforeBlock.getLength()
        });

        const newContentState = Modifier.removeRange(
            contentState,
            targetRange,
            "backward"
        );
        const newEditorState = EditorState.push(this.state.editorState, newContentState, "remove-range");
        this.setState({editorState: newEditorState});
        this.props.onChange(newEditorState);
    }

    blockRenderer(block) {
        for (let i = 0; i < this.props.components.length; i += 1) {
            const componentType = this.props.components[i];
            if (componentType.name === block.getType()) {
                return {
                    component: ComponentWrapper,
                    editable: false,
                    props: {
                        component: bundle.component(componentType.name),
                        onBlur: () => this.onComponentBlur(),
                        onFocus: () => this.onComponentFocus(),
                        onChange: (b, component) => this.onComponentChange(b, component),
                        onDelete: component => this.onComponentDelete(component),
                        onDeleteBefore: component => this.deleteBefore(component),
                        mode: "preview",
                        value: {
                            key: block.getKey(),
                            type: block.getType(),
                            text: block.getText(),
                            data: block.getData() ? block.getData().toJS() : {}
                        }
                    }
                };
            }
        }
    }

    blockStyleFn(contentBlock) {
        const type = contentBlock.getType();
        if (type === "blockquote") {
            return "blockquote";
        }
    }

    keyBindingFn(e) {
        if (e.keyCode === 13) {
            const enterCount = this.enterCount;
            if (enterCount === 1) {
                const editorState = this.state.editorState;
                const contentState = editorState.getCurrentContent();
                const selectionState = editorState.getSelection();
                const block = contentState.getBlockForKey(selectionState.getAnchorKey());

                const blockKey = block.getKey();
                const type = block.getType();

                if (type === "unstyled") {
                    this.enterCount = 0;
                    return getDefaultKeyBinding(e);
                }
                const targetRange = new SelectionState({
                    anchorKey: blockKey,
                    anchorOffset: 0,
                    focusKey: blockKey,
                    focusOffset: block.getLength()
                });

                const newEditorState = EditorState.set(
                    this.state.editorState,
                    {currentContent: Modifier.setBlockType(contentState, targetRange, "unstyled")}
                );

                this.enterCount = 0;
                this.setState({editorState: newEditorState});
                return "none";
            }
            this.enterCount = 1;
            return getDefaultKeyBinding(e);
        }
        this.enterCount = 0;
        return getDefaultKeyBinding(e);
    }

    handleKeyCommand(command) {
        if (command === "none") {
            return "handled";
        }
        return "not-handled";
    }

    componentDidMount() {
        this.editor.focus();
    }

    linkStrategy(contentBlock, fn, contentState) {
        const characterList = contentBlock.getCharacterList();
        let start = 0;
        let end = 0;
        let recording = false;
        for (let i = 0; i < characterList.size; i += 1) {
            const styles = characterList.get(i).getStyle();
            let isLink = false;
            styles.map((style) => {
                if (style.indexOf("LINK") >= 0) {
                    isLink = true;
                }
            });
            if (isLink) {
                if (!recording) {
                    recording = true;
                    start = i;
                    end = i;
                } else if (recording) {
                    end = i;
                }
            } else if (recording) {
                recording = false;
                fn(start, end + 1);
            }
        }
        if (recording) {
            recording = false;
            fn(start, end + 1);
        }
    }

    handleLink(props) {
        const contentState = props.contentState;
        const selectionState = contentState.getSelectionBefore();
        const block = contentState.getBlockForKey(selectionState.getAnchorKey());
        const styles = block.getInlineStyleAt(selectionState.getStartOffset());
        let href = "";
        styles.map((style) => {
            if (style.indexOf("LINK:") === 0) {
                href = style.substr(5);
            }
        });
        return <a target="_blank" href={href} style={{color: "#3498db"}}>{props.children}</a>;
    }

    handleDrop(selection, dataTransfer, isInternal) {
        const blockKey = dataTransfer.data.getData("block");
        if (blockKey) {
            const editorState = this.state.editorState;
            const contentState = editorState.getCurrentContent();
            const componentBlock = contentState.getBlockForKey(blockKey);
            let newEditorState = AtomicBlockUtils.moveAtomicBlock(this.state.editorState, componentBlock, selection, "replace");
            const newSelection = new SelectionState({
                anchorKey: blockKey,
                anchorOffset: 0,
                focusKey: blockKey,
                focusOffset: 0
            });

            newEditorState = EditorState.forceSelection(newEditorState, newSelection);
            this.setState({editorState: newEditorState});
            this.props.onChange(newEditorState);
            return "handled";
        }

        return;
    }

    toggleLinkStyle() {
        let editorState = this.state.editorState;
        const styles = editorState.getCurrentInlineStyle();
        let isLink = false;
        let linkStyle = null;
        styles.map((style) => {
            isLink = style.indexOf("LINK") >= 0;
            linkStyle = style;
        });
        if (isLink) {
            const contentState = editorState.getCurrentContent();
            const selectionState = editorState.getSelection();
            const block = contentState.getBlockForKey(selectionState.getAnchorKey());
            block.findStyleRanges(c => c.hasStyle(linkStyle), (start, end) => {
                const linkSelectionState = SelectionState.createEmpty(block.getKey()).merge({
                    anchorOffset: start,
                    focusOffset: end
                });
                editorState = EditorState.acceptSelection(editorState, linkSelectionState);
                this.setState({editorState}, () => {
                    this.executeInlineCommand(linkStyle);
                });
            });
        } else {
            this.setState({addingLink: true});
        }

    }

    render() {
        return (
            <div className="content-editor" ref={(container) => {
                this.container = container;
            }}>
                <div className="content-editor__editor" onClick={() => this.onFocus()}>
                    <Editor
                        ref={(editor) => {
                            this.editor = editor;
                        }}
                        handleDrop={(selection, dataTransfer, isInternal) => this.handleDrop(selection, dataTransfer, isInternal)}
                        placeholder={this.props.placeholder}
                        readOnly={this.state.readOnly}
                        editorState={this.state.editorState}
                        blockRendererFn={block => this.blockRenderer(block)}
                        blockStyleFn={block => this.blockStyleFn(block)}
                        handleKeyCommand={command => this.handleKeyCommand(command)}
                        keyBindingFn={e => this.keyBindingFn(e)}
                        customStyleMap={this.state.styleMap}
                        onChange={editorState => this.onChange(editorState)}
                        onTab={(e) => {
                            var maxDepth = 4;
                            const editorState = RichUtils.onTab(e, this.state.editorState, maxDepth);
                            this.setState({editorState});
                            this.props.onChange(editorState);
                        }}
                    />
                </div>

                {!this.state.buttons.hidden && <div className="content-editor__buttons" style={{
                    top: this.state.buttons.top,
                    left: this.state.buttons.left
                }}>
                    <div onClick={() => this.toggleButtons()} className="content-editor__button">{this.state.buttons.open && "x"}{!this.state.buttons.open && "+"}</div>
                    {
                        this.state.buttons.open && this.props.components.map(item => <div key={item.name} className="content-editor__button"
                            onClick={() => this.onComponentCreate(item)}><i className={item.icon}/></div>)
                    }
                </div>}

                {!this.state.bar.hidden && <div className="content-editor__bar" style={{
                    top: this.state.bar.top,
                    left: this.state.bar.left
                }}>
                    {this.state.addingLink
                        ? <div>
                            <Input className="content-editor__bar-input" value={this.state.addingLinkValue}
                                onChange={value => this.setState({addingLinkValue: value})}
                                onBlur={() => {
                                    this.setState({
                                        addingLink: false,
                                        addingLinkValue: null
                                    });
                                }}
                                onKeyPress={(e) => {
                                    if (e.key === "Enter") {
                                        this.setState({
                                            addingLink: false,
                                            addingLinkValue: null
                                        });
                                        this.executeInlineCommand("LINK", this.state.addingLinkValue);
                                    }
                                }
                                }/>
                        </div>
                        : <div>
                            {this.state.editorInlineCommands.map(
                                (command) => {
                                    if (command.style === "COLOR") {
                                        return <div className="content-editor__bar-button content-editor__bar-button--color">
                                            <div className="content-editor__bar-color-picker-label">A</div>
                                            <ColorPicker key={command.style}
                                                value={this.state.currentColor}
                                                onChange={value => this.executeInlineCommand("COLOR", value)}></ColorPicker>
                                        </div>;
                                    } else if (command.style === "BGCOLOR") {
                                        return <div className="content-editor__bar-button  content-editor__bar-button--bgcolor">
                                            <ColorPicker key={command.style}
                                                value={this.state.currentBgColor}
                                                onChange={value => this.executeInlineCommand("BGCOLOR", value)}></ColorPicker>
                                        </div>;
                                    } else if (command.style === "LINK") {
                                        return <Button
                                            key={command.style}
                                            className={"content-editor__bar-button " + (this.hasInlineStyle("LINK", true) ? "content-editor__bar-button--selected" : "")}
                                            icon={command.icon}
                                            onClick={() => {
                                                this.toggleLinkStyle();
                                            }}
                                            size="small"></Button>;
                                    }
                                    return <Button
                                        key={command.style}
                                        className={"content-editor__bar-button " + (this.hasInlineStyle(command.style) ? "content-editor__bar-button--selected" : "")}
                                        icon={command.icon}
                                        onClick={() => this.executeInlineCommand(command.style)}
                                        size="small"></Button>;
                                }
                            )}
                            {
                                this.state.editorBlockCommands.map(command =>
                                    <Button
                                        icon={command.icon}
                                        className={"content-editor__bar-button " + (this.state.bar.currentBlockStyle === command.style ? "content-editor__bar-button--selected" : "")}
                                        key={command.style} onClick={() => this.executeBlockCommand(command.style)}>
                                        {!command.icon && command.label}
                                    </Button>
                                )
                            }
                        </div>
                    }
                </div>
                }
            </div>
        );
    }
}

ContentEditor.propTypes = {
    content: PropTypes.array,
    placeholder: PropTypes.string,
    onChange: PropTypes.func,
    components: PropTypes.func
};

