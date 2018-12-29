import React from "react";
import {Link} from "react-router-dom";
import {Button, Cascader, Form, Input, Message as notification, MessageBox as dialog, Pagination, Select, Table} from "element-react";

const i18n = window.i18n;
export default class PostList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            siteURL: null,
            statusOptions: [
                {
                    label: i18n.t("post.statusActive"),
                    value: "ACTIVE"
                },
                {
                    label: i18n.t("post.statusInactive"),
                    value: "INACTIVE"
                },
                {
                    label: i18n.t("post.statusDraft"),
                    value: "DRAFT"
                }
            ],
            limitOptions: [20, 50, 100],
            query: {
                query: null,
                status: null,
                categoryId: null,
                limit: 20,
                page: 1
            },
            data: {
                total: 0,
                page: 1,
                limit: 0,
                items: []
            },
            categoryTree: [],
            selected: [],
            columns: [
                {type: "selection"},
                {
                    label: i18n.t("post.path"),
                    prop: "path",
                    render(data) {
                        if (data.status === "DRAFT") {
                            return <a target="_blank" href={data.path + "?draft=true"}>*{data.path}</a>;
                        } else if (data.status === "ACTIVE") {
                            return <a target="_blank" href={data.path}>{data.path}</a>;
                        }
                        return data.path;
                    }
                },
                {
                    label: i18n.t("post.title"),
                    prop: "title"
                },
                {
                    label: i18n.t("post.status"),
                    render(data) {
                        return (
                            <span>
                                {data.status === "ACTIVE" && i18n.t("post.statusActive")}
                                {data.status === "INACTIVE" && i18n.t("post.statusInactive")}
                                {data.status === "AUDITING" && i18n.t("post.statusAuditing")}
                                {data.status === "DRAFT" && i18n.t("post.statusDraft")}
                            </span>
                        );
                    }
                },
                {
                    label: i18n.t("post.topFixed"),
                    render(data) {
                        return data.topFixed ? i18n.t("post.yes") : i18n.t("post.no");
                    }
                },
                {
                    label: i18n.t("post.totalVisited"),
                    prop: "totalVisited"
                },
                {
                    label: i18n.t("post.totalCommented"),
                    render(data) {
                        if (data.totalCommented && data.totalCommented > 0) {
                            return <Link to={"/admin/post/" + data.id + "/comments"}>{data.totalCommented}</Link>;
                        }
                    }
                },
                {
                    label: i18n.t("post.updatedTime"),
                    render(data) {
                        return (
                            <ElementUI.DateFormatter date={data.updatedTime}/>
                        );
                    }
                },
                {
                    label: i18n.t("post.action"),
                    fixed: "right",
                    width: 200,
                    render: (data) => {
                        if (data.status === "DRAFT") {
                            return (
                                <span className="el-table__actions">
                                    <Button onClick={e => this.publish(data, e)} type="text">{i18n.t("post.publish")}</Button>
                                    <Button type="text">
                                        <Link to={"/admin/post/draft/" + data.id}>{i18n.t("post.update")}</Link>
                                    </Button>
                                    <Button onClick={e => this.delete(data, e)} type="text">{i18n.t("post.delete")}</Button>
                                </span>
                            );
                        } else if (data.status === "ACTIVE") {
                            return (
                                <span className="el-table__actions">
                                    <Button type="text">
                                        <Link to={"/admin/post/" + data.id + "/draft"}>{i18n.t("post.update")}</Link>
                                    </Button>
                                    <Button onClick={e => this.delete(data, e)} type="text">{i18n.t("post.delete")}</Button>
                                </span>
                            );
                        } else if (data.status === "INACTIVE") {
                            return (
                                <span className="el-table__actions">
                                    <Button onClick={e => this.revert(data, e)} type="text">{i18n.t("post.revert")}</Button>
                                    <Button onClick={e => this.delete(data, e)} type="text">{i18n.t("post.delete")}</Button>
                                </span>
                            );
                        }
                    }
                }
            ],
            categoryCascade: [],
            categorySelected: [],
            i18nEnabled: false
        };
    }

    componentWillMount() {
        this.find();
        this.loadCategory();
        this.siteURL();
    }

    siteURL() {
        fetch("/admin/api/post/site/url", {method: "GET"}).then((response) => {
            this.setState({siteURL: response.siteURL === null ? "" : response.siteURL});
        });
    }

    loadCategory() {
        fetch("/admin/api/post/category/tree", {
            method: "PUT",
            body: JSON.stringify({})
        }).then((cascade) => {
            this.trimCascade(cascade);
            this.setState({categoryCascade: cascade});
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

    find(e) {
        if (e) {
            e.preventDefault();
        }
        fetch("/admin/api/post/find", {
            method: "PUT",
            body: JSON.stringify(this.state.query)
        }).then((response) => {
            this.setState({data: response});
        });
    }

    findQuery(query) {
        fetch("/admin/api/post/find", {
            method: "PUT",
            body: JSON.stringify(query)
        }).then((response) => {
            this.setState({
                data: response,
                query: query
            });
        });
    }

    queryChange(key, value) {
        this.setState(
            {query: Object.assign(this.state.query, {[key]: value ? value : null})}
        );
    }

    queryChangeAndFind(key, value) {
        this.setState(
            {query: Object.assign(this.state.query, {[key]: value ? value : null})}
            , () => {
                this.find();
            });
    }

    selectCategory(value) {
        if (value.length === 0) {
            this.setState({
                query: Object.assign(this.state.query, {categoryId: null}),
                categorySelected: value
            }, () => {
                this.find();
            });
        } else {
            this.setState({
                query: Object.assign(this.state.query, {categoryId: value[value.length - 1]}),
                categorySelected: value
            }, () => {
                this.find();
            });
        }
    }

    select(list) {
        this.setState({selected: list});
    }

    batchSelect(list) {
        this.setState({selected: list});
    }

    delete(data, e) {
        e.preventDefault();

        fetch("/admin/api/post/batch-delete", {
            method: "POST",
            body: JSON.stringify({
                posts: [{
                    id: data.id,
                    status: data.status
                }
                ]
            })
        }).then(() => {
            this.find();
            notification({
                title: i18n.t("post.successTitle"),
                type: "success",
                message: i18n.t("post.deleteSuccessMessage")
            });
        });
    }

    batchDelete(e) {
        e.preventDefault();
        const list = this.state.selected;
        if (list.length === 0) {
            return;
        }
        const posts = [];
        for (let i = 0; i < list.length; i += 1) {
            posts.push({
                id: list[i].id,
                status: list[i].status
            });
        }
        fetch("/admin/api/post/batch-delete", {
            method: "POST",
            body: JSON.stringify({posts: posts})
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

    revert(data, e) {
        e.preventDefault();
        dialog
            .confirm("Revert This Page?")
            .then(() => {
                fetch("/admin/api/post/" + data.id + "/revert", {method: "GET"}).then(() => {
                    this.find();
                    notification({
                        title: i18n.t("post.successTitle"),
                        type: "success",
                        message: i18n.t("post.revertSuccessMessage")
                    });
                });
            })
            .catch(() => {
                notification({
                    title: i18n.t("post.errorTitle"),
                    type: "error",
                    message: i18n.t("post.revertFailMessage")
                });
            });
    }

    publish(data, e) {
        e.preventDefault();
        dialog
            .confirm("Publish This Page?")
            .then(() => {
                fetch("/admin/api/post/" + data.id + "/publish", {method: "GET"}).then(() => {
                    this.find();
                    notification({
                        title: i18n.t("post.successTitle"),
                        type: "success",
                        message: i18n.t("post.publishSuccessMessage")
                    });
                });
            })
            .catch(() => {
                notification({
                    title: i18n.t("post.errorTitle"),
                    type: "error",
                    message: i18n.t("post.publishFailMessage")
                });
            });
    }

    remove(store, data) {
        fetch("/admin/api/post/category/delete", {
            method: "POST",
            body: JSON.stringify({ids: [data.id]})
        }).then(() => {
            this.loadCategory();
            const query = this.state.query;
            query.categoryId = null;
            this.findQuery(query);
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
                                    options={this.state.categoryCascade}
                                    value={this.state.categorySelected}
                                    changeOnSelect={true}
                                    onChange={value => this.selectCategory(value)}
                                    showAllLevels={true}
                                    props={{
                                        value: "id",
                                        label: "displayName",
                                        children: "children"
                                    }}
                                    clearable={true}
                                    placeholder={i18n.t("post.categoryAll")}
                                />
                            </Form.Item>
                            <Form.Item>
                                <Select value={this.state.query.status}
                                    onChange={value => this.queryChangeAndFind("status", value)}
                                    clearable={true}
                                    placeholder={i18n.t("post.statusAll")}>
                                    {
                                        this.state.statusOptions.map(el => <Select.Option key={el.value}
                                            label={el.label} value={el.value}/>)
                                    }
                                </Select>
                            </Form.Item>
                            <Form.Item>
                                <Input icon="fa fa-search" value={this.state.query.query} placeholder={i18n.t("post.queryPlaceHolder")}
                                    onChange={value => this.queryChange("query", value)}/>
                            </Form.Item>
                            <Form.Item>
                                <Button nativeType="button"
                                    onClick={e => this.find(e)}>{i18n.t("post.search")}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.selected.length > 0 ? <Button type="danger" onClick={e => this.batchDelete(e)} nativeType="button">{i18n.t("post.batchDelete")}</Button> : ""}
                        <Button type="primary" nativeType="button">
                            <Link to={{pathname: "/admin/post/create"}}>
                                {i18n.t("post.create")}
                            </Link>
                        </Button>
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