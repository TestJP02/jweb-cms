package io.sited.email.ses;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.google.common.base.Strings;
import io.sited.email.EmailModuleImpl;
import io.sited.email.EmailVendor;
import io.sited.email.ses.service.SESEmailVendorImpl;
import io.sited.ApplicationException;

/**
 * @author chi
 */
public class SESModule extends EmailModuleImpl {
    @Override
    protected EmailVendor vendor() {
        SESOptions options = options("ses", SESOptions.class);
        if (Strings.isNullOrEmpty(options.accessKey) || Strings.isNullOrEmpty(options.secretKey)) {
            throw new ApplicationException("missing ses option");
        }
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(options.accessKey, options.secretKey);
        AmazonSimpleEmailService amazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder.standard()
            .withRegion(Regions.valueOf(options.region))
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
        return new SESEmailVendorImpl(amazonSimpleEmailService);
    }
}
