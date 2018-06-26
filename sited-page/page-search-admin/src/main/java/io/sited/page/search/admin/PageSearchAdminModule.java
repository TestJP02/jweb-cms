package io.sited.page.search.admin;

import io.sited.admin.AbstractAdminModule;
import io.sited.admin.ConsoleMenu;
import io.sited.page.search.admin.web.PageSearchAdminController;

/**
 * @author chi
 */
public class PageSearchAdminModule extends AbstractAdminModule {
    @Override
    protected void configure() {
        admin().controller(PageSearchAdminController.class);

        ConsoleMenu.ConsoleMenuItem item = new ConsoleMenu.ConsoleMenuItem();
        item.displayName = "Index";
        item.messageKey = "search.index";
        item.path = "/admin/page/search/index";
        item.bundleName = "pageSearchBundle";

        admin().bundle("pageBundle").addMenuItems(item)
            .addMessages("conf/messages/page-search-admin")
            .addScriptFiles("admin/static/search/page/pageSearch.min.js");
    }
}
