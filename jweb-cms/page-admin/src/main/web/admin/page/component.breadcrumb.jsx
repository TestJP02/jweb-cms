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
                <div className="page-default-component__content">
                    <div className="page-default-component__line page-default-component__line--small  page-default-component__line--inline" style={{width: "30px"}}></div>
                    <div className="page-default-component__line page-default-component__line--small page-default-component__line--inline" style={{width: "30px"}}></div>
                    <div className="page-default-component__line page-default-component__line--small page-default-component__line--inline" style={{width: "30px"}}></div>
                </div>
            </div>
        );
    }
}

PageTitleComponent.defaultProps = {component: {name: i18n.t("page.defaultComponentName")}};

PageTitleComponent.propTypes = {component: {name: PropTypes.string}};