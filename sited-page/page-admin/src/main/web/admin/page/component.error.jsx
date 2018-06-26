import React from "react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class PageDefaultComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {component: props.component};
    }

    render() {
        return (
            <div className="page-default-component page-default-component--error">
                <div className="page-default-component__name">{this.state.component.displayName + "(" + i18n.t("page.componentInvalid") + ")"}</div>
                <div className="page-default-component__content">
                    <div className="page-default-component__line" style={{width: "60%"}}></div>
                    <div className="page-default-component__line" style={{width: "80%"}}></div>
                    <div className="page-default-component__line" style={{width: "40%"}}></div>
                </div>
            </div>
        );
    }
}

PageDefaultComponent.defaultProps = {component: {name: i18n.t("page.defaultComponentName")}};

PageDefaultComponent.propTypes = {component: {name: PropTypes.string}};