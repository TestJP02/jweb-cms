package io.sited.page.tracking.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author chi
 */
@Entity
public class PageTrackingView {
    @Column(name = "total")
    public Integer total;

    public PageTrackingView(Long total) {
        this.total = total.intValue();
    }
}
