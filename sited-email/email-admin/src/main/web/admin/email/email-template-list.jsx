import React from "react";
import {Link} from "react-router-dom";
import {Button, Form, Input, MessageBox as dialog, Notification as notification, Pagination, Table} from "element-react";

const i18n = window.i18n;
export default class EmailTemplateList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            limitOptions: [20, 50, 100],
            query: {
                query: null,
                status: "ACTIVE",
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
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("email.template"),
                    prop: "name",
                    width: 200
                },
                {
                    label: i18n.t("email.subject"),
                    prop: "subject"
                },
                {
                    label: i18n.t("email.createdTime"),
                    width: 200,
                    render: function(data) {
                        return (
                            <ElementUI.DateFormatter date={data.createdTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("email.updatedTime"),
                    width: 200,
                    render: function(data) {
                        return (
                            <ElementUI.DateFormatter date={data.updatedTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("email.action"),
                    width: 200,
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text" size="mini"><Link to={{pathname: "/admin/email/template/" + data.id + "/update"}}>{i18n.t("email.update")}</Link></Button>
                                <Button type="text" nativeType="button" onClick={e => this.delete(data, e)} size="mini">{i18n.t("email.delete")}</Button>
                            </span>
                        );
                    }.bind(this)
                }]
        };
    }

    componentWillMount() {
        this.find();
    }

    find(e) {
        if (e) {
            e.preventDefault();
        }
        fetch("/admin/api/email/template/find", {
            method: "PUT",
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    search(e) {
        e.preventDefault();
        this.find();
    }

    queryChange(key, value, find) {
        this.setState(
            {query: Object.assign(this.state.query, {[key]: value})}
        );
        if (find) {
            this.find();
        }
    }

    languageChange(value) {
        let language = value;
        if (!value) {
            language = null;
        }
        this.setState({query: Object.assign(this.state.query, {language: language})}, () => {
            this.find();
        });
    }

    select(list) {
        this.setState({selected: list});
    }

    batchSelect(list) {
        this.setState({selected: list});
    }

    delete(data, e) {
        e.preventDefault();
        dialog
            .confirm("Delete This Template?", "Hint", {type: "warning"})
            .then(() => {
                fetch("/admin/api/email/template/" + data.id, {method: "DELETE"})
                    .then(() => {
                        notification({
                            title: i18n.t("email.successTitle"),
                            type: "success",
                            message: i18n.t("email.deleteSuccessMessage")
                        });
                        this.find();
                    });
            })
            .catch(() => {
                notification({
                    title: i18n.t("email.errorTitle"),
                    type: "error",
                    message: i18n.t("email.deleteFailMessage")
                });
            });
    }

    batchDelete(e) {
        e.preventDefault();
        const list = this.state.selected;
        if (list.length === 0) {
            return;
        }
        const ids = [];
        for (let i = 0; i < list.length; i += 1) {
            ids.push(list[i].id);
        }
        dialog.confirm(i18n.t("email.batchDeleteHint"), i18n.t("email.hint"), {type: "warning"})
            .then(() => {
                fetch("/admin/api/email/template/delete", {
                    method: "POST",
                    body: JSON.stringify({ids: ids})
                }).then(() => {
                    notification({
                        title: i18n.t("email.successTitle"),
                        type: "success",
                        message: i18n.t("email.deleteSuccessMessage")
                    });
                    this.setState({selected: []});
                    this.find();
                });
            })
            .catch(() => {
                notification({
                    title: i18n.t("email.errorTitle"),
                    type: "error",
                    message: i18n.t("email.deleteFailMessage")
                });
            });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Form inline={true} model={this.state.query}>
                            <Form.Item>
                                <Input icon="fa fa-search" value={this.state.query.query} placeholder={i18n.t("email.queryPlaceHolder")} onChange={k => this.queryChange("query", k)}/>
                            </Form.Item>
                            <Form.Item>
                                <Button nativeType="button" onClick={e => this.search(e)}>{i18n.t("email.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.selected.length > 0
                            ? <Button type="danger" nativeType="button" onClick={e => this.batchDelete(e)}>{i18n.t("email.batchDelete")}</Button>
                            : ""}
                        <Button type="primary"><Link to={{pathname: "/admin/email/template/create"}}>{i18n.t("email.create")}</Link></Button>
                    </div>
                </div>
                <div className="body body--full">
                    <div className="page-container__body">
                        <Table
                            stripe={true}
                            style={{width: "100%"}}
                            columns={this.state.columns}
                            data={this.state.data.items}
                            onSelectChange={list => this.select(list)}
                            onSelectAll={dataList => this.batchSelect(dataList)}
                        />
                    </div>
                </div>
                <div className="footer">
                    <Pagination layout="total,sizes,prev,pager,next,jumper" total={this.state.data.total} pageSizes={this.state.limitOptions} pageSize={this.state.query.limit}
                        currentPage={this.state.query.page} onSizeChange={size => this.queryChange("limit", size, true)} onCurrentChange={currentPage => this.queryChange("page", currentPage, true)}/>
                </div>
            </div>
        );
    }
}