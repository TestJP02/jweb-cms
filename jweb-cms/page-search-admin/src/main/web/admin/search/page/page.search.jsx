import React from "react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Message as notification} from "element-react";

const i18n = window.i18n;
export default class PageIndex extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    index() {
        fetch("/admin/api/page/search/full-index", {
            method: "PUT",
            body: JSON.stringify(this.state.form)
        }).then((response) => {
            notification({
                title: "Success",
                message: i18n.t("search.indexSucceed"),
                type: "success"
            });
            this.props.history.push("/admin/page/list");
        });
    }


    render() {
        return (
            <div className="page">
                <div className="header">
                    <Breadcrumb separator="/">
                        <Breadcrumb.Item><Link to="/admin/">{i18n.t("page.home")}</Link></Breadcrumb.Item>
                        <Breadcrumb.Item><Link
                            to="/admin/page/list">{i18n.t("page.list")}</Link></Breadcrumb.Item>
                        <Breadcrumb.Item>{i18n.t("search.index")}</Breadcrumb.Item>
                    </Breadcrumb>
                </div>
                <div className="toolbar">
                    <div className="toolbar-buttons">
                        <Button className="toolbar-button" type="primary" nativeType="button"
                            onClick={() => this.index()}>{i18n.t("search.index")}</Button>
                    </div>
                </div>
            </div>);
    }
}

PageIndex.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};