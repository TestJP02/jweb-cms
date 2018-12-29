import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Layout} from "element-react";
import PageMD from "./markdown/page.markdown";
import Showdown from "showdown";
import "./component.card.css";

const i18n = window.i18n;
export default class PageCardComponent extends React.Component {
    constructor(props) {
        super(props);
        this.converter = new Showdown.Converter({
            tables: true,
            simplifiedAutoLink: true
        });
        this.state = {
            component: props.component,
            onChange: props.onChange,
            mode: props.mode,
            isChangingFile: false
        };
    }

    componentWillReceiveProps(nextProps) {
        const mode = nextProps.mode;
        this.setState({mode});
    }

    formChange(key, value) {
        const component = this.state.component;
        component.attributes[key] = value;
        this.setState({component});
    }

    save() {
        this.state.onChange(this.state.component);
    }

    onContentChange(content) {
        const component = this.state.component;
        component.attributes.content = content;
        this.setState({component});
    }

    editor() {
        return <Dialog visible={true} onCancel={() => this.save()}>
            <Dialog.Body>
                <Form>
                    <Layout.Row>
                        <Layout.Col span="24">
                            <Form.Item label={i18n.t("page.title")} labelWidth="80">
                                <Input value={this.state.component.attributes.title}
                                    onChange={value => this.formChange("title", value)}></Input>
                            </Form.Item>
                        </Layout.Col>
                        <Layout.Col span="24">
                            <Form.Item label={i18n.t("page.content")} labelWidth="80">
                                <PageMD content={this.state.component.attributes.content} onChange={content => this.onContentChange(content)} />
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

    render() {
        return (
            <div className={(this.state.mode === "edit" ? "editing " : "") + "page-card"}>
                {this.state.mode === "edit"
                    ? this.editor()
                    : <div className="page-card__preview">
                        <div className="page-card__preview-header">
                            <div className="page-card__preview-title">{this.state.component.attributes.title}</div>
                        </div>
                        <div className="page-card__preview-body" dangerouslySetInnerHTML={{__html: this.converter.makeHtml(this.state.component.attributes.content)}}>
                        </div>
                    </div>
                }
            </div>
        );
    }
}

PageCardComponent.defaultProps = {mode: "preview"};

PageCardComponent.propTypes = {
    component: {
        attributes: {
            links: PropTypes.string,
            userEnabled: PropTypes.bool,
            searchEnabled: PropTypes.bool
        }
    },
    onChange: PropTypes.func,
    mode: PropTypes.string
};