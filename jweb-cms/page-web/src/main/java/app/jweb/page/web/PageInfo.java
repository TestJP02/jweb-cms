package app.jweb.page.web;

import app.jweb.page.api.page.PageStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class PageInfo {
    public String id;

    public String userId;

    public String categoryId;

    public String path;

    public String title;

    public String description;

    public List<String> tags;

    public List<String> keywords;

    public String imageURL;

    public List<String> imageURLs;

    public Map<String, String> fields;

    public PageStatus status;

    public OffsetDateTime createdTime;

    public OffsetDateTime updatedTime;

    public String createdBy;

    public String updatedBy;
}
