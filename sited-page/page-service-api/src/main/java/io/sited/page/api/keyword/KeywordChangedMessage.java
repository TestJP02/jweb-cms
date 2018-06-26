package io.sited.page.api.keyword;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;
import java.util.Set;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class KeywordChangedMessage {
    public String path;
    public Set<String> toInsertKeywords;
    public Set<String> toUpdateKeywords;
    public Set<String> toDeleteKeywords;
    public List<KeywordResponse> keywords;
}
