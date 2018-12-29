package app.jweb.page.search.admin;

import app.jweb.admin.AbstractAdminModule;
import app.jweb.admin.ConsoleMenu;
import app.jweb.page.search.admin.web.PageSearchAdminController;

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

        admin().bundle("pageBundle").addMenuItems(item)
            .addRoute("/admin/page/search/index", "pageSearchBundle")
            .addMessages("conf/messages/page-search-admin")
            .addScriptFiles("admin/static/search/page/pageSearch.min.js");
    }
}
