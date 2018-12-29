import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Message as notification} from "element-react";

const i18n = window.i18n;
export default class CategoryList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            keyColumn: {
                label: i18n.t("post.name"),
                prop: "displayName"
            },
            columns: [
                {
                    label: i18n.t("post.path"),
                    prop: "path"
                },
                {
                    label: i18n.t("post.displayName"),
                    prop: "displayName"
                },
                {
                    label: i18n.t("post.status"),
                    prop: "status"
                },
                {
                    label: i18n.t("post.createdTime"),
                    render: function(data) {
                        return (
                            <ElementUI.DateFormatter date={data.createdTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("post.updatedTime"),
                    render: function(data) {
                        return (
                            <ElementUI.DateFormatter date={data.updatedTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("post.action"),
                    fixed: "right",
                    width: 200,
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text">
                                    <Link to={{pathname: "/admin/post/category/create/parent/" + data.id}}>{i18n.t("post.create")}</Link>
                                </Button>
                                <Button type="text">
                                    <Link to={"/admin/post/category/" + data.id + "/update"}>{i18n.t("post.update")}</Link>
                                </Button>
                                {data.parentId &&
                                <Button onClick={e => this.delete(data, e)} type="text">
                                    {i18n.t("post.delete")}
                                </Button>}
                            </span>
                        );
                    }.bind(this)
                }
            ]
        };
    }

    componentWillMount() {
        this.find();
    }

    find(e) {
        if (e) {
            e.preventDefault();
        }
        fetch("/admin/api/post/category/first-two-levels", {method: "GET"}).then((response) => {
            window.console.log(response);
            this.setState({data: response});
        });
    }

    loadChildren(data) {
        fetch("/admin/api/post/category/" + data.id + "/sub-tree", {
            method: "PUT",
            body: JSON.stringify({})
        }).then((response) => {
            const list = this.state.data;
            list.forEach((directory) => {
                if (directory.children) {
                    directory.children.forEach((child) => {
                        if (child.id === data.id) {
                            child.children = response;
                        }
                    });
                }
            });
            this.setState({data: list});
        });
    }

    delete(data, e) {
        e.preventDefault();
        fetch("/admin/api/post/category/" + data.id, {method: "DELETE"}).then(() => {
            notification({
                title: i18n.t("post.successTitle"),
                type: "success",
                message: i18n.t("post.deleteSuccessMessage")
            });
            this.find();
        });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin/">{i18n.t("post.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{i18n.t("post.categoryList")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                    </div>
                </div>
                <div className="body body--full">
                    <div className="page-container__body">
                        <ElementUI.TreeTable treeData={this.state.data} keyColumn={this.state.keyColumn} treeColumns={this.state.columns}
                            loadChildren={data => this.loadChildren(data)}/>
                    </div>
                </div>
            </div>
        );
    }
}