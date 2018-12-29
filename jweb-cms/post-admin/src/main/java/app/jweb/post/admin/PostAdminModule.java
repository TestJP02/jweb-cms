package app.jweb.post.admin;


import app.jweb.admin.AbstractAdminModule;
import app.jweb.admin.ConsoleBundle;
import app.jweb.admin.ConsoleMenu;
import app.jweb.post.admin.service.PostPathService;
import app.jweb.post.admin.web.api.PostAdminController;
import app.jweb.post.admin.web.api.PostCategoryAdminController;
import app.jweb.post.admin.web.api.PostPathAdminController;
import app.jweb.post.admin.web.api.PostStatisticsAdminController;
import app.jweb.post.admin.web.api.PostTagAdminController;
import com.google.common.collect.Lists;

/**
 * @author chi
 */
public class PostAdminModule extends AbstractAdminModule {
    @Override
    protected void configure() {
        PostAdminOptions postAdminOptions = options("post-admin", PostAdminOptions.class);


        bind(PostAdminOptions.class).toInstance(postAdminOptions);
        bind(PostPathService.class);

        admin().controller(PostAdminController.class);
        admin().controller(PostPathAdminController.class);
        admin().controller(PostCategoryAdminController.class);
        admin().controller(PostTagAdminController.class);
        admin().controller(PostStatisticsAdminController.class);

        admin().install(scriptModule());

        admin().bundle("dashboardBundle")
            .addMessages("conf/messages/post-dashboard")
            .addScriptFiles("admin/static/post/postDashboard.min.js");
    }

    private ConsoleBundle scriptModule() {
        ConsoleMenu menu = new ConsoleMenu();
        menu.displayName = "post.menu";
        menu.path = "/admin/post/";

        ConsoleMenu.ConsoleMenuItem postItem = new ConsoleMenu.ConsoleMenuItem();
        postItem.displayName = "post.post";
        postItem.path = "/admin/post/list";
        postItem.rolesAllowed = Lists.newArrayList("post.LIST");

        ConsoleMenu.ConsoleMenuItem categoryItem = new ConsoleMenu.ConsoleMenuItem();
        categoryItem.displayName = "post.category";
        categoryItem.path = "/admin/post/category/list";
        categoryItem.rolesAllowed = Lists.newArrayList("post.LIST");

        ConsoleMenu.ConsoleMenuItem tagItem = new ConsoleMenu.ConsoleMenuItem();
        tagItem.displayName = "post.tags";
        tagItem.path = "/admin/post/tag/list";
        tagItem.rolesAllowed = Lists.newArrayList("post.LIST");

        menu.children = Lists.newArrayList(postItem, categoryItem, tagItem);

        ConsoleBundle consoleModule = new ConsoleBundle();
        consoleModule.name = "postBundle";
        consoleModule.path = "/admin/post/";
        consoleModule.scriptFile = "admin/static/post/post.min.js";
        consoleModule.messages = Lists.newArrayList("conf/messages/post-admin");
        consoleModule.menu = menu;
        return consoleModule;
    }
}
