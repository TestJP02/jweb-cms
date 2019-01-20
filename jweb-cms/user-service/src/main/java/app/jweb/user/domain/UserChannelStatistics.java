package app.jweb.user.domain;

import javax.persistence.Entity;

/**
 * @author miller
 */
@Entity
public class UserChannelStatistics {
    public String channel;
    public Long total;

    public UserChannelStatistics(String channel, Long total) {
        this.channel = channel;
        this.total = total;
    }
}
