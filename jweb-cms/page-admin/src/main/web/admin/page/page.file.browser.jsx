import React from "react";
import {Button, Checkbox, Dialog, Layout, Message as notification, Tree, Upload} from "element-react";
import PropTypes from "prop-types";
import "./page.file.browser.css";

const i18n = window.i18n;
export default class FileBrowser extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            onChange: props.onChange,
            directoryTree: [],
            selectedDirectory: null,
            fileMap: {},
            data: [],
            selectedFiles: props.files,
            defaultSelectedFiles: [],
            treeReady: false,
            visible: props.visible,
            options: {
                children: "children",
                label: "name"
            },
            uploading: false,
            multiple: props.multiple
        };
    }

    componentWillMount() {
        this.loadDirectory();
        if (this.state.selectedFiles.length > 0) {
            this.state.selectedFiles.map(file => this.state.defaultSelectedFiles.push(file));
        }
    }

    loadDirectory() {
        fetch("/admin/api/directory/tree", {method: "GET"}).then((response) => {
            this.setState({
                directoryTree: response,
                treeReady: true,
                selectedDirectory: response.length > 0 ? response[0].id : null
            });
            this.find();
        });
    }

    find(force) {
        const fileMap = this.state.fileMap;
        if (fileMap[this.state.selectedDirectory] && !force) {
            this.setState({data: fileMap[this.state.selectedDirectory]});
        } else {
            fileMap[this.state.selectedDirectory] = [];
            fetch("/admin/api/file/find", {
                method: "PUT",
                body: JSON.stringify({directoryId: this.state.selectedDirectory})
            }).then((response) => {
                fileMap[this.state.selectedDirectory] = response.items;
                this.setState({
                    fileMap: fileMap,
                    data: response.items
                });
            });
        }
    }

    checked(path) {
        for (let index = 0; index < this.state.selectedFiles.length; index += 1) {
            if (this.state.selectedFiles[index].path === path) {
                return true;
            }
        }
    }

    select(file) {
        const selectedFiles = this.state.selectedFiles;
        this.toggleFile(file, selectedFiles);
        this.setState({selectedFiles: selectedFiles});
    }

    toggleFile(file, selectedFiles) {
        if (this.state.multiple) {
            for (let index = 0; index < selectedFiles.length; index += 1) {
                if (selectedFiles[index].path === file.path) {
                    selectedFiles.splice(index, 1);
                    return;
                }
            }
            selectedFiles.push(file);
        } else if (selectedFiles.length !== 0 && selectedFiles[0].path === file.path) {
            selectedFiles.splice(0, 1);
        } else {
            selectedFiles.splice(0, 1, file);
        }
    }

    submit() {
        this.state.onChange(this.state.selectedFiles);
    }

    cancel() {
        this.state.onChange(this.state.defaultSelectedFiles);
    }

    filePreview(file) {
        if (this.isImage(file)) {
            return <img src={"/admin" + file.path} alt="" className="page-file-browser__file-image"/>;
        }
        return <div className="page-file-browser__file-file">
            {this.otherFilePreview(file)}
        </div>;
    }

    isImage(file) {
        const regex = /(\.jpg)|(\.png)|(\.bmp)|(\.gif)$/;
        return regex.test(file.path);
    }

    otherFilePreview(file) {
        if (file.path.indexOf(".doc") >= 0 || file.path.indexOf(".docx") >= 0) {
            return <i className="fa fa-file-word-o"/>;
        } else if (file.path.indexOf(".xls") >= 0) {
            return <i className="fa fa-file-excel-o"/>;
        } else if (file.path.indexOf(".pdf") >= 0) {
            return <i className="fa fa-file-pdf-o"/>;
        } else if (file.path.indexOf(".ppt") >= 0) {
            return <i className="fa fa-file-powerpoint-o"/>;
        }
        return <i className="fa fa-file-o"/>;
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
                title: i18n.t("page.uploadSuccess"),
                type: "success"
            });
            this.find(true);
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
            <div className="page-file-browser">
                <Dialog
                    title="File Browser"
                    visible={this.state.visible}
                    onCancel={() => this.cancel()}
                >
                    <Dialog.Body>
                        <div className="page-file-browser__container">
                            <Layout.Row gutter="20">
                                <Layout.Col span="6">
                                    <div className="page-file-browser__directory">
                                        {this.state.treeReady &&
                                        <Tree
                                            data={this.state.directoryTree}
                                            options={this.state.options}
                                            highlightCurrent={true}
                                            nodeKey="id"
                                            defaultExpandAll={true}
                                            onNodeClicked={(data) => {
                                                this.setState({selectedDirectory: data.id}, () => this.find());
                                            }}
                                        />
                                        }
                                    </div>
                                </Layout.Col>
                                <Layout.Col span="18" className="page-file-browser__body">
                                    {this.state.data.map(file =>
                                        <Layout.Col span="6" key={file.id}>
                                            <div className="page-file-browser__file-item">
                                                <div className="page-file-browser__file-preview"
                                                    onClick={() => this.select(file)}>
                                                    {this.filePreview(file)}
                                                    <Checkbox label={file.id} className="page-file-browser__file-checkbox"
                                                        checked={this.checked(file.path)}
                                                        disabled={true}/>
                                                </div>
                                                <div className="page-file-browser__file-name">{file.id}</div>
                                            </div>
                                        </Layout.Col>
                                    )}
                                </Layout.Col>
                            </Layout.Row>
                        </div>
                    </Dialog.Body>

                    <Dialog.Footer className="dialog-footer">
                        <Layout.Row>
                            <Layout.Col span="12" style={{"textAlign": "left"}}>
                                <Upload
                                    action={"/admin/api/file/upload?directoryId=" + this.state.selectedDirectory}
                                    showFileList={false}
                                    onProgress={() => this.setState({uploading: true})}
                                    onSuccess={(response, file, fileList) => this.uploadSuccess(file, fileList)}
                                    onError={(error, file, fileList) => this.uploadError(error, file, fileList)}
                                    multiple={true}>
                                    <Button
                                        loading={this.state.uploading}
                                        disabled={this.state.uploading}>{i18n.t("page.upload")}</Button>
                                </Upload>
                            </Layout.Col>
                            <Layout.Col span="12">
                                <Button onClick={() => this.cancel()}>{i18n.t("page.cancel")}</Button>
                                <Button type="primary" onClick={() => this.submit()}>{i18n.t("page.confirm")}</Button>
                            </Layout.Col>
                        </Layout.Row>
                    </Dialog.Footer>
                </Dialog>
            </div>
        );
    }
}

FileBrowser.defaultProps = {
    files: [],
    visible: false
};

FileBrowser.propTypes = {
    onChange: PropTypes.func,
    files: PropTypes.array,
    visible: PropTypes.bool,
    multiple: PropTypes.bool
};