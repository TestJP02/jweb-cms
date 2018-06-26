import React from "react";
import {Link} from "react-router-dom";
import {Breadcrumb, Button, Card, Cascader, Form, Input, Message as alert, Upload} from "element-react";
import PropTypes from "prop-types";

const i18n = window.i18n;
export default class FileUpdate extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match.params.id,
            directoryCascade: [],
            directorySelected: [],
            file: {
                id: props.match.params.id,
                directoryId: null,
                tags: [],
                path: null,
                title: null,
                description: null
            },
            rules: {
                directoryId: [
                    {
                        required: true,
                        message: window.i18n.t("file.directoryPathRule"),
                        trigger: "change"
                    }
                ]
            }
        };
    }

    componentWillMount() {
        if (this.state.id) {
            fetch("/admin/api/file/" + this.state.id).then((response) => {
                fetch("/admin/api/directory/tree", {method: "GET"}).then((cascade) => {
                    this.trimCascade(cascade);
                    const path = [];
                    this.traversal(cascade, response.directoryId, path);
                    this.setState({
                        directoryCascade: cascade,
                        directorySelected: path,
                        file: response
                    });
                });
            });
        } else {
            fetch("/admin/api/directory/tree", {method: "GET"}).then((cascade) => {
                this.trimCascade(cascade);
                this.setState({directoryCascade: cascade});
            });
        }
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

    selectDirectory(value) {
        let directoryId = null;
        if (value.length !== 0) {
            directoryId = value[value.length - 1];
        }
        this.setState({
            file: Object.assign(this.state.file, {directoryId: directoryId}),
            directorySelected: value
        });
    }

    onChange(key, value) {
        this.setState({file: Object.assign({}, this.state.file, {[key]: value})});
    }

    check() {
        if (this.state.file.directoryId === null) {
            alert(i18n.t("file.directoryPathRule"));
            return false;
        }
        return true;
    }

    traversal(list, id, result) {
        for (let i = 0; i < list.length; i += 1) {
            const node = list[i];
            if (node.id === id) {
                result.push(id);
                return true;
            }
            if (node.children !== null && this.traversal(node.children, id, result)) {
                result.splice(0, 0, node.id);
                return true;
            }
        }
        return false;
    }

    update() {
        this.form.validate((valid) => {
            if (valid) {
                fetch("/admin/api/file/" + this.state.file.id, {
                    method: "PUT",
                    body: JSON.stringify(this.state.file)
                }).then(() => {
                    this.props.history.push("/admin/file/list");
                });
            } else {
                return false;
            }
        });
    }

    isImage(path) {
        return (/.*\.(jpg|jpeg|png|gif)/i).test(path);
    }

    render() {
        return (
            <div className="page">
                <div className="toolbar">
                    <div className="toolbar-form">
                        <Breadcrumb separator="/">
                            <Breadcrumb.Item><Link to="/admin/">{i18n.t("file.home")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item><Link to="/admin/file/list">{i18n.t("file.fileList")}</Link></Breadcrumb.Item>
                            <Breadcrumb.Item>{this.state.id ? i18n.t("file.fileUpdate") : i18n.t("file.fileCreate")}</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="toolbar-buttons">
                        <Button type="primary" onClick={() => this.update()}>{i18n.t("file.save")}</Button>
                        <Button type="button" onClick={() => this.props.history.push("/admin/file/list")}>{i18n.t("file.cancel")}</Button>
                    </div>
                </div>
                <div className="body">
                    <Card>
                        <Form ref={(form) => {
                            this.form = form;
                        }} model={this.state.file} labelWidth="200" rules={this.state.rules} className="file-form">
                            <Form.Item label={i18n.t("file.directoryPath")} prop="directoryId">
                                <Cascader
                                    options={this.state.directoryCascade}
                                    value={this.state.directorySelected}
                                    changeOnSelect={true}
                                    onChange={value => this.selectDirectory(value)}
                                    clearable={true}
                                    showAllLevels={false}
                                    props={{
                                        value: "id",
                                        label: "path"
                                    }}
                                />
                                <Input style={{float: "right"}} type="hidden" value={this.state.file.directoryId}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("file.path")}>
                                <Upload
                                    showFileList={false}
                                    beforeUpload={() => this.check()}
                                    action={"/admin/api/file/upload?directoryId=" + this.state.file.directoryId}
                                    onSuccess={(response) => {
                                        this.setState({file: response});
                                    }}>
                                    {this.state.file.path
                                        ? <div className="el-form-upload-preview">
                                            {(/.*\.(jpg|jpeg|png|gif)/i).test(this.state.file.path)
                                                ? <img src={"/admin/file" + this.state.file.path}/>
                                                : <div className="el-form-upload-preview__ext">{this.state.file.extension}</div>}
                                            <div className="el-form-upload-preview-delete-wrap"
                                                onClick={() => {
                                                    const file = this.state.file;
                                                    file.path = null;
                                                    this.setState({file});
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
                            <Form.Item label={i18n.t("file.title")}>
                                <Input value={this.state.file.title} onChange={val => this.onChange("title", val)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("file.description")}>
                                <Input type="textarea" value={this.state.file.description} onChange={val => this.onChange("description", val)}/>
                            </Form.Item>
                            <Form.Item label={i18n.t("file.tags")}>
                                <ElementUI.TagList list={this.state.file.tags} readOnly={false} onChange={val => this.onChange("tags", val)}/>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
            </div>
        );
    }
}

FileUpdate.propTypes = {
    match: PropTypes.object.match,
    history: PropTypes.object.history
};