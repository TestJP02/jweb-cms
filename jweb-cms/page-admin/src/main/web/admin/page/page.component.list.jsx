import React from "react";
import {Link} from "react-router-dom";
import {Button, Form, Input, Message as notification, Pagination, Select, Table} from "element-react";

const i18n = window.i18n;
export default class ComponentList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            query: {
                query: null,
                status: "ACTIVE",
                limit: 10,
                page: 1
            },
            data: {
                total: 0,
                page: 1,
                limit: 0,
                items: []
            },
            limitOptions: [10, 20, 50],
            statusOptions: [
                {
                    label: i18n.t("page.statusActive"),
                    value: "ACTIVE"
                },
                {
                    label: i18n.t("page.statusInactive"),
                    value: "INACTIVE"
                }
            ],
            selected: [],
            columns: [
                {
                    label: i18n.t("page.displayName"),
                    prop: "displayName"
                },
                {
                    label: i18n.t("page.status"),
                    prop: "status"
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
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                {data.status === "ACTIVE"
                                    ? <span>
                                        <Button type="text">
                                            <Link to={"/admin/page/component/" + data.id + "/update"}>{i18n.t("page.update")}</Link>
                                        </Button>
                                        <Button type="text" onClick={e => this.delete(data, e)}>
                                            {i18n.t("page.delete")}
                                        </Button>
                                    </span>
                                    : ""}
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
        fetch("/admin/api/page/saved-component/find", {
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

    changeStatus(value) {
        let status = value;
        if (!value) {
            status = null;
        }
        this.setState({query: Object.assign(this.state.query, {status: status})}, () => {
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

        fetch("/admin/api/page/saved-component/batch-delete", {
            method: "POST",
            body: JSON.stringify({ids: [data.id]})
        }).then(() => {
            this.find();
            notification({
                title: i18n.t("page.successTitle"),
                type: "success",
                message: i18n.t("page.deleteSuccessMessage")
            });
        });
    }

    batchDelete(e) {
        e.preventDefault();
        const list = this.state.selected;
        if (list.length === 0) {
            return;
        }
        const pages = [];
        for (let i = 0; i < list.length; i += 1) {
            pages.push({
                id: list[i].id,
                status: list[i].status
            });
        }
        fetch("/admin/api/page/batch-delete", {
            method: "POST",
            body: JSON.stringify({pages: pages})
        }).then(() => {
            notification({
                title: i18n.t("page.successTitle"),
                type: "success",
                message: i18n.t("page.deleteSuccessMessage")
            });
            this.setState({selected: []});
            this.find();
        });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Form inline={true} model={this.state.query}>
                            <Form.Item>
                                <Select value={this.state.query.status}
                                    onChange={value => this.changeStatus(value)}
                                    placeholder={i18n.t("page.statusAll")}
                                    clearable={true}>
                                    {
                                        this.state.statusOptions.map(el => <Select.Option key={el.value}
                                            label={el.label} value={el.value}/>)
                                    }
                                </Select>
                            </Form.Item>
                            <Form.Item>
                                <Input icon="fa fa-search" value={this.state.query.query} placeholder={i18n.t("page.queryPlaceHolder")}
                                    onChange={value => this.queryChange("query", value)}/>
                            </Form.Item>
                            <Form.Item>
                                <Button nativeType="button"
                                    onClick={e => this.find(e)}>{i18n.t("page.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.selected.length > 0 ? <Button type="danger" onClick={e => this.batchDelete(e)} nativeType="button">{i18n.t("page.batchDelete")}</Button> : ""}
                        <Link to={{pathname: "/admin/page/component/create/"}}>
                            <Button type="primary" nativeType="button">
                                {i18n.t("page.create")}
                            </Button>
                        </Link>
                    </div>
                </div>
                <div className="body body--full">
                    <div className="page-container__body">
                        <Table
                            stripe={true}
                            style={{width: "100%"}}
                            columns={this.state.columns}
                            data={this.state.data.items}
                            onSelectChange={item => this.select(item)}
                            onSelectAll={list => this.batchSelect(list)}
                        />
                    </div>
                </div>
                <div className="footer">
                    <Pagination layout="total,sizes,prev,pager,next,jumper" total={this.state.data.total}
                        pageSizes={this.limitOptions} pageSize={this.state.query.limit}
                        currentPage={this.state.query.page} onSizeChange={limit => this.queryChange("limit", limit)}
                        onCurrentChange={page => this.queryChange("page", page)}/>
                </div>
            </div>
        );
    }
}