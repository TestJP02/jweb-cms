import React from "react";
import {Button, Form, Input, Message as notification, MessageBox, Pagination, Table} from "element-react";
import {Link} from "react-router-dom";

const i18n = window.i18n;
export default class VariableList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            query: {
                name: null,
                page: 1,
                limit: 10
            },
            data: {
                total: 0,
                page: 1,
                limit: 0,
                items: []
            },
            limitOptions: [10, 20, 50],
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("page.name"),
                    prop: "name"
                },
                {
                    label: i18n.t("page.status"),
                    prop: "status"
                },
                {
                    label: i18n.t("page.fieldNum"),
                    prop: "fieldNum"
                },
                {
                    label: i18n.t("page.createdTime"),
                    render: function(data) {
                        return (
                            <ElementUI.DateFormatter date={data.createdTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("page.updatedTime"),
                    render: function(data) {
                        return (
                            <ElementUI.DateFormatter date={data.updatedTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("page.action"),
                    fixed: "right",
                    width: 200,
                    render: function(current) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text">
                                    <Link to={{pathname: "/admin/page/variable/" + current.id + "/update"}}> {i18n.t("page.update")} </Link>
                                </Button>
                                <Button type="text" onClick={e => this.delete(current, e)}>
                                    {i18n.t("page.delete")}
                                </Button>
                            </span>
                        );
                    }.bind(this)
                }
            ],
            selected: []
        };
    }

    componentWillMount() {
        this.find();
    }

    find() {
        fetch("/admin/api/page/variable/find", {
            method: "PUT",
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    queryChange(key, value) {
        this.setState(
            {query: Object.assign(this.state.query, {[key]: value})}
        );
    }

    statusChange(value) {
        let status = value;
        if (!value) {
            status = null;
        }
        this.setState({query: Object.assign(this.state.query, {status: status})}, () => {
            this.find();
        });
    }

    select(item, checked) {
        const list = this.state.selected;
        if (checked) {
            list.push(item);
        } else {
            for (let i = 0; i < list.length; i += 1) {
                if (list[i].id === item.id) {
                    list.splice(i, 1);
                }
            }

        }
        this.setState({selected: list});
    }

    batchSelect(list, checked) {
        if (checked) {
            this.setState({selected: list});
        } else {
            this.setState({selected: []});
        }
    }

    delete(data, e) {
        e.preventDefault();
        MessageBox.confirm(i18n.t("page.deleteVariableTip"), i18n.t("page.deleteHint"), {type: "warning"}).then(() => {
            fetch("/admin/api/page/variable/batch-delete", {
                method: "PUT",
                body: JSON.stringify({ids: [data.id]})
            }).then(() => {
                notification({
                    title: i18n.t("page.successTitle"),
                    type: "success",
                    message: i18n.t("page.deleteSuccessMessage")
                });
                this.find();
            });
        });
    }

    batchDelete() {
        MessageBox.confirm(i18n.t("page.deleteVariableTip"), i18n.t("page.deleteHint"), {type: "warning"}).then(() => {
            const list = this.state.selected,
                ids = [];
            if (list.length === 0) {
                return;
            }
            for (let i = 0; i < list.length; i += 1) {
                ids.push(list[i].id);
            }
            fetch("/admin/api/page/variable/batch-delete", {
                method: "PUT",
                body: JSON.stringify({ids: ids})
            }).then(() => {
                notification({
                    title: i18n.t("page.successTitle"),
                    type: "success",
                    message: i18n.t("page.deleteSuccessMessage")
                });
                this.find();
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
                                <Input icon="fa fa-search" placeholder={i18n.t("page.namePlaceHolder")} value={this.state.query.name}
                                    onChange={value => this.queryChange("name", value)}/>
                            </Form.Item>
                            <Form.Item>
                                <Button onClick={() => this.find()}>{i18n.t("page.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="danger" style={this.state.selected.length > 0 ? {} : {"display": "none"}} onClick={() => this.batchDelete()}>{i18n.t("page.batchDelete")}</Button>
                        <Button type="primary">
                            <Link to={{pathname: "/admin/page/variable/create"}}>{i18n.t("page.create")}</Link>
                        </Button>
                    </div>
                </div>
                <div className="body body--full">
                    <Table
                        stripe={true}
                        style={{width: "100%"}}
                        columns={this.state.columns}
                        data={this.state.data.items}
                        onSelectChange={(item, checked) => this.select(item, checked)}
                        onSelectAll={(list, checked) => this.batchSelect(list, checked)}
                    />
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