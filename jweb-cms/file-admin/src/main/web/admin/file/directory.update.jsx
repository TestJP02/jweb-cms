import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Form, Input} from "element-react";
import PropTypes from "prop-types";

export default class DirectoryUpdate extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match.params.id,
            loading: true,
            rolesOptions: [],
            parentId: props.match.params.parentId,
            parentDirectory: null,
            directory: {
                parentId: props.match.params.parentId,
                path: null,
                description: null,
                owner: null,
                userGroup: null,
                role: {
                    owner: [],
                    userGroup: []
                }
            },

            rules: {
                path: [
                    {
                        required: true,
                        validator: (rule, value, callback) => {
                            if (value.indexOf("/") === 0 && value.lastIndexOf("/") === value.length - 1) {
                                return callback();
                            }
                            return callback(new Error(window.i18n.t("file.pathRule")));
                        },
                        trigger: "blur"
                    }
                ],
                displayName: [
                    {
                        required: true,
                        message: window.i18n.t("file.displayNameRule"),
                        trigger: "blur"
                    }
                ],
                description: [
                    {
                        required: true,
                        message: window.i18n.t("file.descriptionRule"),
                        trigger: "blur"
                    }
                ],
                displayOrder: [
                    {
                        required: true,
                        validator: (rule, value, callback) => {
                            if (!value) {
                                return callback(new Error(window.i18n.t("file.displayOrderRule")));
                            }
                            if (isNaN(Number(value)) || value % 1 !== 0) {
                                return callback(new Error(window.i18n.t("file.integerRule")));
                            }
                            return callback();
                        },
                        trigger: "blur"
                    }
                ]
            }
        };
    }

    componentWillMount() {
        // fetch("/admin/api/directory/roles", {method: "GET"}).then((response) => {
        //     const rolesOptions = [];
        //     for (let i = 0; i < response.length; i += 1) {
        //         rolesOptions.push({
        //             label: response[i],
        //             value: response[i]
        //         });
        //     }
        //     this.setState({
        //         rolesOptions: rolesOptions,
        //         loading: false
        //     });
        // });
        if (this.state.id) {
            fetch("/admin/api/directory/" + this.state.id).then((response) => {
                this.setState({directory: response});
            });
        }

        if (this.state.parentId) {
            fetch("/admin/api/directory/" + this.state.parentId).then((response) => {
                this.setState({parentDirectory: response});
            });
        }
    }

    querySearchAsync(queryString, cb, type) {
        clearTimeout(this.timeout);

        this.timeout = setTimeout(() => {
            if (type === "user") {
                fetch("/admin/api/user/find", {
                    method: "PUT",
                    body: JSON.stringify({
                        query: queryString,
                        status: "ACTIVE",
                        page: 1,
                        limit: 10
                    })
                }).then((response) => {
                    const users = [];
                    for (let i = 0; i < response.items.length; i += 1) {
                        const item = response.items[i];
                        users.push({value: item.username});
                    }
                    const results = queryString ? users.filter(this.createFilter(queryString)) : users;
                    cb(results);
                });
            }
            if (type === "userGroup") {
                fetch("/admin/api/user/group/find", {
                    method: "PUT",
                    body: JSON.stringify({
                        query: queryString,
                        status: "ACTIVE",
                        page: 1,
                        limit: 10
                    })
                }).then((response) => {
                    const userGroups = [];
                    for (let i = 0; i < response.items.length; i += 1) {
                        const item = response.items[i];
                        userGroups.push({value: item.name});
                    }
                    const results = queryString ? userGroups.filter(this.createFilter(queryString)) : userGroups;
                    cb(results);
                });
            }
        }, 100 * Math.random());
    }

    createFilter(queryString) {
        return list => list.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0;
    }

    onChange(key, value) {
        this.setState({directory: Object.assign({}, this.state.directory, {[key]: value})});
    }

    onRoleChange(key, value) {
        const role = this.state.directory.role;
        role[key] = value;
        this.onChange("role", role);
    }

    save() {
        this.form.validate((valid) => {
            if (valid) {
                fetch("/admin/api/directory", {
                    method: "POST",
                    body: JSON.stringify(this.state.directory)
                }).then(() => {
                    this.props.history.push("/admin/file/directory/list");
                });
            } else {
                return false;
            }
        });
    }

    update() {
        this.form.validate((valid) => {
            if (valid) {
                fetch("/admin/api/directory/" + this.state.id, {
                    method: "PUT",
                    body: JSON.stringify(this.state.directory)
                }).then(() => {
                    this.props.history.push("/admin/file/directory/list");
                });
            } else {
                return false;
            }
        });
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin/">{window.i18n.t("file.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/file/directory/list">{window.i18n.t("file.fileList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? window.i18n.t("file.directoryUpdate") : window.i18n.t("file.directoryCreate")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.id
                            ? <Button type="primary" onClick={() => this.update()}>{window.i18n.t("file.save")}</Button>
                            : <Button type="primary" onClick={() => this.save()}>{window.i18n.t("file.save")}</Button>}
                        <Button type="button"><Link to="/admin/file/directory/list">{window.i18n.t("file.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form ref={(form) => {
                            this.form = form;
                        }} model={this.state.directory} labelWidth="200" rules={this.state.rules}>
                            {this.state.id
                                ? <Form.Item label={window.i18n.t("file.path")} prop="path"><span>{this.state.directory.path}</span></Form.Item>
                                : <Form.Item label={window.i18n.t("file.path")} prop="path"><Input value={this.state.directory.path} onChange={val => this.onChange("path", val)}/></Form.Item>
                            }
                            <Form.Item label={window.i18n.t("file.description")}>
                                <Input type="textarea" value={this.state.directory.description} onChange={val => this.onChange("description", val)}/>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

DirectoryUpdate.propTypes = {
    match: PropTypes.object.match,
    history: PropTypes.object.history
};