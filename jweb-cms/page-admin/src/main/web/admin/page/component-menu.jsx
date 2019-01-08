import React from "react";
import PropTypes from "prop-types";
import {Card, Menu} from "element-react";
import uuid from "react-native-uuid";

import "./component-selector.css";

const i18n = window.i18n;
export default class ComponentMenu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selectorShown: false,
            componentFilter: "",
            hideOnClick: false,
            showMenu: true,
            visible: false
        };
    }

    createComponent(componentId) {
        const {componentOptions} = this.props;
        for (let i = 0; i < componentOptions.length; i += 1) {
            const component = componentOptions[i];
            if (component.name === componentId) {
                const id = component.savedComponent ? component.id : uuid.v4();
                return {
                    id,
                    name: component.name,
                    attributes: component.attributes
                };
            }
        }
    }

    resetSelect() {
        this.setState({showMenu: false}, () => {
            this.setState({showMenu: true});
        });
    }

    onVisibleChange(visible) {
        this.setState({visible: visible});
    }

    onSelect(componentId) {
        if (componentId) {
            this.props.onSelect(this.createComponent(componentId));
        }
        this.resetSelect();
        this.setState({visible: false});
    }

    componentShouldDisplay(component) {
        return !this.state.componentFilter || this.componentDisplayName(component).toLowerCase().indexOf(this.state.componentFilter.toLowerCase()) >= 0;
    }

    component(name) {
        const {componentOptions} = this.props;
        for (let i = 0; i < componentOptions.length; i += 1) {
            const component = componentOptions[i];
            if (name === component.name) {
                return component;
            }
        }
        return null;
    }

    componentDisplayName(component) {
        if (component.displayName) {
            return component.displayName;
        }
        const comp = this.component(component.name);
        if (comp && comp.displayName) {
            return comp.displayName;
        }
        return component.name;
    }

    changeComponentFilter(componentFilter) {
        this.setState({componentFilter});
    }

    render() {
        const {componentOptions} = this.props;
        return <Card header={i18n.t("page.components")}>
            <Menu mode="vertical" defaultActive="1" className="component-selector" onSelect={componentName => this.onSelect(componentName)}>
                {componentOptions.map(component =>
                    <Menu.Item index={component.name} key={component.name}>{this.componentDisplayName(component)}<i className="fa fa-plus"></i></Menu.Item>)
                }
            </Menu>
        </Card>;
    }
}


ComponentMenu.propTypes = {
    componentOptions: PropTypes.array,
    disabled: PropTypes.bool,
    value: PropTypes.string,
    onSelect: PropTypes.func
};