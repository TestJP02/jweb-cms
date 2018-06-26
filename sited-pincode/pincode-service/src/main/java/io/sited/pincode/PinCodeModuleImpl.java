package io.sited.pincode;


import io.sited.database.DatabaseConfig;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageConfig;
import io.sited.message.MessageModule;
import io.sited.message.TopicOptions;
import io.sited.pincode.api.PinCodeModule;
import io.sited.pincode.api.PinCodeWebService;
import io.sited.pincode.api.message.SendPinCodeMessage;
import io.sited.pincode.domain.PinCodeTracking;
import io.sited.pincode.service.PinCodeMessageHandler;
import io.sited.pincode.service.PinCodeProvider;
import io.sited.pincode.service.PinCodeService;
import io.sited.pincode.web.PinCodeWebServiceImpl;

/**
 * @author chi
 */
public class PinCodeModuleImpl extends PinCodeModule {
    @Override
    protected void configure() {
        DatabaseConfig databaseConfig = module(DatabaseModule.class);
        databaseConfig.entity(PinCodeTracking.class);

        bind(PinCodeMessageHandler.class);
        MessageConfig messageConfig = module(MessageModule.class);
        messageConfig.createTopic(SendPinCodeMessage.class, new TopicOptions());
        messageConfig.listen(SendPinCodeMessage.class, PinCodeMessageHandler.class);

        bind(PinCodeOptions.class).toInstance(options("pincode", PinCodeOptions.class));
        bind(PinCodeProvider.class);
        bind(PinCodeService.class);


        api().service(PinCodeWebService.class, PinCodeWebServiceImpl.class);
    }
}
