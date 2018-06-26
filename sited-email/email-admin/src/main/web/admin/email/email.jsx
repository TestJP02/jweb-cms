import React from "react";
import {Route} from "react-router-dom";

import EmailList from "./email-list";
import Email from "./email-view";
import EmailTemplateList from "./email-template-list";
import EmailTemplate from "./email-template-update";

export default function EmailIndex() {
    return (
        <div>
            <Route path="/admin/email/list" component={EmailList}/>
            <Route path="/admin/email/:id/view" component={Email}/>
            <Route path="/admin/email/template/list" component={EmailTemplateList}/>
            <Route path="/admin/email/template/:id/update" component={EmailTemplate}/>
            <Route path="/admin/email/template/create" component={EmailTemplate}/>
        </div>
    );
}