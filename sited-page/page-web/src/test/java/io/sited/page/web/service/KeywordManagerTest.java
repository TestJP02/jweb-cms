package io.sited.page.web.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.sited.page.api.PageKeywordWebService;
import io.sited.page.api.keyword.KeywordResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


/**
 * @author chi
 */
public class KeywordManagerTest {
    private KeywordManager keyManager;

    @BeforeEach
    public void setup() {
        PageKeywordWebService pageKeywordWebService = Mockito.mock(PageKeywordWebService.class);
        when(pageKeywordWebService.find()).thenReturn(keywords());
        keyManager = new KeywordManager().setPageKeywordWebService(pageKeywordWebService);
    }

    @Test
    public void createInnerLink() {
        CreateInnerLinksResult result = keyManager.createInnerLinks("1 2 34");
        assertEquals("<a href=\"/1\">1</a> 2 <a href=\"/34\">34</a>", result.result);
        assertTrue(result.inserted);
    }

    private List<KeywordResponse> keywords() {
        List<KeywordResponse> keywords = Lists.newArrayList();
        KeywordResponse keyword = new KeywordResponse();
        keyword.path = "/1";
        keyword.value = "1";
        keywords.add(keyword);

        keyword = new KeywordResponse();
        keyword.path = "/3";
        keyword.value = "3";
        keywords.add(keyword);

        keyword = new KeywordResponse();
        keyword.path = "/34";
        keyword.value = "34";
        keywords.add(keyword);

        return keywords;
    }

    @Test
    public void testFresh() {
        KeywordManager.FreshKeywordTrie freshKeywordTrie = new KeywordManager.FreshKeywordTrie();
        freshKeywordTrie.toInserts = Sets.newHashSet(keywordResponse("/2", "2"));
        freshKeywordTrie.toUpdates = Sets.newHashSet(keywordResponse("/1update", "1"));
        freshKeywordTrie.toDeletes = Sets.newHashSet(keywordResponse("/3", "3"));
        keyManager.refresh(freshKeywordTrie);
        CreateInnerLinksResult result = keyManager.createInnerLinks("1 2 45");
        assertEquals("<a href=\"/1update\">1</a> <a href=\"/2\">2</a> 45", result.result);
        assertTrue(result.inserted);
    }

    private KeywordResponse keywordResponse(String path, String value) {
        KeywordResponse keywordResponse = new KeywordResponse();
        keywordResponse.path = path;
        keywordResponse.value = value;
        return keywordResponse;
    }
}