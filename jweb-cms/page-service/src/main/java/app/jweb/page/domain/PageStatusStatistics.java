package app.jweb.page.domain;

import app.jweb.page.api.page.PageStatus;

import javax.persistence.Entity;

/**
 * @author miller
 */
@Entity
public class PageStatusStatistics {
    public PageStatus status;
    public Long total;

    public PageStatusStatistics(PageStatus status, Long total) {
        this.status = status;
        this.total = total;
    }
}
