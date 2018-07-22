package io.sited.page.web.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.PageKeywordWebService;
import io.sited.page.api.keyword.KeywordResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chi
 */
public class KeywordManager {
    PageKeywordWebService pageKeywordWebService;
    volatile KeywordTrie trie;

    public KeywordManager setPageKeywordWebService(PageKeywordWebService pageKeywordWebService) {
        this.pageKeywordWebService = pageKeywordWebService;
        return this;
    }

    public void start() {
        reloadAll();
    }

    public void reloadAll() {
        trie = new KeywordTrie(pageKeywordWebService.find());
    }

    public void refresh(FreshKeywordTrie freshKeywordTrie) {
        freshKeywordTrie.toInserts.forEach(trie::add);
        freshKeywordTrie.toUpdates.forEach(trie::update);
        freshKeywordTrie.toDeletes.forEach(trie::remove);
    }

    public CreateInnerLinksResult createInnerLinks(String text) {
        CreateInnerLinksResult result = new CreateInnerLinksResult();
        StringBuilder b = new StringBuilder();

        char[] chars = text.toCharArray();
        String[] words = trie.words(chars);
        for (int i = words.length - 1; i >= 0; i--) {
            String word = words[i];
            int length = trie.maxLength();
            if (i < length) {
                length = i + 1;
            }
            while (length > 0) {
                Optional<KeywordNode> keywordNode = trie.find(words, i - length + 1, length);
                if (keywordNode.isPresent()) {
                    StringBuilder innerText = new StringBuilder();
                    for (int j = i - length + 1; j <= i; j++) {
                        innerText.append(words[j]);
                    }
                    b.insert(0, "<a href=\"" + keywordNode.get().path + "\">" + innerText + "</a>");
                    result.inserted = true;
                    break;
                }
                length--;
            }
            if (length == 0) {
                b.insert(0, word);
            } else {
                i -= length - 1;
            }
        }
        result.result = b.toString();
        return result;
    }


    public static class KeywordTrie {
        private final Logger logger = LoggerFactory.getLogger(KeywordManager.class);
        private final Map<String, KeywordNode> root = Maps.newConcurrentMap();
        private final Map<Integer, AtomicInteger> lengthCountMap = Maps.newConcurrentMap();
        private volatile int maxLength = 0;

        public KeywordTrie(List<KeywordResponse> keywords) {
            keywords.forEach(this::add);
        }

        public Optional<KeywordNode> find(String[] word, int offset, int length) {
            Map<String, KeywordNode> nodes = root;
            for (int i = 0; i < length; i++) {
                String key = word[offset + i];
                KeywordNode keywordNode = nodes.get(key);
                if (keywordNode == null) {
                    return Optional.empty();
                } else if (i == length - 1 && keywordNode.path != null) {
                    return Optional.of(keywordNode);
                } else {
                    nodes = keywordNode.children;
                }
            }
            return Optional.empty();
        }

        public int maxLength() {
            return maxLength;
        }

        private void add(KeywordResponse keyword) {
            Map<String, KeywordNode> nodes = root;
            int keyWordLength = 0;
            char[] chars = keyword.value.toCharArray();
            String[] words = words(chars);
            for (int i = 0,
                 length = words.length; i < length; i++) {
                String key = words[i];
                KeywordNode keywordNode = nodes.get(key);
                if (keywordNode == null) {
                    keywordNode = new KeywordNode();
                    if (i == words.length - 1) {
                        keywordNode.path = keyword.path;
                    }
                    keywordNode.children = Maps.newConcurrentMap();
                    nodes.put(key, keywordNode);
                }
                nodes = keywordNode.children;
                keyWordLength++;
            }

            if (keyWordLength > maxLength) {
                maxLength = keyWordLength;
            }
            AtomicInteger count = lengthCountMap.get(keyWordLength);
            if (count == null) {
                lengthCountMap.put(keyWordLength, new AtomicInteger(1));
            } else {
                count.addAndGet(1);
                lengthCountMap.put(keyWordLength, count);
            }
        }

        private void update(KeywordResponse keyword) {
            Map<String, KeywordNode> nodes = root;
            char[] chars = keyword.value.toCharArray();
            String[] words = words(chars);
            for (int i = 0,
                 length = words.length; i < length; i++) {
                String key = words[i];

                KeywordNode keywordNode = nodes.get(key);
                if (keywordNode == null) {
                    logger.warn("missing keyword ,value={},index={}", keyword.value, i);
                    keywordNode = new KeywordNode();
                    keywordNode.children = Maps.newConcurrentMap();
                    nodes.put(key, keywordNode);
                }
                if (i == words.length - 1) {
                    keywordNode.path = keyword.path;
                }
                nodes = keywordNode.children;
            }
        }

        @SuppressWarnings("checkstyle:NestedIfDepth")
        private void remove(KeywordResponse keyword) {
            Map<String, KeywordNode> nodes = root;
            char[] chars = keyword.value.toCharArray();
            String[] words = words(chars);
            int keyWordLength = 0;
            for (int i = 0; i < words.length; i += 1) {
                String key = words[i];
                keyWordLength++;

                KeywordNode keywordNode = nodes.get(key);
                if (keywordNode == null) {
                    logger.warn("missing keyword,value={},index={}", keyword, i);
                    break;
                }
                if (i == words.length - 1) {
                    if (keywordNode.children.isEmpty()) {
                        nodes.remove(key);
                    } else {
                        keywordNode.path = null;
                    }
                }
            }
            AtomicInteger atomicInteger = lengthCountMap.get(keyWordLength);
            if (keyWordLength == maxLength) {
                int count = atomicInteger.decrementAndGet();
                if (count == 0) {
                    int length = keyWordLength - 1;
                    while (length > 0) {
                        AtomicInteger check = lengthCountMap.get(length);
                        if (check != null && check.get() != 0) {
                            maxLength = length;
                            break;
                        }
                        length = length - 1;
                    }
                    if (length == 0) {
                        maxLength = 0;
                    }
                }
            } else {
                atomicInteger.decrementAndGet();
            }
        }

        @SuppressWarnings("checkstyle:NestedIfDepth")
        String[] words(char... chars) {
            List<String> list = Lists.newArrayList();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                String word;
                if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                    StringBuilder wordBuilder = new StringBuilder();
                    wordBuilder.append(c);
                    for (int j = i + 1; j < chars.length; j++) {
                        char next = chars[j];
                        if (Character.isAlphabetic(next) || Character.isDigit(next)) {
                            wordBuilder.append(next);
                            if (j == chars.length - 1) {
                                i = j;
                            }
                        } else {
                            i = j - 1;
                            break;
                        }
                    }
                    word = wordBuilder.toString();
                } else {
                    word = String.valueOf(c);
                }
                list.add(word);
            }
            return list.toArray(new String[0]);
        }
    }

    public static class KeywordNode {
        public String path;
        public Map<String, KeywordNode> children;
    }

    public static class FreshKeywordTrie {
        public Set<KeywordResponse> toInserts;
        public Set<KeywordResponse> toUpdates;
        public Set<KeywordResponse> toDeletes;
    }
}
