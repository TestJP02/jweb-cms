package app.jweb.page.web.service;

import app.jweb.page.web.PostInfo;
import app.jweb.post.api.post.PostStatus;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class PostInfoImpl implements PostInfo, PostInfo.Builder {
    private String id;
    private String userId;
    private String categoryId;
    private String path;
    private String templatePath;
    private Integer version;
    private String title;
    private String description;
    private List<String> tags = ImmutableList.of();
    private List<String> keywords = ImmutableList.of();
    private String imageURL;
    private String content;
    private List<String> imageURLs = ImmutableList.of();
    private Map<String, String> fields = ImmutableMap.of();
    private PostStatus status;
    private OffsetDateTime createdTime;
    private String createdBy;
    private OffsetDateTime updatedTime;
    private String updatedBy;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String userId() {
        return userId;
    }

    @Override
    public String categoryId() {
        return categoryId;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public String templatePath() {
        return templatePath;
    }

    @Override
    public Integer version() {
        return version;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public List<String> tags() {
        return tags;
    }

    @Override
    public List<String> keywords() {
        return keywords;
    }

    @Override
    public String imageURL() {
        return imageURL;
    }

    @Override
    public List<String> imageURLs() {
        return imageURLs;
    }

    @Override
    public Map<String, String> fields() {
        return fields;
    }

    @Override
    public PostStatus status() {
        return status;
    }

    @Override
    public OffsetDateTime createdTime() {
        return createdTime;
    }

    @Override
    public OffsetDateTime updatedTime() {
        return updatedTime;
    }

    @Override
    public String createdBy() {
        return createdBy;
    }

    @Override
    public String updatedBy() {
        return updatedBy;
    }

    @Override
    public PostInfo.Builder setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public PostInfo build() {
        return this;
    }

    @Override
    public PostInfo.Builder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public PostInfo.Builder setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    @Override
    public PostInfo.Builder setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public PostInfo.Builder setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    @Override
    public PostInfo.Builder setVersion(Integer version) {
        this.version = version;
        return this;
    }

    @Override
    public PostInfo.Builder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public PostInfo.Builder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public Builder setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public PostInfo.Builder setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public PostInfo.Builder setKeywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    @Override
    public PostInfo.Builder setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    @Override
    public PostInfo.Builder setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
        return this;
    }

    @Override
    public PostInfo.Builder setFields(Map<String, String> fields) {
        this.fields = fields;
        return this;
    }

    @Override
    public PostInfo.Builder setStatus(PostStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public PostInfo.Builder setCreatedTime(OffsetDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    @Override
    public PostInfo.Builder setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @Override
    public PostInfo.Builder setUpdatedTime(OffsetDateTime updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    @Override
    public PostInfo.Builder setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }
}
