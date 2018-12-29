package app.jweb.file.admin;

import app.jweb.file.admin.web.FileAdminController;
import app.jweb.file.admin.web.api.DirectoryAdminWebController;
import app.jweb.file.admin.web.api.FileAdminWebController;
import com.google.common.collect.Lists;
import app.jweb.admin.AbstractAdminModule;
import app.jweb.admin.ConsoleBundle;
import app.jweb.admin.ConsoleMenu;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 * @author chi
 */
public class FileAdminModule extends AbstractAdminModule {
    @Override
    protected void configure() {
        admin().controller(FileAdminWebController.class);
        admin().controller(DirectoryAdminWebController.class);
        admin().controller(FileAdminController.class);

        app().register(MultiPartFeature.class);
        admin().install(jsModule());
    }

    private ConsoleBundle jsModule() {
        ConsoleMenu menu = new ConsoleMenu();
        menu.displayName = "file.menu";
        menu.path = "/admin/file/";

        ConsoleMenu.ConsoleMenuItem fileItem = new ConsoleMenu.ConsoleMenuItem();
        fileItem.displayName = "file.fileList";
        fileItem.path = "/admin/file/list";
        fileItem.rolesAllowed = Lists.newArrayList("file.LIST");

        ConsoleMenu.ConsoleMenuItem directoryItem = new ConsoleMenu.ConsoleMenuItem();
        directoryItem.displayName = "file.directoryList";
        directoryItem.path = "/admin/file/directory/list";
        directoryItem.rolesAllowed = Lists.newArrayList("file.LIST");
        menu.children = Lists.newArrayList(fileItem, directoryItem);

        ConsoleBundle consoleModule = new ConsoleBundle();
        consoleModule.name = "fileBundle";
        consoleModule.path = "/admin/file/";
        consoleModule.scriptFile = "admin/static/file/file.min.js";
        consoleModule.messages = Lists.newArrayList("conf/messages/file-admin");
        consoleModule.menu = menu;
        return consoleModule;
    }
}
