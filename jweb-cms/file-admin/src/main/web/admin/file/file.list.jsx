import React from "react";
import {Link} from "react-router-dom";
import {Button, Cascader, DateFormatter, Form, Input, Message as alert, Message as notification, MessageBox, MessageBox as dialog, Pagination, Popover, Table, Upload} from "element-react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class FileList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            limitOptions: [20, 50, 100],
            statusOptions: [{
                label: i18n.t("file.statusActive"),
                value: "ACTIVE"
            }, {
                label: i18n.t("file.statusInactive"),
                value: "INACTIVE"
            }],
            directoryCascade: [],
            directorySelected: [],
            query: {
                keywords: "",
                directoryId: null,
                limit: 20,
                page: 1
            },
            data: {
                total: 0,
                page: 1,
                limit: 0,
                items: []
            },
            selected: [],
            uploading: false,
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("file.path"),
                    prop: "path",
                    render: function(data) {
                        return (/.*\.(jpg|jpeg|png|gif)/i).test(data.path)
                            ? <Popover placement="top-start" trigger="hover" content={(<img style={{
                                display: "block",
                                width: "100%"
                            }} src={"/admin" + data.path}/>)}>
                                <a target="_blank" href={"/admin" + data.path} title={data.path} style={{
                                    display: "block",
                                    "text-overflow": "ellipsis",
                                    overflow: "hidden"
                                }}> {data.path} </a>
                            </Popover>
                            : <a target="_blank" href={"/admin" + data.path}> {data.path} </a>;
                    }
                },
                {
                    label: i18n.t("file.fileName"),
                    prop: "fileName"
                },
                {
                    label: i18n.t("file.length"),
                    render: function(data) {
                        if (data.length) {
                            const length = Math.round(data.length * 100 / 1024) / 100;
                            return length + "K";
                        }
                        return "";
                    }
                },
                {
                    label: i18n.t("file.status"),
                    prop: "status"
                },
                {
                    label: i18n.t("file.createdTime"),
                    render: function(data) {
                        return <DateFormatter date={new Date(data.createdTime)}/>;
                    }
                },
                {
                    label: i18n.t("file.updatedTime"),
                    render: function(data) {
                        return <DateFormatter date={new Date(data.updatedTime)}/>;
                    }
                },
                {
                    label: i18n.t("file.action"),
                    fixed: "right",
                    width: 200,
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text" size="mini"><Link to={{pathname: "/admin/file/" + data.id + "/update"}}>{i18n.t("file.update")}</Link></Button>
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
        this.loadDirectory();
        this.find();
    }

    loadDirectory() {
        fetch("/admin/api/directory/tree", {method: "GET"}).then((cascade) => {
            this.trimCascade(cascade);
            this.setState({directoryCascade: cascade});
        });
    }

    trimCascade(cascade) {
        for (let i = 0; i < cascade.length; i += 1) {
            if (cascade[i].children.length === 0) {
                cascade[i].children = null;
            } else {
                this.trimCascade(cascade[i].children);
            }
        }
    }

    find() {
        fetch("/admin/api/file/find", {
            method: "PUT",
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    queryChange(key, value) {
        this.setState({query: Object.assign(this.state.query, {[key]: value})});
        if (key === "status") {
            this.find();
        }
    }

    statusChange(value) {
        const status = value ? value : null;
        this.setState({query: Object.assign(this.state.query, {status: status})});
        this.find();
    }

    select(selected) {
        this.setState({selected: selected});
    }

    selectDirectory(value) {
        let directoryId = null;
        if (value.length !== 0) {
            directoryId = value[value.length - 1];
        }
        this.setState({
            query: Object.assign(this.state.query, {directoryId: directoryId}),
            directorySelected: value
        });
        this.find();
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
        MessageBox.confirm(i18n.t("file.deleteFileTip"), i18n.t("file.deleteHint"), {type: "warning"}).then(() => {
            fetch("/admin/api/file/delete", {
                method: "POST",
                body: JSON.stringify({ids: [data.id]})
            }).then(() => {
                alert({
                    type: "success",
                    message: i18n.t("file.deleteSuccessTip")
                });
                this.find();
            });
        });
    }

    batchDelete() {
        MessageBox.confirm(i18n.t("file.deleteFileTip"), i18n.t("file.deleteHint"), {type: "warning"}).then(() => {
            const list = this.state.selected;
            if (list.length !== 0) {
                const ids = [];
                for (let i = 0; i < list.length; i += 1) {
                    ids.push(list[i].id);
                }
                fetch("/admin/api/file/delete", {
                    method: "POST",
                    body: JSON.stringify({ids: ids})
                }).then(() => {
                    alert({
                        type: "success",
                        message: i18n.t("file.deleteSuccessTip")
                    });
                    this.find();
                });
            }
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
                message: i18n.t("file.uploadSuccess"),
                type: "success"
            });
            this.find();
            this.setState({uploading: false});
        }


    }

    uploadError(error, file, fileList) {
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
            this.find();
            this.setState({uploading: false});
        }

        notification({
            title: "Upload " + file.name + " failed",
            message: file.message,
            type: "error"
        });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Form inline={true} model={this.state.query}>
                            <Form.Item>
                                <Cascader
                                    options={this.state.directoryCascade}
                                    value={this.state.directorySelected}
                                    placeholder={i18n.t("file.directory")}
                                    changeOnSelect={true}
                                    onChange={value => this.selectDirectory(value)}
                                    clearable={true}
                                    showAllLevels={false}
                                    props={{
                                        value: "id",
                                        label: "name"
                                    }}
                                />
                            </Form.Item>
                            <Form.Item> <Input value={this.state.query.keywords} onChange={value => this.queryChange("keywords", value)} icon="fa fa-search"/> </Form.Item>
                            <Form.Item> <Button nativeType="button" onClick={e => this.find(e)}>{i18n.t("file.search")}</Button> </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.selected.length > 0 ? <Button className="toolbar-button" onClick={() => this.batchDelete()} type="danger">{i18n.t("file.delete")}</Button> : ""}
                        <div style={{
                            display: "inline-block",
                            marginLeft: "10px"
                        }}>
                            <Upload
                                action="/admin/api/file/upload"
                                showFileList={false}
                                onProgress={() => this.setState({uploading: true})}
                                onSuccess={(response, file, fileList) => this.uploadSuccess(file, fileList)}
                                onError={(error, file, fileList) => this.uploadError(error, file, fileList)}
                                multiple={true}>
                                <Button className="toolbar-button" type="primary" loading={this.state.uploading}>{i18n.t("file.upload")}</Button>
                            </Upload>
                        </div>
                    </div>
                </div>
                <div className="body body--full body--file">
                    <Table style={{width: "100%"}} columns={this.state.columns} data={this.state.data.items} stripe={true}
                        onSelectChange={selected => this.select(selected)}/>
                </div>
                <div className="footer">
                    <Pagination layout="total,sizes,prev,pager,next,jumper" total={this.state.data.total}
                        pageSizes={this.state.limitOptions} pageSize={this.state.query.limit}
                        currentPage={this.state.query.page}
                        onSizeChange={(limit) => {
                            this.queryChange("page", 1);
                            this.queryChange("limit", limit);
                            this.find();
                        }}
                        onCurrentChange={(page) => {
                            this.queryChange("page", page);
                            this.find();
                        }}/>
                </div>
            </div>
        );
    }
}

FileList.propTypes = {
    match: PropTypes.object.match,
    history: PropTypes.object.history
};