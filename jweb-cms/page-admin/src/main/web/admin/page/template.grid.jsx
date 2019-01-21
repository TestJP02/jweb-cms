import React from "react";
import {Button, Card, Layout, MessageBox, Radio} from "element-react";
import ReactGridLayout from "react-grid-layout";
import PropTypes from "prop-types";
import uuid from "react-native-uuid";
import DefaultComponent from "./component.default";
import ErrorComponent from "./component.error";
import TemplateGridEditor from "./template.grid.editor";
import "../../node_modules/react-grid-layout/css/styles.css";
import "../../node_modules/react-resizable/css/styles.css";

import "./template.grid.css";
import ComponentMenu from "./component-menu";

const bundle = window.app.bundle("pageBundle");

const i18n = window.i18n;

export default class LayoutGridEditor extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            sections: props.sections ? props.sections : [],
            sectionMap: {},
            layout: [],
            layoutComponents: {},
            componentOptions: [],
            onChange: props.onChange,
            dom: null,
            rowHeight: 10,
            responsiveMode: "md"
        };
        fetch("/admin/api/page/component/section", {method: "GET"}).then((response) => {
            const componentOptions = response;
            for (let i = 0; i < componentOptions.length; i += 1) {
                const component = window.app.bundle("pageBundle").component(componentOptions[i].componentName);
                if (component) {
                    componentOptions[i].component = component;
                }
            }
            this.setState({componentOptions});
        });
    }

    componentDidMount() {
        this.refreshLayout();
    }

    refreshLayout() {
        const layout = this.fromTemplate(this.state.sections);
        this.setState({layout});
    }

    onLayoutChange(layout) {
        try {
            const templateSections = this.toTemplateSections(layout, 12);
            const sections = templateSections.map(section => this.toLayoutSection(section));

            for (let i = 0; i < sections.length; i += 1) {
                const section = sections[i];
                if (section.components && section.components.length === 1) {
                    const component = this.component(section.components[0].name);
                    if (component) {
                        section.name = component.componentName;
                    } else {
                        section.name = section.components[0].name;
                    }
                } else {
                    section.name = "section";
                }
            }
            this.setState({sections});
            this.state.onChange(sections);
        } catch (e) {
            window.console.error(e);
        }
        this.setState({layout});
    }

    toLayoutSection(section) {
        const layoutSection = Object.assign({}, {
            id: section.i,
            components: this.state.layoutComponents[section.i],
            children: section.children.map(child => this.toLayoutSection(child)),
            wrapper: section.wrapper
        });
        if (this.getSection(layoutSection.id) && this.getSection(layoutSection.id).widths) {
            layoutSection.widths = this.getSection(layoutSection.id).widths.filter(width => width.screenWidth !== this.state.responsiveMode);
        } else {
            layoutSection.widths = ["xs", "sm", "md"].filter(width => width !== this.state.responsiveMode).map(width => ({
                screenWidth: width,
                width: 12
            }));
        }
        layoutSection.widths.push({
            screenWidth: this.state.responsiveMode,
            x: section.x,
            y: section.y,
            width: section.w,
            height: section.h
        });
        this.putSection(layoutSection);
        return layoutSection;
    }

    getSection(sectionId) {
        return this.state.sectionMap[sectionId];
    }

    putSection(section) {
        const sectionMap = this.state.sectionMap;
        sectionMap[section.id] = section;
        this.setState({sectionMap});
    }

    toTemplateSections(layoutSections, columns) {
        const templateSections = [];
        let wrappers = [];
        const sections = this.sort(layoutSections);

        for (let i = 0; i < sections.length;) {
            const sameYSections = this.sameY(i, sections);
            if (this.isFullWidth(sameYSections, columns) || i === 0) { // Or first row
                const wrapper = this.createWrapper(sameYSections, null);
                templateSections.push(wrapper);
                wrappers = wrapper.children.length ? wrapper.children : [wrapper];
            } else {
                const matchedWrappers = this.findWrappers(sameYSections, wrappers);
                if (matchedWrappers.length === wrappers.length) { // new row
                    const wrapper = this.createWrapper(sameYSections, null);
                    templateSections.push(wrapper);
                    wrappers = wrapper.children.length ? wrapper.children : [wrapper];
                } else if (matchedWrappers.length === 1) {
                    const parent = matchedWrappers[0].parent;
                    const parentWrapper = this.wrap(matchedWrappers, parent);
                    const childWrapper = this.createWrapper(sameYSections, parentWrapper);
                    parentWrapper.children.push(childWrapper);
                    this.replaceChildren(parent, matchedWrappers, [parentWrapper]);
                } else {
                    const parent = matchedWrappers[0].parent;
                    const parentWrapper = this.wrap(matchedWrappers, parent);
                    const matchedChildWrapper = this.wrap(matchedWrappers, parentWrapper);
                    const childWrapper = this.createWrapper(sameYSections, parentWrapper);
                    parentWrapper.children = [matchedChildWrapper, childWrapper];

                    this.replaceChildren(parent, matchedWrappers, [parentWrapper]);
                    wrappers = this.replaceWrapper(wrappers, matchedWrappers, parentWrapper);
                }
            }
            i += sameYSections.length;
        }
        return templateSections;
    }

    fromTemplate(sections) {
        const sectionMap = this.state.sectionMap;
        let layoutSections = [];

        window.console.log(JSON.stringify(sections));

        for (let i = 0; i < sections.length; i += 1) {
            const section = sections[i];
            sectionMap[section.id] = section;
            if (section.wrapper || section.children.length > 0) {
                layoutSections = layoutSections.concat(this.fromTemplate(section.children));
            } else {
                const layoutComponents = this.state.layoutComponents;
                layoutComponents[section.id] = section.components;
                this.setState({layoutComponents});
                const layoutSection = {
                    i: section.id,
                    x: 0,
                    y: 0,
                    w: 12,
                    h: 4,
                    minH: 4,
                    widths: section.widths
                };
                const width = this.getScreenWidth(section.widths, this.state.responsiveMode);
                if (width) {
                    Object.assign(layoutSection, {
                        x: width.x ? width.x : 0,
                        y: width.y ? width.y : 0,
                        w: width.width ? width.width : 12,
                        h: width.height ? width.height : 4
                    });
                }
                window.console.log(JSON.stringify(layoutSection));
                layoutSections.push(layoutSection);
            }
        }
        this.setState({sectionMap});
        return layoutSections;
    }

    getScreenWidth(widths, name) {
        for (let j = 0; j < widths.length; j += 1) {
            if (widths[j].screenWidth === name) {
                return widths[j];
            }
        }
    }

    createWrapper(layoutSections, parent) {
        if (layoutSections.length === 1) {
            return {
                i: layoutSections[0].i,
                wrapper: false,
                children: [],
                parent: parent,
                x: layoutSections[0].x,
                w: layoutSections[0].w,
                y: layoutSections[0].y,
                h: layoutSections[0].h,
                minH: 4
            };
        }
        const end = layoutSections[layoutSections.length - 1].x + layoutSections[layoutSections.length - 1].w;
        const wrapper = {
            i: uuid.v4(),
            wrapper: true,
            children: [],
            parent: parent,
            x: layoutSections[0].x,
            w: end - layoutSections[0].x,
            y: layoutSections[0].y,
            h: layoutSections[0].h,
            minH: 4
        };
        wrapper.children = layoutSections.map(section => this.toSection(section, wrapper));
        return wrapper;
    }

    wrap(wrappers, parent) {
        if (wrappers.length === 1 && wrappers[0].wrapper) {
            return wrappers[0];
        }
        const end = wrappers[wrappers.length - 1].x + wrappers[wrappers.length - 1].w;
        const wrapper = {
            i: uuid.v4(),
            wrapper: true,
            children: [],
            parent: parent,
            x: wrappers[0].x,
            w: end - wrappers[0].x,
            y: wrappers[0].y,
            h: wrappers[0].h,
            minH: 4
        };
        wrapper.children = wrappers.map((current) => {
            current.parent = wrapper;
            return current;
        });
        return wrapper;
    }

    toSection(layoutSection, parent) {
        return {
            i: layoutSection.i,
            parent: parent,
            wrapper: false,
            children: [],
            x: layoutSection.x,
            y: layoutSection.y,
            w: layoutSection.w,
            h: layoutSection.h,
            minH: 4
        };
    }

    sameY(start, sections) {
        const y = sections[start].y;
        const sameYSections = [];
        for (let i = start; i < sections.length; i += 1) {
            if (sections[i].y === y) {
                sameYSections.push(sections[i]);
            } else {
                break;
            }
        }
        return sameYSections;
    }

    isFullWidth(sections, columns) {
        const totalWidth = sections.reduce((section, sum) => section.w + sum, 0);
        return totalWidth === columns;
    }

    sort(layout) {
        for (let i = 0; i < layout.length - 1; i += 1) {
            let index = i;
            for (let j = i + 1; j < layout.length; j += 1) {
                const section = layout[j];
                if (section.y < layout[index].y) {
                    index = j;
                } else if (section.y === layout[index].y && section.x < layout[index].x) {
                    index = j;
                }
            }

            const value = layout[i];
            layout[i] = layout[index];
            layout[index] = value;
        }
        return layout;
    }

    findWrappers(group, wrappers) {
        const matchedWrappers = [];
        const startX = group[0].x;
        const endX = group[group.length - 1].x + group[group.length - 1].w;
        for (let i = 0; i < wrappers.length; i += 1) {
            const wrapper = wrappers[i];
            if (wrapper.x >= startX && wrapper.x < endX) {
                matchedWrappers.push(wrapper);
            } else if (wrapper.x + wrapper.w > startX && wrapper.x + wrapper.w <= endX) {
                matchedWrappers.push(wrapper);
            }
        }
        return matchedWrappers;
    }

    replaceChildren(parent, children, target) {
        for (let i = 0; i < parent.children.length; i += 1) {
            if (parent.children[i] === children[0]) {
                parent.children.splice(i, children.length, ...target);
            }
        }
    }

    setParent(sections, parent) {
        for (let i = 0; i < sections.length; i += 1) {
            sections[i].parent = parent;
        }
    }

    replaceWrapper(wrappers, matchedWrapper, wrapper) {
        for (let i = 0; i < wrappers.length; i += 1) {
            if (wrappers[i] === matchedWrapper[0]) {
                wrappers.splice(i, matchedWrapper.length, wrapper);
            }
        }
    }

    removeSection(sectionId) {
        MessageBox.confirm(i18n.t("page.gridDeleteConfirm"), i18n.t("page.warning"), {type: "error"}).then(() => {
            const layout = this.state.layout;
            this.setState({layout: layout.filter(section => section.i !== sectionId)});
        });
    }

    selectComponent(component) {
        const layout = this.state.layout;
        let y = 0;
        layout.map((section) => {
            if (section.y + section.h > y) {
                y = section.y + section.h;
            }
        });
        const grid = {
            i: uuid.v4(),
            x: 0,
            y: y + 1,
            w: 12,
            h: 4,
            minH: 4
        };
        const layoutComponents = this.state.layoutComponents;
        layoutComponents[grid.i] = [component];
        this.setState({
            layout: this.state.layout.concat([grid]),
            layoutComponents
        });
    }

    renderComponent(componentValue, section) {
        if (componentValue.id === this.state.editingComponentId) {
            window.console.log("edit component " + this.state.editingComponentId);
            return this.editComponent(componentValue, section);
        }

        const component = this.component(componentValue.name);
        if (component) {
            if (component.component) {
                return React.createElement(component.component, {
                    component: componentValue,
                    readOnly: this.state.readOnly,
                    onChange: value => this.componentChanged(value)
                });
            }
            return <DefaultComponent component={componentValue} responsiveMode={this.state.responsiveMode}/>;
        }
        return <ErrorComponent component={componentValue} responsiveMode={this.state.responsiveMode}/>;
    }

    editComponent(component, section) {
        return React.createElement(bundle.component(component.name), {
            component: component,
            readOnly: this.state.readOnly,
            mode: "edit",
            onChange: value => this.componentChanged(value)
        });
    }

    component(name) {
        for (let i = 0; i < this.state.componentOptions.length; i += 1) {
            const component = this.state.componentOptions[i];
            if (name === component.name) {
                return component;
            }
        }
        return null;
    }

    isComponentEditable(name) {
        const component = this.component(name);
        return component && component.component && !component.savedComponent;
    }

    componentChanged(component) {
        const layoutComponents = this.state.layoutComponents;
        layoutComponents[this.state.editingSectionId] = [component];
        const sectionId = this.state.editingSectionId;
        this.setState({
            layoutComponents,
            editingComponentId: null,
            editingSectionId: null,
            changed: true
        }, () => {
            const layout = this.state.layout;
            for (let i = 0; i < layout.length; i += 1) {
                const section = layout[i];
                if (section.i === sectionId) {
                    this.resizeSection(section);
                    return;
                }
            }
        });
    }

    componentFocused(section) {
        if (this.state.responsiveMode === "md") {
            if (this.state.editingSectionId !== section.i) {
                if (this.isComponentEditable(this.state.layoutComponents[section.i][0].name)) {
                    this.setState({
                        editingComponentId: this.state.layoutComponents[section.i][0].id,
                        editingSectionId: section.i,
                        elements: {}
                    }, () => this.resizeSection(section));
                }
            }
        }
    }

    resizeSection(section) {
        const dom = window.document.getElementById(section.i);
        const domHeight = this.findSectionBody(dom, "page-grid-editor__grid-body").clientHeight;

        const minH = parseInt(domHeight / (this.state.rowHeight + 10), 10) + 3;
        window.console.log("height=" + domHeight + "; minH=" + minH);

        if (section.h !== minH) {
            const heightChanged = minH - section.h;
            section.minH = minH;
            section.h = minH;
            const layout = this.adjustY(this.state.layout, section.y, heightChanged);
            for (let i = 0; i < layout.length; i += 1) {
                const layoutSection = layout[i];
                if (layoutSection.i === section.i) {
                    window.console.log("update section height");
                    layout[i] = section;
                    this.setState({layout: this.state.layout.filter(child => child.i !== section.i)}, () => this.setState({layout}));
                }
            }
        }
    }

    adjustY(layout, y, height) {
        for (let i = 0; i < layout.length; i += 1) {
            const layoutSection = layout[i];
            if (layoutSection.y > y) {
                layoutSection.y += height;
                window.console.log("update section " + layoutSection.i + ", y=" + layoutSection.y);
            }
        }
        return layout;
    }

    findSectionBody(dom, className) {
        for (let i = 0; i < dom.childNodes.length; i += 1) {
            if (dom.childNodes[i].className === className) {
                return dom.childNodes[i];
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

    updateSection(updatingSection) {
        const section = this.getSection(this.state.updatingSectionId);
        if (section.id === updatingSection.id) {
            section.widths = updatingSection.widths;
            section.name = updatingSection.name;
            this.putSection(section);
            this.setState({updatingSectionId: null});
        }
    }

    cancelCreate() {
        this.setState({updatingSectionId: null});
    }

    onResize(layout, oldItem, newItem) {
        this.setState({
            currentI: newItem.i,
            currentWidth: newItem.w
        });
    }

    onResizeStop() {
        this.setState({
            currentI: null,
            currentWidth: 0
        });
    }

    changeResponsive(mode) {
        this.setState({responsiveMode: mode}, () => this.refreshLayout());
    }

    render() {
        return (
            <Layout.Row className="el-form-group" gutter="30">
                <Layout.Col span="6">
                    <div className="page-grid-editor__header">
                        <ComponentMenu componentOptions={this.state.componentOptions}
                            onSelect={component => this.selectComponent(component)}/>
                    </div>
                </Layout.Col>
                <Layout.Col span="18">
                    <div className="page-grid-editor__body">
                        <Radio.Group value={this.state.responsiveMode} onChange={value => this.changeResponsive(value)}>
                            <Radio.Button value="md"><i className="fa fa-desktop"></i></Radio.Button>
                            <Radio.Button value="sm"><i className="fa fa-tablet"></i></Radio.Button>
                            <Radio.Button value="xs"><i className="fa fa-mobile"></i></Radio.Button>
                        </Radio.Group>
                        <Card className={"page-grid-editor__card--" + this.state.responsiveMode}>
                            <div ref={dom => !this.state.dom && this.setState({dom})}>
                                {
                                    this.state.componentOptions.length &&
                                    <ReactGridLayout
                                        verticalCompact={true}
                                        className="layout"
                                        layout={this.state.layout}
                                        containerPadding={[0, 0]}
                                        draggableHandle=".page-grid-editor__grid-header"
                                        rowHeight={this.state.rowHeight}
                                        width={this.state.dom && this.state.dom.offsetWidth}
                                        onLayoutChange={layout => this.onLayoutChange(layout)}
                                        onResize={(layout, oldItem, newItem) => this.onResize(layout, oldItem, newItem)}
                                        onResizeStop={() => this.onResizeStop()}
                                        cols={12}>
                                        {this.state.layout.map((section, index) =>
                                            <div id={section.i} tabIndex={index} key={section.i} className="page-grid-editor__grid" onFocus={() => this.componentFocused(section)}>
                                                {this.state.currentI === section.i &&
                                                <div className="page-grid-editor__ruler" data-width={this.state.currentWidth}></div>
                                                }
                                                <div className="page-grid-editor__grid-header">
                                                    <span className="page-grid-editor__grid-header-title">{this.componentDisplayName(this.state.layoutComponents[section.i][0])}</span>
                                                    <Button className="page-grid-editor__grid-operation" type="text" icon="close"
                                                        onClick={() => this.removeSection(section.i)}></Button>
                                                </div>
                                                <div className="page-grid-editor__grid-body">
                                                    {this.state.layoutComponents[section.i] &&
                                                    this.state.layoutComponents[section.i][0] &&
                                                    this.renderComponent(this.state.layoutComponents[section.i][0], section)}
                                                </div>
                                            </div>
                                        )}
                                    </ReactGridLayout>
                                }
                                {this.state.updatingSectionId &&
                                <TemplateGridEditor section={this.getSection(this.state.updatingSectionId)}
                                    onChange={section => this.updateSection(section)}
                                    onCancel={() => this.cancelCreate()}
                                    template={this.state.template}/>
                                }
                            </div>
                        </Card>
                    </div>
                </Layout.Col>
            </Layout.Row>
        );
    }
}

LayoutGridEditor.propTypes = {
    sections: PropTypes.array,
    onChange: PropTypes.func
};