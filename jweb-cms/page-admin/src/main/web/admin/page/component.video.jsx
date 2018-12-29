import React from "react";
import PropTypes from "prop-types";
import {Button, Dialog, Form, Input, Layout} from "element-react";

import "./component.video.css";

const i18n = window.i18n;

export default class VideoComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            mode: props.mode,
            component: props.component,
            isNew: false
        };
    }

    componentWillMount() {
        if (!this.state.component.attributes.innerText && this.state.component.attributes.innerText !== "") {
            this.setState({isNew: true});
        }
        this.videoMap = {};
    }

    componentWillReceiveProps(props) {
        this.setState({
            mode: props.mode,
            component: props.component
        });
    }

    formChange(name, value) {
        const component = this.state.component;
        component.attributes[name] = this.videoURL(value);
        this.setState({component});
    }

    videoURL(url) {
        if (url.includes("https://www.youtube.com/embed/")) {
            return url;
        } else if (url.includes("https://youtu.be/")) {
            return "https://www.youtube.com/embed/" + url.substring("https://youtu.be/".length);
        } else if (url.includes("https://www.youtube.com/watch")) {
            return "https://www.youtube.com/embed/" + this.getQueryString("v", url);
        }
        return url;
    }

    getQueryString(field, url) {
        const reg = new RegExp("[?&]" + field + "=([^&#]*)", "i");
        const groups = reg.exec(url);
        return groups ? groups[1] : null;
    }

    isYoutubeURL(url) {
        return url && (url.includes("www.youtube.com") || url.includes("youtu.be"));
    }

    isStreamURL(url) {
        const reg = new RegExp(/\.\w{3,4}$/);
        return url && reg.test(url);
    }

    onChange() {
        this.setState({isNew: false});
        this.props.onChange(this.state.component);
    }

    renderVideo() {
        const url = this.state.component.attributes.src;
        if (this.isYoutubeURL(url)) {
            return <iframe title="YouTube video player" type="text/html"
                width="640" height="390" src={url}
                frameBorder="0" ></iframe>;
        } else if (this.isStreamURL(url)) {
            return <video width="480" style={{"max-width": "100%"}}
                ref={(ref) => {
                    this.videoMap[url] = ref;
                }}
                onClick={(event) => {
                    const video = this.videoMap[url];
                    if (video.paused) {
                        video.play();
                    } else {
                        video.pause();
                    }
                }}>
                <source src={url} type={`video/${url.substring(url.lastIndexOf(".") + 1, url.length)}`} />
                Your browser does not support HTML5 video.
            </video>;
        }
    }

    render() {
        return <div className="page-component">
            {(this.state.mode === "edit" || this.state.isNew) &&
                <Dialog visible={true} onCancel={() => this.props.onChange(this.state.component)} title={"Insert video"}>
                    <Dialog.Body>
                        <Form>
                            <Layout.Row>
                                <Layout.Col span="24">
                                    <Form.Item label={i18n.t("page.src")} labelWidth="120">
                                        <Input value={this.state.component.attributes.src}
                                            onChange={value => this.formChange("src", value)}></Input>
                                    </Form.Item>
                                </Layout.Col>
                            </Layout.Row>
                        </Form>
                    </Dialog.Body>

                    <Dialog.Footer className="dialog-footer">
                        <Button type="primary" onClick={() => this.onChange()}>{i18n.t("page.save")}</Button>
                    </Dialog.Footer>
                </Dialog>
            }
            <div className="video-component">
                {this.renderVideo()}
            </div>
        </div>;
    }
}

VideoComponent.propTypes = {
    component: PropTypes.object,
    onChange: PropTypes.func,
    mode: PropTypes.string
};