package io.sited.email.service;

import com.google.common.collect.ImmutableList;
import io.sited.email.domain.EmailTemplate;
import io.sited.ApplicationException;
import io.sited.resource.Resource;
import io.sited.resource.ResourceRepository;
import io.sited.resource.StringResource;

import javax.inject.Provider;
import javax.ws.rs.NotFoundException;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author chi
 */
public class EmailTemplateRepository implements ResourceRepository {
    private final Provider<EmailTemplateService> emailTemplateService;

    public EmailTemplateRepository(Provider<EmailTemplateService> emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    public Optional<Resource> get(String path) {
        int index = path.lastIndexOf('-');
        if (index < 0) throw new ApplicationException("invalid path, path={}", path);
        String type = path.substring(index + 1, path.length());
        EmailTemplate emailTemplate = emailTemplateService.get().findByName(path.substring(1, index)).orElseThrow(() -> new NotFoundException("missing template,name=" + path));
        if ("subject".equals(type)) {
            return Optional.of(new StringResource(path, emailTemplate.subject));
        } else if ("content".equals(type)) {
            return Optional.of(new StringResource(path, emailTemplate.content));
        }
        throw new ApplicationException("invalid path type, type={}", type);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void create(Resource resource) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public Iterator<Resource> iterator() {
        return ImmutableList.<Resource>of().iterator();
    }
}
