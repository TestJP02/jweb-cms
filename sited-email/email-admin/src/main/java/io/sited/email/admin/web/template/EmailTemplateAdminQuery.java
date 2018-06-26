package io.sited.email.admin.web.template;

import io.sited.email.api.template.EmailTemplateStatus;

/**
 * @author chi
 */
public class EmailTemplateAdminQuery {
    public String query;
    public EmailTemplateStatus status;
    public String sortingField = "updatedTime";
    public Boolean desc = true;
    public Integer page = 1;
    public Integer limit = 10;
}
