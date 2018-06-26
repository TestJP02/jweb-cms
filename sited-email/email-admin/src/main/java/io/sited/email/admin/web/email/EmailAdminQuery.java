package io.sited.email.admin.web.email;

import io.sited.email.api.email.SendEmailStatus;

/**
 * @author chi
 */
public class EmailAdminQuery {
    public String query;
    public SendEmailStatus status;
    public String sortingField;
    public Boolean desc = true;
    public Integer page = 1;
    public Integer limit = 10;
}
