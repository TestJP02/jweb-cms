package app.jweb.page;

import app.jweb.database.DatabaseConfig;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageConfig;
import app.jweb.message.MessageModule;
import app.jweb.message.TopicOptions;
import app.jweb.page.api.PageComponentWebService;
import app.jweb.page.api.PageModule;
import app.jweb.page.api.PageSavedComponentWebService;
import app.jweb.page.api.PageTemplateWebService;
import app.jweb.page.api.PageVariableWebService;
import app.jweb.page.api.component.SavedComponentChangedMessage;
import app.jweb.page.api.template.TemplateChangedMessage;
import app.jweb.page.api.variable.VariableChangedMessage;
import app.jweb.page.domain.PageSavedComponent;
import app.jweb.page.domain.PageTemplate;
import app.jweb.page.domain.PageVariable;
import app.jweb.page.service.PageComponentService;
import app.jweb.page.service.PageSavedComponentService;
import app.jweb.page.service.PageTemplateService;
import app.jweb.page.service.PageVariableService;
import app.jweb.page.web.PageComponentWebServiceImpl;
import app.jweb.page.web.PageSavedComponentWebServiceImpl;
import app.jweb.page.web.PageTemplateWebServiceImpl;
import app.jweb.page.web.PageVariableWebServiceImpl;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author chi
 */
public class PageModuleImpl extends PageModule {
    @Override
    protected void configure() {
        bind(PageOptions.class).toInstance(options("page", PageOptions.class));

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(TemplateChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(VariableChangedMessage.class, new TopicOptions());
        messageConfig.createTopic(SavedComponentChangedMessage.class, new TopicOptions());

        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(PageVariable.class)
            .entity(PageTemplate.class)
            .entity(PageSavedComponent.class);

        bind(PageVariableService.class);
        bind(PageSavedComponentService.class);
        bind(PageComponentService.class);
        bind(PageTemplateService.class);

        api().service(PageVariableWebService.class, PageVariableWebServiceImpl.class);
        api().service(PageTemplateWebService.class, PageTemplateWebServiceImpl.class);
        api().service(PageComponentWebService.class, PageComponentWebServiceImpl.class);
        api().service(PageSavedComponentWebService.class, PageSavedComponentWebServiceImpl.class);
    }

    @Override
    public List<String> declareRoles() {
        List<String> declareRoles = Lists.newArrayList(super.declareRoles());
        declareRoles.add("PUBLISH");
        return declareRoles;
    }
}
