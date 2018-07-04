package io.sited.page.web.service.message;

import com.google.common.collect.Sets;
import io.sited.message.MessageHandler;
import io.sited.page.api.keyword.KeywordChangedMessage;
import io.sited.page.api.keyword.KeywordResponse;
import io.sited.page.web.service.KeywordManager;

import javax.inject.Inject;

/**
 * @author chi
 */
public class KeywordChangedMessageHandler implements MessageHandler<KeywordChangedMessage> {
    @Inject
    KeywordManager keywordManager;

    @Override
    public void handle(KeywordChangedMessage keywordChangedMessage) throws Throwable {
        keywordManager.refresh(fresh(keywordChangedMessage));
    }

    private KeywordManager.FreshKeywordTrie fresh(KeywordChangedMessage keywordChangedMessage) {
        KeywordManager.FreshKeywordTrie freshKeywordTrie = new KeywordManager.FreshKeywordTrie();
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
