import React from "react";
import {Route, RouteExtension} from "react-router-dom";
import VariableList from "./page.variable.list";
import VariableUpdate from "./page.variable.update";
import TemplateList from "./template.list";
import TemplateUpdate from "./template.update";
import ComponentList from "./page.component.list";
import ComponentUpdate from "./page.component.update";

import CategoryList from "./page.category.list";
import CategoryUpdate from "./page.category.update";

import PageDownloadComponent from "./component.download";
import PageImageComponent from "./component.image";
import PageCodeComponent from "./component.code";
import VideoComponent from "./component.video";
import PageTableComponent from "./component.table";
import PageImageSlideComponent from "./component.image-slide";
import PageAuthorComponent from "./component.author";
import PageBannerComponent from "./component.banner";
import PageCategoryTitleComponent from "./component.category.title";
import PageFooterComponent from "./component.footer";
import PageHeaderComponent from "./component.header";
import PageLinkListComponent from "./component.link-list";
import PageHotLinkListComponent from "./component.hot-link-list";
import PageListComponent from "./component.page-list";
import PageTitleComponent from "./component.page-title";
import PageHtmlComponent from "./component.html";
import PageItemGridsComponent from "./component.item-grids";
import PageItemListComponent from "./component.item-list";
import PageTextComponent from "./component.text";
import PageTeamComponent from "./component.team";
import PageContactComponent from "./component.contact";
import PageBannerSingleComponent from "./component.banner-single";
import PageBreadcrumbComponent from "./component.breadcrumb";
import PageCategoryTreeComponent from "./component.categoryTree";
import PageCardComponent from "./component.card";


import "./page-edit.css";
import "./page.css";

const module = window.app.bundle("pageBundle");

Object.assign(module, {
    components: [],
    addComponent: function(component) {
        this.components.push(component);
    },
    component: function(name) {
        for (let i = 0; i < this.components.length; i += 1) {
            if (this.components[i].name === name) {
                return this.components[i].component;
            }
        }
        return null;
    }
});

module.addComponent({
    name: "download",
    component: PageDownloadComponent
});

module.addComponent({
    name: "image",
    component: PageImageComponent
});

module.addComponent({
    name: "code",
    component: PageCodeComponent
});

module.addComponent({
    name: "video",
    component: VideoComponent
});

module.addComponent({
    name: "table",
    component: PageTableComponent
});

module.addComponent({
    name: "image-slide",
    component: PageImageSlideComponent
});
module.addComponent({
    name: "author",
    component: PageAuthorComponent
});
module.addComponent({
    name: "banner",
    component: PageBannerComponent
});
module.addComponent({
    name: "category-header",
    component: PageCategoryTitleComponent
});
module.addComponent({
    name: "footer",
    component: PageFooterComponent
});

module.addComponent({
    name: "header",
    component: PageHeaderComponent
});
module.addComponent({
    name: "link-list",
    component: PageLinkListComponent
});
module.addComponent({
    name: "hot-link-list",
    component: PageHotLinkListComponent
});

module.addComponent({
    name: "list",
    component: PageListComponent
});

module.addComponent({
    name: "title",
    component: PageTitleComponent
});
module.addComponent({
    name: "html",
    component: PageHtmlComponent
});

module.addComponent({
    name: "item-grids",
    component: PageItemGridsComponent
});
module.addComponent({
    name: "item-list",
    component: PageItemListComponent
});
module.addComponent({
    name: "text",
    component: PageTextComponent
});
module.addComponent({
    name: "team",
    component: PageTeamComponent
});
module.addComponent({
    name: "contact",
    component: PageContactComponent
});
module.addComponent({
    name: "banner-single",
    component: PageBannerSingleComponent
});
module.addComponent({
    name: "breadcrumb",
    component: PageBreadcrumbComponent
});

module.addComponent({
    name: "category-tree",
    component: PageCategoryTreeComponent
});

module.addComponent({
    name: "card",
    component: PageCardComponent
});

module.addComponent({
    name: "page-list",
    component: PageListComponent
});

export default function Page() {
    return (
        <div>
            <Route path="/admin/page/variable/list" component={VariableList}/>
            <Route path="/admin/page/variable/create" component={VariableUpdate}/>
            <Route path="/admin/page/variable/:id/update" component={VariableUpdate}/>
            <Route path="/admin/page/category/list" component={CategoryList}/>
            <Route path="/admin/page/category/create/parent/:parentId" component={CategoryUpdate}/>
            <Route path="/admin/page/category/:id/update" component={CategoryUpdate}/>
            <Route path="/admin/page/list" component={TemplateList}/>
            <Route path="/admin/page/create" component={TemplateUpdate}/>
            <Route path="/admin/page/:id/update" component={TemplateUpdate}/>
            <Route path="/admin/page/component/list" component={ComponentList}/>
            <Route path="/admin/page/component/:id/update" component={ComponentUpdate}/>
            <Route path="/admin/page/component/create" component={ComponentUpdate}/>
            <RouteExtension bundle="pageBundle"/>
        </div>
    );
}

