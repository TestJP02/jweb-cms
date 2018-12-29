import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Layout, Select} from "element-react";

const i18n = window.i18n;
export default class PageLinkListComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            component: props.component,
            onChange: props.onChange,
            mode: props.mode
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
        const compnent = this.state.component;
        this.state.onChange(compnent);
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
                                            <Input value={this.state.component.attributes.name} onChange={val => this.formChange("name", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="24">
                                        <Form.Item label={i18n.t("page.linkListType")} props="type">
                                            <Select placeholder={i18n.t("page.categorySelect")} value={this.state.component.attributes.type}
                                                onChange={val => this.formChange("type", val)}>
                                                <Select.Option key="hottest" label={i18n.t("page.linkListTypeHottest")} value="HOTTEST" />
                                                <Select.Option key="newest" label={i18n.t("page.linkListTypeNewest")} value="NEWEST" />
                                            </Select>
                                        </Form.Item>
                                    </Layout.Col>
                                    {this.state.component.attributes.type === "HOTTEST" &&
                                        <Layout.Col span="24">
                                            <Form.Item label={i18n.t("page.linkListPeriod")} props="period">
                                                <Select placeholder={i18n.t("page.categorySelect")} value={this.state.component.attributes.period}
                                                    onChange={val => this.formChange("period", val)}>
                                                    <Select.Option key="day" label={i18n.t("page.linkListPeriodDay")} value="DAY" />
                                                    <Select.Option key="week" label={i18n.t("page.linkListPeriodWeek")} value="WEEK" />
                                                    <Select.Option key="month" label={i18n.t("page.linkListPeriodMonth")} value="MONTH" />
                                                </Select>
                                            </Form.Item>
                                        </Layout.Col>
                                    }
                                    <Layout.Col span="16">
                                        <Form.Item label={i18n.t("page.linkListPage")} props="page">
                                            <Input type="number" value={this.state.component.attributes.page}
                                                onChange={val => this.formChange("page", val)} />
                                        </Form.Item>
                                    </Layout.Col>
                                    <Layout.Col span="16">
                                        <Form.Item label={i18n.t("page.linkListLimit")} props="limit">
                                            <Input type="number" value={this.state.component.attributes.limit}
                                                onChange={val => this.formChange("limit", val)} />
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
                <div className="page-default-component__name">{this.state.component.displayName}</div>
                <ul className="page-default-component__list">
                    <li className="page-default-component__item">
                        <div className="page-default-component__content">
                            <div className="page-default-component__line page-default-component__line--h4" style={{width: "60%"}}></div>
                            <div className="page-default-component__line page-default-component__line--small" style={{width: "40%"}}></div>
                        </div>
                    </li>
                    <li className="page-default-component__item">
                        <div className="page-default-component__content">
                            <div className="page-default-component__line page-default-component__line--h4" style={{width: "60%"}}></div>
                            <div className="page-default-component__line page-default-component__line--small" style={{width: "40%"}}></div>
                        </div>
                    </li>
                    <li className="page-default-component__item">
                        <div className="page-default-component__content">
                            <div className="page-default-component__line page-default-component__line--h4" style={{width: "60%"}}></div>
                            <div className="page-default-component__line page-default-component__line--small" style={{width: "40%"}}></div>
                        </div>
                    </li>
                    <li className="page-default-component__item">
                        <div className="page-default-component__content">
                            <div className="page-default-component__line page-default-component__line--h4" style={{width: "60%"}}></div>
                            <div className="page-default-component__line page-default-component__line--small" style={{width: "40%"}}></div>
                        </div>
                    </li>
                </ul>
            </div>
        );
    }
}

PageLinkListComponent.defaultProps = {
    component: {
        name: i18n.t("page.defaultComponentName"),
        type: "HOTTEST",
        period: "WEEK",
        page: 1,
        limit: 10
    },
    mode: "preview"
};

PageLinkListComponent.propTypes = {
    component: {
        name: PropTypes.string,
        type: PropTypes.string,
        perios: PropTypes.string,
        page: PropTypes.number,
        limit: PropTypes.number
    },
    onChange: PropTypes.func,
    mode: PropTypes.string
};