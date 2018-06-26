import React from "react";
import PropTypes from "prop-types";
import {Tree} from "element-react";

export default class PageTree extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: props.data,
            options: props.options,
            static: props.static,
            highlightCurrent: props.highlightCurrent,
            onNodeClicked: props.onNodeClicked
        };
    }

    onNodeClicked(data, reactElement) {
        this.state.onNodeClicked(data, reactElement);
    }

    shouldComponentUpdate() {
        return !this.state.static;
    }

    render() {
        return <Tree
            data={this.state.data}
            options={this.state.options}
            highlightCurrent={true}
            onNodeClicked={(data, reactElement) => this.onNodeClicked(data, reactElement)}
        />;
    }
}

PageTree.propTypes = {
    data: PropTypes.array,
    options: PropTypes.object,
    static: PropTypes.bool,
    highlightCurrent: PropTypes.bool,
    onNodeClicked: PropTypes.func
};