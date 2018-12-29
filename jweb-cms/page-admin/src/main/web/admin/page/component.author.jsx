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
            <div className="page-author page-default-component">
                <div className="page-default-component__name">{this.state.component.name}</div>

                <div className="page-author__head"></div>
                <div className="page-default-component__content">
                    <div className="page-default-component__line page-default-component__line--inline" style={{width: "30%"}}></div>
                    <br/>
                    <div className="page-default-component__line page-default-component__line--inline" style={{width: "20%"}}></div>
                    <div className="page-default-component__line page-default-component__line--inline" style={{width: "20%"}}></div>
                </div>
            </div>
        );
    }
}

PageDefaultComponent.defaultProps = {component: {name: i18n.t("page.defaultComponentName")}};

PageDefaultComponent.propTypes = {component: {name: PropTypes.string}};