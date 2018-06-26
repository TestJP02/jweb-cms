import React, {Component} from "react";
import {Route} from "react-router-dom";
import PropTypes from "prop-types";

export default class RouteExtension extends Component {
    constructor(props) {
        super(props);
        this.state = {routes: []};
    }

    componentDidMount() {
        window.console.log(this.props.bundle);

        const menu = window.app.bundle(this.props.bundle).menu;
        const routes = [];
        if (menu && menu.children) {
            menu.children.forEach((item) => {
                if (item.bundleName && window[item.bundleName]) {
                    routes.push({
                        path: item.path,
                        component: window[item.bundleName].default
                    });
                }
            });
        }
        this.setState({routes});
    }

    render() {
        return this.state.routes.map(route => <Route key={route.path} path={route.path} component={route.component}/>);
    }
}

RouteExtension.propTypes = {bundle: PropTypes.string};