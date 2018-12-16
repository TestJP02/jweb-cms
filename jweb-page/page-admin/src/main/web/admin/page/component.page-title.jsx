import React from "react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class PageTitleComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {component: props.component};
    }

    render() {
        return (
            <div className="page-default-component">
                <div className="page-default-component__name">{this.state.component.displayName}</div>
                <div className="page-default-component__content">
                    <div className="page-default-component__line page-default-component__line--h1" style={{width: "80%"}}></div>
                    <div className="page-default-component__line page-default-component__line--small" style={{width: "40%"}}></div>
                </div>
            </div>
        );
    }
}

PageTitleComponent.defaultProps = {component: {name: i18n.t("page.defaultComponentName")}};

PageTitleComponent.propTypes = {component: {name: PropTypes.string}};