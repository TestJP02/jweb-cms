import React from "react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class PageCategoryTitleComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {component: props.component};
    }

    render() {
        return (
            <div className="page-default-component">
                <div className="page-default-component__content">
                    <div className="page-default-component__line page-default-component__line--h1" style={{width: "50%"}}></div>
                    <div className="page-default-component__line" style={{width: "30%"}}></div>
                </div>
            </div>
        );
    }
}

PageCategoryTitleComponent.defaultProps = {component: {name: i18n.t("page.defaultComponentName")}};

PageCategoryTitleComponent.propTypes = {component: {name: PropTypes.string}};