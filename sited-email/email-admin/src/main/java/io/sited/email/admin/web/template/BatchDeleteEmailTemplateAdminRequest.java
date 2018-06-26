package io.sited.email.admin.web.template;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chi
 */
public class BatchDeleteEmailTemplateAdminRequest {
    @NotNull
    public List<String> ids;
}
