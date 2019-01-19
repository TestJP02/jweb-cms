package app.jweb.user.admin.web.ajax.user;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author miller
 */
public class UserStatisticsAJAXResponse {
    @XmlElement(name = "total")
    public Long total;
    @XmlElement(name = "channels")
    public List<Channel> channels;

    public static class Channel {
        @XmlElement(name = "channel")
        public String channel;
        @XmlElement(name = "total")
        public Long total;
    }
}
