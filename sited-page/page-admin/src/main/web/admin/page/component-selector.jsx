
import React from "react";
import PropTypes from "prop-types";
import {Select} from "element-react";
import uuid from "react-native-uuid";

import "./component-selector.css";

export default class ComponentSelector extends React.Component {
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
        const {componentOptions, disabled} = this.props;
        return <div className="component-selector">
            {
                !this.state.visible && <div className="component-selector__bg"><i className="fa fa-plus"/></div>
            }

            {this.state.showMenu && <Select className="page-grid-editor__add"
                ref={(select) => {
                    this.select = select;
                }}
                placeholder="  "
                disabled={disabled}
                filterable={true}
                clearable={true}
                onVisibleChange={visible => this.onVisibleChange(visible)}
                onChange={componentName => this.onSelect(componentName)}>
                {componentOptions.map(component => <Select.Option key={component.name}
                    label={this.componentDisplayName(component)}
                    value={component.name}/>)}
            </Select>
            }
        </div>;
    }
}


ComponentSelector.propTypes = {
    componentOptions: PropTypes.array,
    disabled: PropTypes.bool,
    value: PropTypes.string,
    onSelect: PropTypes.func
};