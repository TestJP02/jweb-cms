import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Select} from "element-react";

const i18n = window.i18n;

export default class TemplateGridEditor extends React.Component {
    constructor(props) {
        super(props);
        this.state = {section: props.section};
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.section) {
            const section = nextProps.section;
            this.initSection(section);
            this.setState({section: section});
        }
    }

    componentWillMount() {
        const widthOptions = [];
        for (let width = 1; width <= 12; width += 1) {
            widthOptions.push(width);
        }
        this.setState({widthOptions});
    }

    formChange(key, value) {
        this.setState({section: Object.assign({}, this.state.section, {[key]: value})});
    }

    changeWidth(screenWidth, sectionValue) {
        let value = sectionValue;
        if (!value || value < 1) {
            value = 1;
        } else if (value > this.gridColumns) {
            value = this.gridColumns;
        }
        this.state.section.widths.forEach((width) => {
            if (width.screenWidth === screenWidth) {
                width.width = value;
            }
        });
        this.setState({section: this.state.section});
    }

    save() {
        this.props.onChange(this.state.section);
    }

    cancel() {
        this.props.onCancel();
    }

    render() {
        return (
            <div className="layout-section__add-section-panel">
                {this.state.section &&
                <Dialog
                    title="Add Section"
                    visible={true}
                    onCancel={() => this.cancel()}
                >
                    <Dialog.Body>
                        <Form className="layout-section__info-form"
                            model={this.state.section} labelWidth="150" rules={this.state.sectionRules}>
                            <Form.Item label={i18n.t("page.name")}>
                                <Input value={this.state.section.name}
                                    onChange={val => this.formChange("name", val)}/>
                            </Form.Item>
                            {this.state.section.widths.filter(width => width.screenWidth !== "lg").map(width =>
                                <Form.Item key={this.state.section.id + width.screenWidth}
                                    label={width.screenWidth} prop="width">
                                    <Select value={width.width} onChange={value => this.changeWidth(width.screenWidth, value)}>
                                        <Select.Option key="auto" value={null} label={i18n.t("page.auto")}></Select.Option>
                                        {this.state.widthOptions.map(widthOption =>
                                            <Select.Option key={widthOption} value={widthOption} label={widthOption}></Select.Option>
                                        )}
                                    </Select>
                                </Form.Item>
                            )}
                        </Form>
                    </Dialog.Body>
                    <Dialog.Footer className="dialog-footer">
                        <Button onClick={() => this.cancel()}>取 消</Button>
                        <Button type="primary" onClick={() => this.save()}>确 定</Button>
                    </Dialog.Footer>
                </Dialog>
                }
            </div>
        );
    }
}

TemplateGridEditor.propTypes = {
    section: {
        id: PropTypes.require,
        children: PropTypes.array,
        components: PropTypes.array
    },
    onChange: PropTypes.func,
    onCancel: PropTypes.func
};