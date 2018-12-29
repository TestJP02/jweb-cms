package app.jweb.pincode;


import app.jweb.pincode.api.PinCodeModule;
import app.jweb.pincode.api.PinCodeWebService;
import app.jweb.pincode.api.message.SendPinCodeMessage;
import app.jweb.pincode.domain.PinCodeTracking;
import app.jweb.pincode.service.PinCodeMessageHandler;
import app.jweb.pincode.service.PinCodeProvider;
import app.jweb.pincode.web.PinCodeWebServiceImpl;
import app.jweb.database.DatabaseConfig;
import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageConfig;
import app.jweb.message.MessageModule;
import app.jweb.message.TopicOptions;
import app.jweb.pincode.service.EmailSender;
import app.jweb.pincode.service.PinCodeService;

/**
 * @author chi
 */
public class PinCodeModuleImpl extends PinCodeModule {
    @Override
    protected void configure() {
        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig.entity(PinCodeTracking.class);

        PinCodeOptions options = options("pincode", PinCodeOptions.class);
        bind(PinCodeOptions.class).toInstance(options);
        bind(EmailSender.class).toInstance(new EmailSender(options.smtp));
        bind(PinCodeMessageHandler.class);
        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(SendPinCodeMessage.class, new TopicOptions());
        messageConfig.listen(SendPinCodeMessage.class, requestInjection(new PinCodeMessageHandler()));

        bind(PinCodeProvider.class);
        bind(PinCodeService.class);


        api().service(PinCodeWebService.class, PinCodeWebServiceImpl.class);
    }
}
