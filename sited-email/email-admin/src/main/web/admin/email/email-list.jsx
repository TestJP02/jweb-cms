import React from "react";
import {Link} from "react-router-dom";
import {Button, Form, Input, Pagination, Select, Table} from "element-react";

const i18n = window.i18n;
export default class EmailList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            limitOptions: [20, 50, 100],
            statusOptions: [{
                value: "SUCCESS",
                label: i18n.t("email.statusSuccess")
            }, {
                value: "FAILED",
                label: i18n.t("email.statusFail")
            }],
            query: {
                query: null,
                status: null,
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
                {
                    label: i18n.t("email.fromUser"),
                    prop: "from",
                    width: 200
                },
                {
                    label: i18n.t("email.to"),
                    prop: "to",
                    width: 200
                },
                {
                    label: i18n.t("email.subject"),
                    prop: "subject"
                },
                {
                    label: i18n.t("email.status"),
                    prop: "status"
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
                    label: i18n.t("email.action"),
                    width: 200,
                    render: function(data) {
                        return (
                            <span className="el-table__actions">
                                <Button type="text" size="mini"><Link to={{pathname: "/admin/email/" + data.id + "/view"}}>{i18n.t("email.view")}</Link></Button>
                            </span>
                        );
                    }
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
        fetch("/admin/api/email/find", {
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

    statusChange(value) {
        let status = value;
        if (!value) {
            status = null;
        }
        this.setState({query: Object.assign(this.state.query, {status: status})}, () => {
            this.find();
        });
    }

    queryChange(key, value, find) {
        if (find) {
            this.setState({query: Object.assign(this.state.query, {[key]: value})}, () => {
                this.find();
            });
        } else {
            this.setState({query: Object.assign(this.state.query, {[key]: value})});
        }
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

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Form inline={true} model={this.state.query}>
                            <Form.Item>
                                <Select placeholder={i18n.t("email.statusPlaceHolder")} value={this.state.query.status} onChange={k => this.statusChange(k)} clearable={true}>{
                                    this.state.statusOptions.map(el => <Select.Option key={el.value} label={el.label} value={el.value}/>)
                                }</Select>
                            </Form.Item>
                            <Form.Item>
                                <Input icon="fa fa-search" value={this.state.query.query} placeholder={i18n.t("email.queryPlaceHolder")} onChange={k => this.queryChange("query", k)}/>
                            </Form.Item>
                            <Form.Item>
                                <Button nativeType="button" onClick={e => this.search(e)}>{i18n.t("email.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                    </div>
                </div>
                <div className="body body--full">
                    <div className="page-container__body">
                        <Table
                            stripe={true}
                            style={{width: "100%"}}
                            columns={this.state.columns}
                            data={this.state.data.items}
                            onSelectChange={(dataItem, checked) => this.select(dataItem.id, checked)}
                            onSelectAll={(dataList, checked) => this.batchSelect(dataList, checked)}
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