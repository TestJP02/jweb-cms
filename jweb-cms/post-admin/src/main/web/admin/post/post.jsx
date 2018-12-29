import React from "react";
import {Route, RouteExtension} from "react-router-dom";
import PostList from "./post.list";
import PostUpdate from "./post.update";
import CategoryUpdate from "./post.category.update";
import CategoryList from "./post.category.list";
import TagList from "./tag.list";
import TagUpdate from "./tag.update";

import "./post-edit.css";
import "./post.css";

const module = window.app.bundle("postBundle");

Object.assign(module, {
    components: [],
    addComponent: function (component) {
        this.components.push(component);
    },
    component: function (name) {
        for (let i = 0; i < this.components.length; i += 1) {
            if (this.components[i].name === name) {
                return this.components[i].component;
            }
        }
        return null;
    }
});

export default function Post() {
    return (
        <div>
            <Route path="/admin/post/list" component={PostList}/>
            <Route path="/admin/post/create" component={PostUpdate}/>
            <Route path="/admin/post/:postId/draft" component={PostUpdate}/>
            <Route path="/admin/post/draft/:id" component={PostUpdate}/>
            <Route path="/admin/post/category/list" component={CategoryList}/>
            <Route path="/admin/post/category/create/parent/:parentId" component={CategoryUpdate}/>
            <Route path="/admin/post/category/:id/update" component={CategoryUpdate}/>
            <Route path="/admin/post/tag/list" component={TagList}/>
            <Route path="/admin/post/tag/:id/update" component={TagUpdate}/>
            <Route path="/admin/post/tag/create" component={TagUpdate}/>
            <RouteExtension bundle="postBundle"/>
        </div>
    );
}

