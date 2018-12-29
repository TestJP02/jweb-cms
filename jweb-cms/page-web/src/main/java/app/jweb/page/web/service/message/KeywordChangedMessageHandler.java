package app.jweb.page.web.service.message;

import app.jweb.message.MessageHandler;
import app.jweb.page.web.service.KeywordService;
import app.jweb.post.api.keyword.KeywordChangedMessage;
import app.jweb.post.api.keyword.KeywordResponse;
import com.google.common.collect.Sets;

import javax.inject.Inject;

/**
 * @author chi
 */
public class KeywordChangedMessageHandler implements MessageHandler<KeywordChangedMessage> {
    @Inject
    KeywordService keywordService;

    @Override
    public void handle(KeywordChangedMessage keywordChangedMessage) throws Throwable {
        keywordService.refresh(fresh(keywordChangedMessage));
    }

    private KeywordService.FreshKeywordTrie fresh(KeywordChangedMessage keywordChangedMessage) {
        KeywordService.FreshKeywordTrie freshKeywordTrie = new KeywordService.FreshKeywordTrie();
        freshKeywordTrie.toInserts = Sets.newHashSet();
        freshKeywordTrie.toUpdates = Sets.newHashSet();
        freshKeywordTrie.toDeletes = Sets.newHashSet();
        keywordChangedMessage.toInsertKeywords.forEach(keyword -> {
            freshKeywordTrie.toInserts.add(keywordResponse(keyword, keywordChangedMessage.path));
        });
        keywordChangedMessage.toUpdateKeywords.forEach(keyword -> {
            freshKeywordTrie.toUpdates.add(keywordResponse(keyword, keywordChangedMessage.path));
        });
        keywordChangedMessage.toDeleteKeywords.forEach(keyword -> {
            freshKeywordTrie.toDeletes.add(keywordResponse(keyword, keywordChangedMessage.path));
        });
        return freshKeywordTrie;
    }

    private KeywordResponse keywordResponse(String value, String path) {
        KeywordResponse keywordResponse = new KeywordResponse();
        keywordResponse.value = value;
        keywordResponse.path = path;
        return keywordResponse;
    }
}
