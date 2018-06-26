import React from "react";
import PropTypes from "prop-types";
import {Button, Carousel, Dialog, Form, Input, Layout} from "element-react";
import FileBrowser from "./page.file.browser";

const i18n = window.i18n;
export default class PageBannerComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            component: props.component,
            onChange: props.onChange,
            mode: props.mode,
            current: {attribute: {}},
            previewVisible: false,
            previewURL: null,
            uploading: false,
            index: null,
            isChangingFile: false
        };
    }

    componentWillMount() {
        const component = this.state.component;
        if (!component.attributes.banners) {
            component.attributes.banners = [];
            this.setState({
                component,
                isChangingFile: true
            });
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState({mode: nextProps.mode});
    }

    add() {
        this.editImage(null, {});
    }

    editImage(index, image) {
        if (!image.attribute) {
            image.attribute = {};
        }
        this.setState({
            current: image,
            index: index
        });
    }

    view(index, image) {
        this.setState({
            previewVisible: true,
            previewURL: image.src
        });
    }

    delete(index) {
        const component = this.state.component;
        component.attributes.banners.splice(index, 1);
        this.setState({component}, () => {
            this.saveComponent();
        });
        if (this.state.index === index) {
            this.setState({
                index: null,
                current: {attibute: {}}
            });
        } else if (this.state.index > index) {
            this.setState({index: this.state.index - 1});
        }
    }

    onChange() {
        this.saveComponent();
        this.setState({mode: "preview"});
    }

    saveComponent() {
        this.state.onChange(this.state.component);
    }

    formChange(key, val) {
        const keys = key.split(".");
        const current = this.state.current;
        let data = current;
        keys.forEach((thisKey, index) => {
            if (index === keys.length - 1) {
                data[thisKey] = val;
                return;
            }
            if (!data[thisKey]) {
                data[thisKey] = {};
            }
            data = data[thisKey];
        });
        this.setState({current: current});
    }

    saveCurrent() {
        const index = this.state.index;
        const component = this.state.component;
        if (index !== null && component.attributes.banners[index]) {
            component.attributes.banners[index] = this.state.current;
        }
        this.setState({component}, () => this.saveComponent());
    }

    imageExist(banner, banners) {
        for (let index = 0; index < banners.length; index += 1) {
            if (banners[index].src === banner.path) {
                return true;
            }
        }
        return false;
    }

    onFileChange(files) {
        const component = this.state.component;
        files.map((file) => {
            if (!this.imageExist(file, component.attributes.banners)) {
                const image = {src: file.path};
                component.attributes.banners.push(image);
            }
        });
        this.setState({
            component,
            isChangingFile: false
        }, () => this.saveComponent());
    }

    files() {
        const files = [];
        this.state.component.attributes.banners.map((image) => {
            const file = {path: image.src};
            files.push(file);
        });
        return files;
    }

    editor() {
        if (this.state.isChangingFile) {
            return <FileBrowser visible={true}
                files={this.files()}
                multiple={true}
                onChange={files => this.onFileChange(files)}/>;
        }
        return (
            <Dialog size="large"
                visible={true}
                onCancel={() => this.onChange()}
            >
                <Dialog.Body>
                    <Layout.Row gutter="10" className="page-image-slider__preview-section">
                        {this.state.component.attributes.banners.map(
                            (image, index) =>
                                <Layout.Col key={"image-" + index} span="3">
                                    <div className={(index === this.state.index ? "page-image-slider__image-container--editing " : "") + "page-image-slider__image-container"}>
                                        <div className="page-image-slider__image-preview"
                                            onClick={() => this.editImage(index, image)}>
                                            <img src={"/admin/file" + image.src} alt="" />
                                        </div>
                                        <div className="page-image-slider__image-name"
                                            onClick={() => this.editImage(index, image)}>{image.name}
                                        </div>
                                        <Button type="text" className="page-image-slider__image-delete el-icon-close"
                                            onClick={() => this.delete(index)}/>
                                    </div>
                                </Layout.Col>
                        )}
                        <Layout.Col key="add-image" span="3" className="">
                            <div className="page-image-slider__image-container page-image-slider__image-add">
                                <Button type="text" size="small"
                                    className="page-file-browser__add-button"
                                    onClick={() => this.setState({isChangingFile: true})}>+</Button>
                            </div>
                        </Layout.Col>
                    </Layout.Row>

                    <div className="page-image-slider-editor">
                        <Form ref={(form) => {
                            this.form = form;
                        }} model={this.state.current} labelWidth="120" rules={this.state.formRules}>
                            <Layout.Row>
                                <Layout.Col span="6">
                                    <Form.Item label={i18n.t("page.name")} props="name">
                                        <Input disabled={this.state.index === null} value={this.state.current.name} onChange={val => this.formChange("name", val)}/>
                                    </Form.Item>
                                </Layout.Col>
                                <Layout.Col span="6">
                                    <Form.Item label={i18n.t("page.alt")} props="alt">
                                        <Input disabled={this.state.index === null} value={this.state.current.alt} onChange={val => this.formChange("alt", val)}/>
                                    </Form.Item>
                                </Layout.Col>
                                <Layout.Col span="6">
                                    <Form.Item label={i18n.t("page.width")} props="attribute.width">
                                        <Input type="number" disabled={this.state.index === null} value={this.state.current.attribute.width}
                                            onChange={val => this.formChange("attribute.width", val)}/>
                                    </Form.Item>
                                </Layout.Col>
                                <Layout.Col span="6">
                                    <Form.Item label={i18n.t("page.height")} props="attribute.height">
                                        <Input type="number" disabled={this.state.index === null} value={this.state.current.attribute.height}
                                            onChange={val => this.formChange("attribute.height", val)}/>
                                    </Form.Item>
                                </Layout.Col>
                                <Layout.Col span="24">
                                    <Form.Item label={i18n.t("page.description")} props="description">
                                        <Input disabled={this.state.index === null} type="textarea" value={this.state.current.description} onChange={val => this.formChange("description", val)}/>
                                    </Form.Item>
                                </Layout.Col>
                                <Layout.Col span="24">
                                    <Form.Item label={i18n.t("page.link")} props="link">
                                        <Input disabled={this.state.index === null} value={this.state.current.link} onChange={val => this.formChange("link", val)}/>
                                    </Form.Item>
                                </Layout.Col>
                            </Layout.Row>
                        </Form>
                    </div>
                </Dialog.Body>

                <Dialog.Footer className="dialog-footer">
                    {this.state.index !== null &&
                    <Button type="primary" onClick={() => this.saveCurrent()}>{i18n.t("page.save")}</Button>
                    }
                    <Button onClick={() => this.onChange()}>{i18n.t("page.close")}</Button>
                </Dialog.Footer>
            </Dialog>
        );
    }

    render() {
        return (
            <div className={(this.state.mode === "edit" ? "editing " : "") + "page-image-slider"}>
                {this.state.mode === "edit"
                    ? this.editor()
                    : <div className="page-image-slider__preview">
                        <Carousel >
                            {
                                this.state.component.attributes.banners.map((image, index) =>
                                    <Carousel.Item key={index}>
                                        <img src={"/admin/file" + image.src} alt="" />
                                    </Carousel.Item>
                                )
                            }
                        </Carousel>
                    </div>
                }
            </div>
        );
    }
}

PageBannerComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};