package io.sited.page.web.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.sited.page.api.page.PageStatus;
import io.sited.page.web.PageInfo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class PageInfoImpl implements PageInfo, PageInfo.Builder {
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
    private PageStatus status;
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
    public PageStatus status() {
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
    public PageInfo.Builder setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public PageInfo build() {
        return this;
    }

    @Override
    public PageInfo.Builder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public PageInfo.Builder setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    @Override
    public PageInfo.Builder setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public PageInfo.Builder setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    @Override
    public PageInfo.Builder setVersion(Integer version) {
        this.version = version;
        return this;
    }

    @Override
    public PageInfo.Builder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public PageInfo.Builder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public Builder setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public PageInfo.Builder setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public PageInfo.Builder setKeywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    @Override
    public PageInfo.Builder setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    @Override
    public PageInfo.Builder setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
        return this;
    }

    @Override
    public PageInfo.Builder setFields(Map<String, String> fields) {
        this.fields = fields;
        return this;
    }

    @Override
    public PageInfo.Builder setStatus(PageStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public PageInfo.Builder setCreatedTime(OffsetDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    @Override
    public PageInfo.Builder setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @Override
    public PageInfo.Builder setUpdatedTime(OffsetDateTime updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    @Override
    public PageInfo.Builder setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }
}
