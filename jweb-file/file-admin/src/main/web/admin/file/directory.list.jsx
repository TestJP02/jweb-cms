import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, DateFormatter, Message as alert, Message as notification, MessageBox as dialog} from "element-react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class DirectoryList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            uploading: false,
            selected: [],
            data: {
                total: 0,
                page: 1,
                limit: 0,
                items: []
            },
            keyColumn: {
                label: i18n.t("file.path"),
                prop: "name"
            },
            columns: [
                {
                    label: i18n.t("file.status"),
                    prop: "status"
                },
                {
                    label: i18n.t("file.owner"),
                    prop: "owner"
                },
                {
                    label: i18n.t("file.createdTime"),
                    render: function (data) {
                        return <DateFormatter date={new Date(data.createdTime)}/>;
                    }
                },
                {
                    label: i18n.t("file.updatedTime"),
                    render: function (data) {
                        return <DateFormatter date={new Date(data.updatedTime)}/>;
                    }
                },
                {
                    label: i18n.t("file.action"),
                    fixed: "right",
                    width: 250,
                    render: function (data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text" size="mini"><Link to={{pathname: "/admin/file/directory/" + data.id + "/create"}}>{i18n.t("file.create")}</Link></Button>
                                <Button type="text" size="mini"><Link to={{pathname: "/admin/file/directory/" + data.id + "/update"}}>{i18n.t("file.update")}</Link></Button>
                                {data.status === "INACTIVE" ? <Button onClick={e => this.revert(data, e)} type="text" size="mini">{i18n.t("file.revert")}</Button>
                                    : <Button onClick={e => this.delete(data, e)} type="text" size="mini">{i18n.t("file.delete")}</Button>}
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

    synchronize() {
        fetch("/admin/api/file/synchronize", {method: "GET"})
            .then(() => {
                alert({
                    type: "info",
                    message: i18n.t("file.synchronizeSuccess")
                });
                this.find();
            });
    }

    uploadSuccess(file, fileList) {
        let processed = true;
        for (let i = 0; i < fileList.length; i += 1) {
            if (file.name === fileList[i].name) {
                fileList[i].processed = true;
            }

            if (!fileList[i].processed) {
                processed = false;
            }
        }

        if (processed) {
            notification({
                title: i18n.t("file.uploadSuccess"),
                type: "success"
            });
            this.setState({uploading: false});
        }
    }

    find() {
        fetch("/admin/api/directory/first-two-levels", {method: "GET"}).then((response) => {
            this.setState({data: response});
        });
    }

    revert(data) {
        dialog.confirm(i18n.t("file.fileRevertTip"))
            .then(() => {
                fetch("/admin/api/file/" + data.id + "/revert", {method: "PUT"}).then(() => {
                    this.find();
                });
            })
            .catch(() => {
                alert({
                    type: "info",
                    message: i18n.t("file.revertCancelTip")
                });
            });
    }

    delete(data) {
        fetch("/admin/api/directory/" + data.id, {method: "DELETE"}).then(() => {
            this.find();
        });
    }

    loadChildren(data) {
        fetch("/admin/api/directory/" + data.id + "/sub-tree", {method: "GET"}).then((response) => {
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

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin">{i18n.t("file.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{i18n.t("file.directoryList")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button className="toolbar-button" type="primary" onClick={() => this.synchronize()}>{i18n.t("file.synchronizeFile")}</Button>
                    </div>
                </div>
                <div className="body body--full">
                    <ElementUI.TreeTable loadChildren={data => this.loadChildren(data)} treeData={this.state.data} keyColumn={this.state.keyColumn} treeColumns={this.state.columns}/>
                </div>
            </div>
        );
    }
}

FileList.propTypes = {
    match: PropTypes.object.match,
    history: PropTypes.object.history
};