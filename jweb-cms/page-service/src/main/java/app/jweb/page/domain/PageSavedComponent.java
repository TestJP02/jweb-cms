package app.jweb.page.domain;


import app.jweb.page.api.component.SavedComponentStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "page_component")
public class PageSavedComponent {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "component_name")
    public String componentName;

    @Column(name = "display_name")
    public String displayName;

    @Column(name = "attributes", columnDefinition = "text")
    public String attributes;

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public SavedComponentStatus status;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;

    @Column(name = "updated_time")
    public OffsetDateTime updatedTime;

    @Column(name = "updated_by", length = 64)
    public String updatedBy;
}
