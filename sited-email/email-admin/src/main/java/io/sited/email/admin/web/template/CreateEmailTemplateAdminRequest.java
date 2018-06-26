package io.sited.email.admin.web.template;


import javax.validation.constraints.NotNull;

/**
 * @author chi
 */
public class CreateEmailTemplateAdminRequest {
    @NotNull
    public String name;
    @NotNull
    public String subject;
    @NotNull
    public String content;
    @NotNull
    public String language;
}
