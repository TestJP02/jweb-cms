import React from "react";
import {Breadcrumb, Button, Card, Form, Input, Select, Upload} from "element-react";
import PropTypes from "prop-types";
import {Link} from "react-router-dom";

import "./tag.update.css";

const i18n = window.i18n;
export default class TagUpdate extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id: this.props.match.params.id,
            form: {
                displayName: null,
                description: null,
                path: []
            },
            parentOptions: [],
            rules: {
                displayName: [{
                    required: true,
                    message: i18n.t("post.nameRequired"),
                    trigger: "blur"
                }]
            }
        };
    }

    componentWillMount() {
        if (this.state.id) {
            fetch("/admin/api/post/tag/" + this.state.id)
                .then((response) => {
                    this.setState({form: response});
                });
        }

        fetch("/admin/api/post/tag/find", {
            method: "PUT",
            body: JSON.stringify({})
        }).then((response) => {
            this.setState({parentOptions: response.items});
        });
    }

    onChange(key, value) {
        this.setState(
            {form: Object.assign(this.state.form, {[key]: value})}
        );
    }

    save() {
        this.formRef.validate((valid) => {
            if (valid) {
                fetch("/admin/api/post/tag", {
                    method: "post",
                    body: JSON.stringify(this.state.form)
                }).then(() => {
                    this.props.history.push("/admin/post/tag/list");
                });
            } else {
                return false;
            }
        });
    }


    addField() {
        const form = this.state.form;
        if (!form.fields) {
            form.fields = {};
        }
        const number = Object.entries(form.fields).length + 1;
        form.fields["field " + number] = "value " + number;
        this.setState({form});
    }

    removeField(key) {
        const form = this.state.form;
        delete form.fields[key];
        this.setState({form});
    }

    changeFieldKey(newKey, prevKey) {
        const form = this.state.form;
        const fields = {};
        for (const key in form.fields) {
            if (key === prevKey) {
                fields[newKey] = form.fields[prevKey];
            } else {
                fields[key] = form.fields[key];
            }
        }
        form.fields = fields;
        this.setState({form});
    }

    changeFieldValue(value, key) {
        const form = this.state.form;
        form.fields[key] = value;
        this.setState({form});
    }


    update() {
        this.formRef.validate((valid) => {
            if (valid) {
                fetch("/admin/api/post/tag/" + this.state.id, {
                    method: "put",
                    body: JSON.stringify(this.state.form)
                }).then(() => {
                    this.props.history.push("/admin/post/tag/list");
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
                            <Breadcrumb.Item><Link to="/admin">{i18n.t("post.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/post/tag/list">{i18n.t("post.tags")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? i18n.t("post.updateTag") : i18n.t("post.createTag")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        {this.state.id ? <span>
                            <Button type="primary" onClick={() => this.update()}>{i18n.t("post.save")}</Button>
                        </span> : <Button type="primary" onClick={() => this.save()}>{i18n.t("post.save")}</Button>}
                        <Button type="button"><Link to="/admin/post/tag/list">{i18n.t("post.cancel")}</Link></Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form model={this.state.form} rules={this.state.rules} ref={(c) => {
                            this.formRef = c;
                        }} labelWidth="150">
                            <Form.Item label={i18n.t("post.parentTag")} prop="parentId">
                                <Select value={this.state.form.parentId} onChange={val => this.onChange("parentId", val)}
                                    placeholder={i18n.t("post.selectParentTag")} clearable={true}>
                                    {this.state.parentOptions.map(el => <Select.Option key={el.id} label={el.displayName}
                                        value={el.id}/>)}
                                </Select>
                            </Form.Item>
                            <Form.Item label={i18n.t("post.tagName")} prop="displayName">
                                <Input value={this.state.form.displayName} onChange={value => this.onChange("displayName", value)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("post.path")} prop="displayName">
                                <Input value={this.state.form.name} onChange={value => this.onChange("name", value)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("post.tags")}>
                                <ElementUI.TagList list={this.state.form.tags} onChange={val => this.onChange("tags", val)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("post.alias")} prop="alias">
                                <Input value={this.state.form.alias} onChange={value => this.onChange("alias", value)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("post.displayOrder")} prop="displayOrder">
                                <Input value={this.state.form.displayOrder} onChange={value => this.onChange("displayOrder", value)} type="number"/>
                            </Form.Item>
                            <Form.Item label={i18n.t("post.description")} prop="description">
                                <Input value={this.state.form.description} onChange={value => this.onChange("description", value)} type="textarea"/>
                            </Form.Item>
                            <Form.Item label={i18n.t("post.imageURL")}>
                                <Upload
                                    showFileList={false}
                                    action={"/admin/api/file/upload?directoryPath=/upload/post/"}
                                    onSuccess={(response) => {
                                        const form = this.state.form;
                                        form.imageURL = response.path;
                                        this.setState({form});
                                    }}>
                                    {this.state.form.imageURL
                                        ? <div className="el-form-upload-preview">
                                            <img src={"/admin" + this.state.form.imageURL}/>
                                            <div className="el-form-upload-preview-delete-wrap"
                                                onClick={() => {
                                                    const form = this.state.form;
                                                    form.imageURL = null;
                                                    this.setState({form});
                                                }}>
                                                <Button type="text" className="el-form-upload-preview-delete">
                                                    <i className="iconfont icon-icon_delete"/>
                                                </Button>
                                            </div>
                                        </div>
                                        : <Button className="el-form-upload-button" size="large">
                                            <i className="el-icon-plus"/>
                                        </Button>}
                                </Upload>
                            </Form.Item>
                            <Form.Item label={i18n.t("post.type")} prop="description">
                                <Input value={this.state.form.type} onChange={value => this.onChange("type", value)}/>
                            </Form.Item>

                            <Form.Item label={window.ElementUI.i18n.t("post.fields")} prop="fields">
                                {this.state.form.fields && Object.entries(this.state.form.fields).map((field, index) =>
                                    <Form.Item className="form-update__field" key={field.key} labelWidth="0">
                                        <Input value={field[1]}
                                            placeholder={i18n.t("form.fieldValue")}
                                            prepend={<Input value={field[0]} placeholder={i18n.t("form.fieldName")} onChange={val => this.changeFieldKey(val, field[0])}/>}
                                            onChange={val => this.changeFieldValue(val, field[0])}
                                            append={<Button size="small" onClick={() => this.removeField(field[0])} icon="minus"></Button>}/>
                                    </Form.Item>
                                )}
                                <Button onClick={() => this.addField()} icon="plus"></Button>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

TagUpdate.propTypes = {
    match: PropTypes.object,
    history: PropTypes.object
};