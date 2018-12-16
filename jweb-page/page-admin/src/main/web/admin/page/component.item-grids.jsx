import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Layout, MessageBox} from "element-react";
import FileBrowser from "./page.file.browser";

const i18n = window.i18n;
export default class PageItemGridsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            component: props.component,
            onChange: props.onChange,
            mode: props.mode,
            editingGrid: {},
            editingIndex: null
        };
    }

    componentWillReceiveProps(nextProps) {
        const mode = nextProps.mode;
        this.setState({mode});
    }

    formChange(key, val) {
        const component = this.state.component;
        component.attributes[key] = val;
        this.setState({component});
    }

    saveComponent() {
        const component = this.state.component;
        this.state.onChange(component);
    }

    addGrid() {
        if (this.state.editingIndex === null) {
            this.setState({
                editingIndex: this.state.component.attributes.grids ? this.state.component.attributes.grids.length : 0,
                editingGrid: {}
            });
        } else if (!this.state.component.attributes.grids || this.state.component.attributes.grids.length === this.state.editingIndex) {
            this.setState({editingIndex: null});
        }
    }

    editGrid(index) {
        if (this.state.editingIndex === null) {
            this.setState({
                editingIndex: index,
                editingGrid: Object.assign({}, this.state.component.attributes.grids[index])
            });
        } else if (this.state.editingIndex === index) {
            MessageBox.confirm(i18n.t("page.gridDeleteConfirm"), i18n.t("page.warning"), {type: "error"}).then(() => {
                const component = this.state.component;
                component.attributes.grids.splice(index, 1);
                this.setState({
                    component,
                    editingIndex: null
                });
            });
        }
    }

    changeGrid(key, val) {
        const editingGrid = this.state.editingGrid;
        editingGrid[key] = val;
        this.setState({editingGrid});
    }

    files() {
        const files = [];
        if (this.state.editingGrid.imageURL) {
            files.push({path: this.state.editingGrid.imageURL});
        }
        return files;
    }

    changeImage(files) {
        if (files && files.length > 0) {
            this.changeGrid("imageURL", files[0].path);
            this.setState({addingImage: false});
        }
    }

    saveGrid() {
        const component = this.state.component;
        if (!component.attributes.grids) {
            component.attributes.grids = [];
        }

        if (component.attributes.grids[this.state.editingIndex]) {
            const grid = component.attributes.grids[this.state.editingIndex];
            Object.assign(grid, this.state.editingGrid);
        } else {
            const grid = Object.assign({}, this.state.editingGrid);
            component.attributes.grids.push(grid);
        }
        this.setState({
            component: component,
            editingIndex: null
        });
    }

    cancelGrid() {
        this.setState({editingIndex: null});
    }

    gridStyle(index) {
        const length = this.state.component.attributes.grids ? this.state.component.attributes.grids.length : 0;
        if (index === this.state.editingIndex) {
            return {
                transform: "rotateZ(45deg)",
                background: "#ff2000",
                opacity: 1
            };
        } else if (!index && index !== 0 && this.state.editingIndex === length) {
            return {
                transform: "rotateZ(45deg)",
                background: "#ff2000",
                opacity: 1
            };
        }
        return {};
    }

    gridEditor() {
        return <div className="page-item-grids__editor">
            <Layout.Row>
                <Layout.Col span="8">
                    {this.state.editingGrid.imageURL
                        ? <img className="page-item-grids__editor-image"
                            src={this.state.editingGrid.imageURL} alt=""
                            onClick={() => this.setState({addingImage: true})} />
                        : <div className="page-item-grids__editor-image-add"
                            onClick={() => this.setState({addingImage: true})}>
                            <Button className="page-item-grids-preview__item-add" type="icon">
                                <i className="fa fa-plus" />
                            </Button>
                        </div>
                    }
                </Layout.Col>
                <Layout.Col span="16">
                    <Form.Item props="name" labelWidth="0">
                        <Input type="text" value={this.state.editingGrid.name}
                            placeholder={i18n.t("page.name")}
                            onChange={val => this.changeGrid("name", val)} />
                    </Form.Item>
                    <Form.Item props="link" labelWidth="0">
                        <Input type="text" value={this.state.editingGrid.link}
                            placeholder={i18n.t("page.link")}
                            onChange={val => this.changeGrid("link", val)} />
                    </Form.Item>
                </Layout.Col>
                <Layout.Col span="24">
                    <Form.Item props="name" labelWidth="0">
                        <Input type="textarea" value={this.state.editingGrid.description}
                            placeholder={i18n.t("page.description")}
                            onChange={val => this.changeGrid("description", val)} />
                    </Form.Item>
                </Layout.Col>
                <Layout.Col span="24">
                    <Button onClick={() => this.cancelGrid()}>{i18n.t("page.cancel")}</Button>
                    <Button type="primary" onClick={() => this.saveGrid()} >{i18n.t("page.save")}</Button>
                </Layout.Col>
            </Layout.Row>
        </div>;
    }

    render() {
        return (
            <div className="page-default-component">
                {this.state.mode === "edit" &&
                    <Dialog
                        visible={true}
                        onCancel={() => this.saveComponent()}
                    >
                        <Dialog.Body>
                            <Form ref={(form) => {
                                this.form = form;
                            }} model={this.state.component.attributes} labelWidth="120" rules={this.state.formRules}>
                                <Layout.Row>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.name")} props="name">
                                            <Input type="text" value={this.state.component.attributes.name} onChange={val => this.formChange("name", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.link")} props="name">
                                            <Input type="text" value={this.state.component.attributes.link} onChange={val => this.formChange("link", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.gridLimit")} props="limit">
                                            <Input type="number" value={this.state.component.attributes.limit} onChange={val => this.formChange("limit", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.gridList")} props="limit">
                                            <div className="page-grid__list">
                                                <div className="page-item-grids-preview__item" onClick={() => this.addGrid()}>
                                                    <Button className="page-item-grids-preview__item-add" type="icon"
                                                        style={this.gridStyle()}>
                                                        <i className="fa fa-plus" />
                                                    </Button>
                                                </div>
                                                {this.state.component.attributes.grids &&
                                                    this.state.component.attributes.grids.map((grid, index) =>
                                                        <div key={index} className={"page-item-grids-preview__item " +
                                                            (this.state.editingIndex === index ? "page-item-grids-preview__item--editing" : "") +
                                                            (this.state.editingIndex !== null && this.state.editingIndex !== index ? "page-item-grids-preview__item--disable" : "")}>
                                                            <img className="page-item-grids-preview__item-image" src={grid.imageURL} alt="" />
                                                            <div className="page-item-grids-preview__item-name">{grid.name} </div>
                                                            <Button className="page-item-grids-preview__item-add page-item-grids-preview__item-add--hidden" type="icon"
                                                                style={this.gridStyle(index)}
                                                                onClick={() => this.editGrid(index)}>
                                                                <i className={"fa " + (this.state.editingIndex === index ? "fa-plus" : "fa-pencil")} />
                                                            </Button>
                                                        </div>
                                                    )}
                                            </div>
                                            {this.state.editingIndex !== null && this.gridEditor()}
                                        </Form.Item>
                                    </Layout.Col>

                                </Layout.Row>
                            </Form>
                        </Dialog.Body>

                        <Dialog.Footer className="dialog-footer">
                            <Button type="primary" onClick={() => this.saveComponent()}>{i18n.t("page.save")}</Button>
                        </Dialog.Footer>
                    </Dialog>
                }
                {this.state.addingImage &&
                    <FileBrowser files={this.files()}
                        multiple={false}
                        visible={true}
                        onChange={files => this.changeImage(files)} />
                }
                <div className="page-default-component__name">{this.state.component.attributes.name}</div>
                <div className="page-item-grids-preview">
                    <div className="page-item-grids-preview__list">
                        {this.state.component.attributes.grids &&
                            this.state.component.attributes.grids.map((grid, index) =>
                                <div key={index} className="page-item-grids-preview__item"
                                    style={{width: String(100 / this.state.component.attributes.limit) + "%"}}>
                                    <img className="page-item-grids-preview__item-image" src={grid.imageURL} alt="" />
                                    <div className="page-item-grids-preview__item-name">{grid.name} </div>
                                </div>
                            )}
                    </div>
                </div>
            </div>
        );
    }
}

PageItemGridsComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};