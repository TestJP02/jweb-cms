import React from "react";
import PropTypes from "prop-types";
import {Button, Cascader, Dialog, Form, Input} from "element-react";

const i18n = window.i18n;
export default class PageListComponent extends React.Component {
    constructor(props) {
        super(props);

        const component = props.component;
        if (!component.attributes.userEnabled) {
            component.attributes.userEnabled = false;
        }
        if (!component.attributes.links) {
            component.attributes.links = [];
        }
        this.state = {
            component,
            onChange: props.onChange,
            mode: props.mode
        };
        this.setCategory();
    }

    componentWillUpdate(props) {
        const mode = this.state.mode;
        const newMode = props.mode;
        if (newMode !== mode) {
            this.setState({mode: newMode});
        }
    }

    setCategory() {
        fetch("/admin/api/page/category/tree", {
            method: "PUT",
            body: JSON.stringify({})
        }).then((response) => {
            const cascade = response;
            this.trimCascade(cascade);
            const component = this.state.component;
            const categorySelected = [];
            if (component.attributes.categoryId) {
                this.traversal(cascade, component.attributes.categoryId, categorySelected);
            }
            this.setState({
                categoryList: cascade,
                categorySelected: categorySelected
            });
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

    traversal(list, id, categorySelected) {
        for (let i = 0; i < list.length; i += 1) {
            const node = list[i];
            if (node.id === id) {
                categorySelected.push(id);
                return true;
            }
            if (node.children !== null && this.traversal(node.children, id, categorySelected)) {
                categorySelected.splice(0, 0, node.id);
                return true;
            }
        }
        return false;
    }

    formChange(key, value) {
        const component = this.state.component;
        component.attributes[key] = value;
        this.setState({component});
    }

    save() {
        this.state.onChange(this.state.component);
    }

    selectCategory(value) {
        const component = this.state.component;
        component.attributes.categoryId = value[value.length - 1];
        this.setState({
            component,
            categorySelected: value
        });
    }

    editor() {
        if (this.state.isChangingFile) {
            return <FileBrowser visible={true}
                files={this.files()}
                multiple={false}
                onChange={files => this.onFileChange(files)} />;
        }
        return <Dialog className="page-list__editor" visible={true} onCancel={() => this.save()}>
            <Dialog.Body>
                <Form>
                    <Form.Item label={i18n.t("page.title")} labelWidth="120">
                        <Input value={this.state.component.attributes.title}
                            onChange={value => this.formChange("title", value)}></Input>
                    </Form.Item>
                    <Form.Item label={i18n.t("page.category")} labelWidth="120">
                        <Cascader
                            options={this.state.categoryList ? this.state.categoryList : []}
                            value={this.state.categorySelected}
                            changeOnSelect={true}
                            onChange={value => this.selectCategory(value)}
                            props={{
                                value: "id",
                                label: "displayName"
                            }}
                            showAllLevels={true}
                            clearable={true}
                        />
                    </Form.Item>
                    <Form.Item label={i18n.t("page.listLimit")} labelWidth="120">
                        <Input value={this.state.component.attributes.limit}
                            onChange={value => this.formChange("limit", value)}></Input>
                    </Form.Item>
                </Form>
            </Dialog.Body>

            <Dialog.Footer className="dialog-footer">
                <Button type="primary" onClick={() => this.save()}>{i18n.t("page.save")}</Button>
            </Dialog.Footer>
        </Dialog>;
    }

    render() {
        return (
            <div className={(this.state.mode === "edit" ? "editing " : "") + "page-default-component"}>
                {this.state.mode === "edit"
                    ? this.editor()
                    : <div className="page-header__preview">
                        <div className="page-default-component__name">{this.state.component.attributes.title || this.state.component.name}</div>
                        <ul className="page-default-component__list">
                            <li className="page-default-component__item page-default-component__item--image">
                                <div className="page-default-component__image"></div>
                                <div className="page-default-component__content">
                                    <div className="page-default-component__line page-default-component__line--h1" style={{width: "60%"}}></div>
                                    <div className="page-default-component__line " style={{width: "100%"}}></div>
                                    <div className="page-default-component__line" style={{width: "60%"}}></div>
                                    <div className="page-default-component__line page-default-component__line--small" style={{width: "40%"}}></div>
                                </div>
                            </li>
                            <li className="page-default-component__item page-default-component__item--image">
                                <div className="page-default-component__image"></div>
                                <div className="page-default-component__content ">
                                    <div className="page-default-component__line page-default-component__line--h1" style={{width: "60%"}}></div>
                                    <div className="page-default-component__line " style={{width: "100%"}}></div>
                                    <div className="page-default-component__line" style={{width: "60%"}}></div>
                                    <div className="page-default-component__line page-default-component__line--small" style={{width: "40%"}}></div>
                                </div>
                            </li>
                            <li className="page-default-component__item page-default-component__item--image">
                                <div className="page-default-component__image"></div>
                                <div className="page-default-component__content">
                                    <div className="page-default-component__line page-default-component__line--h1" style={{width: "60%"}}></div>
                                    <div className="page-default-component__line " style={{width: "100%"}}></div>
                                    <div className="page-default-component__line" style={{width: "60%"}}></div>
                                    <div className="page-default-component__line page-default-component__line--small" style={{width: "40%"}}></div>
                                </div>
                            </li>
                        </ul>
                    </div>
                }
            </div>
        );
    }
}

PageListComponent.defaultProps = {component: {name: i18n.t("page.defaultComponentName")}};

PageListComponent.propTypes = {
    component: {name: PropTypes.string},
    mode: PropTypes.string,
    onChange: PropTypes.func
};