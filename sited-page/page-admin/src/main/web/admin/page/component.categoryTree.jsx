import React from "react";
import PropTypes from "prop-types";
import {Button, Cascader, Dialog, Form, Layout} from "element-react";

const i18n = window.i18n;
export default class PageCategoryTreeComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            mode: props.mode,
            component: props.component,
            categoryOptions: [],
            selectedCategories: []
        };
    }

    componentWillReceiveProps(nextProps) {
        const mode = nextProps.mode;
        this.setState({mode});
    }

    componentWillMount() {
        fetch("/admin/api/page/category/tree", {
            method: "PUT",
            body: JSON.stringify({})
        }).then((response) => {
            const categoryOptions = response;
            this.trimCascade(categoryOptions);
            const selectedCategories = [];
            if (this.state.component.attributes.categoryId) {
                this.traversal(categoryOptions, this.state.component.attributes.categoryId, selectedCategories);
            }
            this.setState({
                categoryOptions: categoryOptions,
                selectedCategories: selectedCategories
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

    save() {
        this.props.onChange(this.state.component);
    }

    selectCategory(value) {
        const component = this.state.component;
        component.attributes.categoryId = value[value.length - 1];
        this.setState({
            component: component,
            selectedCategories: value
        });
    }

    render() {
        if (this.state.mode === "edit") {
            return <Dialog
                visible={true}
                onCancel={() => this.save()}
            >
                <Dialog.Body>
                    <Form>
                        <Layout.Row>
                            <Layout.Col span="24">
                                <Form.Item label={i18n.t("page.footerCopyrights")} labelWidth="120">
                                    <Cascader
                                        options={this.state.categoryOptions}
                                        value={this.state.selectedCategories}
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
                            </Layout.Col>
                        </Layout.Row>
                    </Form>
                </Dialog.Body>
                <Dialog.Footer className="dialog-footer">
                    <Button type="primary" onClick={() => this.save()}>{i18n.t("page.save")}</Button>
                </Dialog.Footer>
            </Dialog>;
        }
        return <div className="page-author page-default-component">
            <div className="page-default-component__content">
                <div className="page-default-component__line page-default-component__line--inline" style={{width: "30%"}}></div>
                <br/>
                <div className="page-default-component__line page-default-component__line--inline" style={{width: "20%"}}></div>
                <div className="page-default-component__line page-default-component__line--inline" style={{width: "20%"}}></div>
            </div>
        </div>;
    }
}

PageCategoryTreeComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};