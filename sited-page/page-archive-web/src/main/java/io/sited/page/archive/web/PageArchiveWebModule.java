package io.sited.page.archive.web;

import io.sited.page.archive.web.component.ArchivePageListComponent;
import io.sited.page.archive.web.component.ArchivesComponent;
import io.sited.page.archive.web.web.ArchiveController;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class PageArchiveWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().addComponent(requestInjection(new ArchivePageListComponent()));
        web().addComponent(requestInjection(new ArchivesComponent()));

        web().controller(ArchiveController.class);
    }
}
