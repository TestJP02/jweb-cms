package io.sited.file.admin;

import com.google.common.collect.Lists;
import io.sited.admin.AbstractAdminModule;
import io.sited.admin.ConsoleBundle;
import io.sited.admin.ConsoleMenu;
import io.sited.file.admin.web.FileAdminController;
import io.sited.file.admin.web.ajax.DirectoryAdminAJAXController;
import io.sited.file.admin.web.ajax.FileAdminAJAXController;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 * @author chi
 */
public class FileAdminModule extends AbstractAdminModule {
    @Override
    protected void configure() {
        admin().controller(FileAdminAJAXController.class);
        admin().controller(DirectoryAdminAJAXController.class);
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
