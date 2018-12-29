package app.jweb;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Locale;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AppOptions {
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "baseURL")
    public String baseURL = "http://localhost:8080/";
    @XmlElement(name = "env")
    public Environment env = Environment.PROD;
    @XmlElement(name = "language")
    public String language = Locale.getDefault().getLanguage();
    @XmlElement(name = "supportLanguages")
    public List<String> supportLanguages = Lists.newArrayList();
    @XmlElement(name = "webEnabled")
    public Boolean webEnabled = true;
    @XmlElement(name = "apiEnabled")
    public Boolean apiEnabled = true;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "host")
    public String host;
    @XmlElement(name = "port")
    public String port;
}
