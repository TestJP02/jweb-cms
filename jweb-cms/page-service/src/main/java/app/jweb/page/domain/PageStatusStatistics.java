package app.jweb.page.domain;

import app.jweb.page.api.page.PageStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author miller
 */
@Entity
@Table(name = "page_page")
public class PageStatusStatistics {
    @Id
    @Column(name = "status")
    public PageStatus status;
    @Column(name = "total")
    public Long total;
}
