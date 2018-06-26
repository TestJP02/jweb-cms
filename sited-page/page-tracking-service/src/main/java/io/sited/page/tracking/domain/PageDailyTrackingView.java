package io.sited.page.tracking.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author chi
 */
@Entity
public class PageDailyTrackingView {
    @Column(name = "total")
    public Integer total;
    @Column(name = "timestamp")
    public String timestamp;

    public PageDailyTrackingView(Long total, String timestamp) {
        this.total = total.intValue();
        this.timestamp = timestamp;
    }
}
