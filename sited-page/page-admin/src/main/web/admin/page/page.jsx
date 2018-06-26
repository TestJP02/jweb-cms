import React from "react";
import {Route, RouteExtension} from "react-router-dom";
import PageList from "./page.list";
import PageUpdate from "./page.update";
import CategoryUpdate from "./page.category.update";
import CategoryList from "./page.category.list";
import VariableList from "./page.variable.list";
import VariableUpdate from "./page.variable.update";
import TemplateList from "./template.list";
import TemplateUpdate from "./template.update";
import ComponentList from "./page.component.list";
import ComponentUpdate from "./page.component.update";
import TagList from "./tag.list";
import TagUpdate from "./tag.update";
import CommentList from "./page.comment.list";

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
            <Route path="/admin/page/list" component={PageList}/>
            <Route path="/admin/page/create" component={PageUpdate}/>
            <Route path="/admin/page/:pageId/draft" component={PageUpdate}/>
            <Route path="/admin/page/:pageId/comments" component={CommentList}/>
            <Route path="/admin/page/draft/:id" component={PageUpdate}/>
            <Route path="/admin/page/category/list" component={CategoryList}/>
            <Route path="/admin/page/category/create/parent/:parentId" component={CategoryUpdate}/>
            <Route path="/admin/page/category/:id/update" component={CategoryUpdate}/>
            <Route path="/admin/page/variable/list" component={VariableList}/>
            <Route path="/admin/page/variable/create" component={VariableUpdate}/>
            <Route path="/admin/page/variable/:id/update" component={VariableUpdate}/>
            <Route path="/admin/page/template/list" component={TemplateList}/>
            <Route path="/admin/page/template/create" component={TemplateUpdate}/>
            <Route path="/admin/page/template/:id/update" component={TemplateUpdate}/>
            <Route path="/admin/page/component/list" component={ComponentList}/>
            <Route path="/admin/page/component/:id/update" component={ComponentUpdate}/>
            <Route path="/admin/page/component/create" component={ComponentUpdate}/>
            <Route path="/admin/page/tag/list" component={TagList}/>
            <Route path="/admin/page/tag/:id/update" component={TagUpdate}/>
            <Route path="/admin/page/tag/create" component={TagUpdate}/>
            <RouteExtension bundle="pageBundle"/>
        </div>
    );
}

