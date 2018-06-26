import React from "react";
import PropTypes from "prop-types";
import {Input} from "element-react";

import "./component.wrapper.css";

const i18n = window.i18n;
export default class ComponentWrapper extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            component: {
                id: props.blockProps.value.key,
                name: props.blockProps.value.type,
                attributes: props.blockProps.value.data,
                text: props.blockProps.value.text,
                inlineStyles: props.blockProps.value.inlineStyles
            },
            mode: props.blockProps.mode,
            focused: false,
            dragging: false
        };
    }

    onChange(component) {
        this.props.blockProps.onChange(this.props.block, {
            key: component.id,
            type: component.name,
            data: component.attributes,
            text: component.text,
            inlineStyles: component.inlineStyles
        });
        this.props.blockProps.onBlur();
        this.setState({mode: "preview"});
    }

    onFocus() {
        this.props.blockProps.onFocus();
        this.setState({focused: true});
    }

    onDragStart(event) {
        window.console.log("DRAG START");
        event.dataTransfer.dropEffect = "move";
        event.dataTransfer.setData("block", this.props.block.key);
        this.props.blockProps.onBlur();
        this.setState({
            mode: "preview",
            dragging: true
        });
    }

    onDoubleClick() {
        this.setState({
            mode: "edit",
            focused: true
        });
    }

    onBlur() {
        this.props.blockProps.onBlur();
        this.setState({
            mode: "preview",
            focused: false
        });
    }

    onDelete() {
        this.props.blockProps.onDelete(this.props.block);
    }

    onTitleChange(title) {
        const component = this.state.component;
        component.attributes.title = title;
        this.setState({component});

        this.props.blockProps.onChange(this.props.block, {
            key: component.id,
            type: component.name,
            data: component.attributes,
            text: component.text,
            inlineStyles: component.inlineStyles
        });
    }

    handleWrapperRef(ref) {
        if (!ref || !ref.querySelector(".page-component")) {
            return;
        }
        const wrapperHeight = this.state.wrapperHeight;
        const componentHeight = ref.querySelector(".page-component").clientHeight;
        if (!wrapperHeight || wrapperHeight !== componentHeight) {
            this.setState({wrapperHeight: componentHeight});
        }
    }

    deleteBefore() {
        this.props.blockProps.onDeleteBefore(this.props.block);
    }

    render() {
        return (
            <div className="component-wrapper_outer">
                <ComponentWrapperDecorator
                    height={this.state.wrapperHeight}
                    preDecorator={true}
                    onFocus={() => {
                        this.onFocus();
                    }}
                    onDelete={() => this.deleteBefore()}
                />
                <div className="component-wrapper" tabIndex={0}
                    onFocus={() => this.onFocus()}
                    onDoubleClick={() => this.onDoubleClick()}
                    onDragStart={e => this.onDragStart(e)}
                    ref={ref => this.handleWrapperRef(ref)}>
                    {
                        React.createElement(this.props.blockProps.component, {
                            component: this.state.component,
                            mode: this.state.mode,
                            onChange: component => this.onChange(component)
                        })
                    }
                    {this.state.focused &&
                        <Input type="text"
                            value={this.state.component.attributes.title}
                            onChange={value => this.onTitleChange(value)}
                            placeholder={i18n.t("page.titlePlaceholder")}
                            className="component-wrapper__title" />
                    }
                    {!this.state.focused && <p className="component-wrapper__title">{this.state.component.attributes.title}</p>}
                </div>
                <ComponentWrapperDecorator
                    height={this.state.wrapperHeight}
                    onFocus={() => {
                        this.onFocus();
                    }}
                    onDelete={() => this.onDelete()}
                />
                {
                    this.state.focused && !this.state.dragging && <div className="component-wrapper__mask" onClick={() => this.onBlur()}></div>
                }
            </div>
        );
    }
}

function ComponentWrapperDecorator(props) {
    return <div className="component-wrapper_decorator"
        contentEditable="true"
        tabIndex={0}
        style={{
            "line-height": `${props.height}px`,
            "height": `${props.height}px`,
            "font-size": `${props.height}px`
        }}
        onFocus={() => props.onFocus()}
        onKeyDown={(e) => {
            window.e = e;
            if (e.key === "Backspace") {
                props.onDelete();
            }
            e.preventDefault();
            e.stopPropagation();
            return false;
        }}
    >
    </div>;
}

ComponentWrapper.propTypes = {
    blockProps: PropTypes.object,
    block: PropTypes.object,
    components: PropTypes.func
};

ComponentWrapperDecorator.propTypes = {
    height: PropTypes.number,
    onFocus: PropTypes.func,
    onDelete: PropTypes.func
};
