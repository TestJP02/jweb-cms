import React from "react";
import {Route} from "react-router-dom";

import FileList from "./file.list";
import FileUpdate from "./file.update";
import DirectoryUpdate from "./directory.update";
import DirectoryList from "./directory.list";
import "./file.css";

export default function File() {
    return (
        <div>
            <Route path="/admin/file/list" component={FileList}/>
            <Route path="/admin/file/directory/list" component={DirectoryList}/>
            <Route path="/admin/file/create" component={FileUpdate}/>
            <Route path="/admin/file/:id/update" component={FileUpdate}/>
            <Route path="/admin/file/directory/:parentId/create" component={DirectoryUpdate}/>
            <Route path="/admin/file/directory/:id/update" component={DirectoryUpdate}/>
        </div>
    );
}