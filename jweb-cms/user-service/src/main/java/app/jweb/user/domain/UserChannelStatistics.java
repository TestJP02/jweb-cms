package app.jweb.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author miller
 */
@Entity
@Table(name = "user_user")
public class UserChannelStatistics {
    @Id
    @Column(name = "channel")
    public String channel;
    @Column(name = "total")
    public Long total;
}
