import React from "react";
import PropTypes from "prop-types";
import "./component.code.css";

export default class PageCodeComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            component: props.component,
            mode: props.mode
        };
    }

    componentWillReceiveProps(nextProps) {
        const mode = nextProps.mode;
        this.setState({mode});
    }

    onChange() {
        const html = this.state.editor.innerText;
        const component = this.state.component;

        if (html !== component.attributes.innerText) {
            component.attributes.innerText = html;
            this.setState({component});
            this.props.onChange(component);
        }
    }

    render() {
        if (this.state.mode === "edit") {
            return (
                <div className={(this.state.mode === "edit" ? "editing " : "page-component") + " page-component-code"}>
                    <pre contentEditable={true}
                        tabIndex={0}
                        ref={(ref) => {
                            if (!this.state.editor) {
                                this.setState({editor: ref});
                            }
                        }}
                        onBlur={() => this.onChange()}>
                        {this.state.component.attributes.innerText}
                    </pre>
                </div>
            );
        }
        return (
            <div className={(this.state.mode === "edit" ? "editing " : "page-component") + " page-component-code"}>
                <pre dangerouslySetInnerHTML={{__html: this.state.component.attributes.innerText}}>
                </pre>
            </div>
        );

    }
}

PageCodeComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};