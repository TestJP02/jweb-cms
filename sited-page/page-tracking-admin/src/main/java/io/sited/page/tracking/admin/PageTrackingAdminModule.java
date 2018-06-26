package io.sited.page.tracking.admin;

import io.sited.admin.AbstractAdminModule;
import io.sited.page.tracking.admin.web.ajax.PageTrackingAJAXController;

/**
 * @author chi
 */
public class PageTrackingAdminModule extends AbstractAdminModule {
    @Override
    protected void configure() {
        admin().controller(PageTrackingAJAXController.class);

        admin().bundle("dashboardBundle")
            .addMessages("conf/messages/page-tracking")
            .addScriptFiles("admin/static/page-tracking/pageTracking.min.js");
    }
}
