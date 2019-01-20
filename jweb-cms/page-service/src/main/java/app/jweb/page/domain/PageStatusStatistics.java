package app.jweb.page.domain;

import app.jweb.page.api.page.PageStatus;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author miller
 */
@Entity
public class PageStatusStatistics {
    @Column(name = "status")
    public PageStatus status;
    @Column(name = "total")
    public Long total;
}
