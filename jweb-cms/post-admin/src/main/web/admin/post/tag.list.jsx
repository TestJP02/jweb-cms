import React from "react";
import {Link} from "react-router-dom";
import {Button, Form, Input, Message as notification, Pagination, Table} from "element-react";

const i18n = window.i18n;
export default class Tag extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            query: {
                displayName: null,
                type: null,
                page: 1,
                limit: 20
            },
            data: {
                total: 0,
                page: 1,
                limit: 20,
                items: []
            },
            limitOptions: [20, 50, 100],
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("post.tagName"),
                    prop: "displayName"
                },
                {
                    label: i18n.t("post.tagPath"),
                    prop: "path"
                },
                {
                    label: i18n.t("post.tags"),
                    render: function(data) {
                        if (data.tags) {
                            return data.tags.join(";");
                        }
                        return "";
                    }
                },
                {
                    label: i18n.t("post.alias"),
                    prop: "alias"
                },
                {
                    label: i18n.t("post.totalTagged"),
                    prop: "totalTagged"
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
                    render: function(current) {
                        return (
                            <span className="el-table__actions">
                                {!current.readOnly &&
                                <Button type="text">
                                    <Link
                                        to={{pathname: "/admin/post/tag/" + current.id + "/update"}}> {i18n.t("post.update")} </Link>
                                </Button>
                                }

                                {!current.readOnly &&
                                <Button type="text" onClick={e => this.delete(current, e)}>
                                    {i18n.t("post.delete")}
                                </Button>
                                }
                            </span>
                        );
                    }.bind(this)
                }
            ],
            selected: []
        }
        ;
    }

    componentWillMount() {
        this.find();
    }

    find() {
        fetch("/admin/api/post/tag/find", {
            method: "PUT",
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
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
        fetch("/admin/api/post/tag/batch-delete", {
            method: "POST",
            body: JSON.stringify({ids: [data.id]})
        }).then(() => {
            notification({
                title: i18n.t("post.successTitle"),
                type: "success",
                message: i18n.t("post.deleteSuccessMessage")
            });
            this.setState({selected: []});
            this.find();
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
        fetch("/admin/api/post/tag/batch-delete", {
            method: "POST",
            body: JSON.stringify({ids: ids})
        }).then(() => {
            notification({
                title: i18n.t("post.successTitle"),
                type: "success",
                message: i18n.t("post.deleteSuccessMessage")
            });
            this.setState({selected: []});
            this.find();
        });
    }

    queryChange(key, value) {
        this.setState(
            {query: Object.assign(this.state.query, {[key]: value})}
        );
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Form inline={true} model={this.state.query}>
                            <Form.Item>
                                <Input icon="fa fa-search" value={this.state.query.displayName} placeholder={i18n.t("post.pathPlaceHolder")}
                                    onChange={value => this.queryChange("displayName", value)}/>
                            </Form.Item>
                            <Form.Item>
                                <Button nativeType="button"
                                    onClick={e => this.find(e)}>{i18n.t("post.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.selected.length > 0 ? <Button type="danger" onClick={e => this.batchDelete(e)}
                            nativeType="button">{i18n.t("post.batchDelete")}</Button> : <span></span>}
                        <Link to="/admin/post/tag/create">
                            <Button type="primary" nativeType="button">
                                {i18n.t("post.create")}
                            </Button>
                        </Link>
                    </div>
                </div>
                <div className="body body--full">
                    <Table
                        stripe={true}
                        style={{width: "100%"}}
                        columns={this.state.columns}
                        data={this.state.data.items}
                        onSelectChange={item => this.select(item)}
                        onSelectAll={list => this.batchSelect(list)}
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