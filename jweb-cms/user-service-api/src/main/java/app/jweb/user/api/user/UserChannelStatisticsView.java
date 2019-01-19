package app.jweb.user.api.user;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author miller
 */
public class UserChannelStatisticsView {
    @XmlElement(name = "channel")
    public String channel;
    @XmlElement(name = "total")
    public Long total;
}
