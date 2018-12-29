package app.jweb.page.web.service;

import app.jweb.post.api.keyword.KeywordResponse;
import com.google.common.base.Strings;

import java.util.Map;

/**
 * @author chi
 */
public class InnerLinkBuilder {
    private final Map<String, KeywordResponse> keywords;
    private final Integer baseStep;

    public InnerLinkBuilder(Map<String, KeywordResponse> keywords) {
        this.keywords = keywords;
        Integer step = 0;
        for (String keyword : keywords.keySet()) {
            if (!Strings.isNullOrEmpty(keyword) && keyword.length() > step) {
                step = keyword.length();
            }
        }
        this.baseStep = step;
    }

    /*public static void main(String[] args) {
        List<String> l = Lists.newArrayList("12", "456", "9");
        int base = 0;
        for (String s : l) {
            if (!Strings.isNullOrEmpty(s) && s.length() > base) {
                base = s.length();
            }
        }
        String a = "123456789";
        System.out.println("origin:");
        System.out.println(a);
        int length = a.length();
        int step = length < base ? length : base;
        int start = length - step;
        int end = length;
        StringBuilder result = new StringBuilder();
        int count = 0;
        while (start >= 0) {
            String stepString = a.substring(start, end);
            System.out.println("step:" + count + ",start:" + start + ",end:" + end);
            System.out.println("stepString:" + stepString);
            int delta = 1;
            for (int index = 0; index < stepString.length(); index++) {
                String check = stepString.substring(index);
                if (l.contains(check)) {
                    //todo transfer link
                    String link = "(" + check + ")";
                    delta = check.length();
                    result.insert(0, link);
                    break;
                }
                if (index == stepString.length() - 1) {
                    result.insert(0, check);
                }
            }
            end = end - delta;
            if (end == 0) {
                break;
            }
            start = start - delta > 0 ? start - delta : 0;
            count++;
        }
        System.out.println("result:");
        System.out.println(result.toString());
    }*/

    public String build(String content) {
        int length = content.length();
        int step = length < baseStep ? length : baseStep;
        int end = length;
        int start = length - step;
        StringBuilder result = new StringBuilder();
        while (start >= 0) {
            String stepString = content.substring(start, end);
            int delta = 1;
            for (int index = 0; index < stepString.length(); index++) {
                String check = stepString.substring(index);
                if (keywords.containsKey(check)) {
                    String link = transferLink(check);
                    delta = check.length();
                    result.insert(0, link);
                    break;
                }
                if (index == stepString.length() - 1) {
                    result.insert(0, check);
                }
            }
            end = end - delta;
            if (end == 0) {
                break;
            }
            start = start - delta > 0 ? start - delta : 0;
        }
        return result.toString();
    }

    private String transferLink(String keyword) {
        KeywordResponse keywordResponse = keywords.get(keyword);
        return String.format("<a href=\"%s\">%s</a>", keywordResponse.path, keywordResponse.value);
    }
}
