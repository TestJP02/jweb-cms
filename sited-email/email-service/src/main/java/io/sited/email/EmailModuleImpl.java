package io.sited.email;

import io.sited.ApplicationException;
import io.sited.database.DatabaseConfig;
import io.sited.database.DatabaseModule;
import io.sited.email.api.EmailModule;
import io.sited.email.api.EmailTemplateWebService;
import io.sited.email.api.EmailWebService;
import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.domain.EmailTemplate;
import io.sited.email.domain.EmailTracking;
import io.sited.email.service.EmailMessageHandler;
import io.sited.email.service.EmailTemplateEngineManager;
import io.sited.email.service.EmailTemplateRepository;
import io.sited.email.service.EmailTemplateService;
import io.sited.email.service.EmailTrackingService;
import io.sited.email.web.EmailTemplateWebServiceImpl;
import io.sited.email.web.EmailWebServiceImpl;
import io.sited.message.MessageConfig;
import io.sited.message.MessageModule;
import io.sited.message.TopicOptions;
import io.sited.template.TemplateEngine;

/**
 * @author chi
 */
public abstract class EmailModuleImpl extends EmailModule {
    @Override
    protected void configure() {
        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig
            .entity(EmailTracking.class)
            .entity(EmailTemplate.class);

        bind(EmailVendor.class).toInstance(vendor());
        bind(EmailTrackingService.class);
        bind(EmailTemplateService.class);

        TemplateEngine emailTemplateEngine = new TemplateEngine().addRepository(new EmailTemplateRepository(() -> require(EmailTemplateService.class)));
        bind(EmailTemplateEngineManager.class).toInstance(new EmailTemplateEngineManager(emailTemplateEngine));

        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(SendEmailRequest.class, new TopicOptions());
        messageConfig.listen(SendEmailRequest.class, requestInjection(new EmailMessageHandler()));

        api().service(EmailWebService.class, EmailWebServiceImpl.class);
        api().service(EmailTemplateWebService.class, EmailTemplateWebServiceImpl.class);
    }

    protected EmailVendor vendor() {
        throw new ApplicationException("missing email vendor");
    }
}
