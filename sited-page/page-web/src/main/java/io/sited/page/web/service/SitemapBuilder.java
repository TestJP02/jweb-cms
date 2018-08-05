package io.sited.page.web.service;

import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.page.PageResponse;
import io.sited.resource.Resource;
import io.sited.resource.StringResource;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author chi
 */
public class SitemapBuilder {
    private final String baseURL;
    private final PageCacheRepository repository;
    private final int maxCount;
    private final StringBuilder b = new StringBuilder(128);
    private int index = 0;
    private int fileIndex = 1;

    public SitemapBuilder(String baseURL, PageCacheRepository repository, int maxCount) {
        this.baseURL = baseURL;
        this.repository = repository;
        this.maxCount = maxCount;
    }

    public SitemapBuilder appendCategories(List<CategoryResponse> categories) {
        for (CategoryResponse category : categories) {
            index++;
            if (index == maxCount) {
                flush();
            }
            appendURL(category.path, category.updatedTime, "daily", "0.8");
        }
        return this;
    }

    public SitemapBuilder appendPages(List<PageResponse> pages) {
        for (PageResponse page : pages) {
            index++;
            if (index == maxCount) {
                flush();
            }
            appendURL(page.path, page.updatedTime, "weekly", "0.5");
        }
        return this;
    }

    public Resource build() {
        String path = "sitemap/sitemap.xml";
        if (fileIndex == 1) {
            Resource resource = new StringResource(path, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">" + b.toString() + "</urlset>");
            repository.create(resource);
            return resource;
        } else {
            flush();
            b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
            for (int i = 0; i < fileIndex; i++) {
                b.append("<sitemap><loc>").append(baseURL).append("/sitemap/sitemap").append(i + 1).append(".xml</loc><lastmod>");
                b.append(OffsetDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)).append("</lastmod></sitemap>");
            }
            b.append("</sitemapindex>");
            Resource resource = new StringResource(path, b.toString());
            repository.create(resource);
            return resource;
        }
    }

    private void flush() {
        String path = "sitemap/sitemap" + fileIndex + ".xml";
        Resource resource = new StringResource(path, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">" + b.toString() + "</urlset>");
        repository.create(resource);
        b.delete(0, b.length());
        index = 0;
        fileIndex++;
    }

    private void appendURL(String path, OffsetDateTime updatedTime, String changeFrequency, String priority) {
        b.append("<url><loc>").append(baseURL).append(path).append("</loc><lastmod>").append(updatedTime.format(DateTimeFormatter.ISO_LOCAL_DATE))
            .append("</lastmod><changefreq>").append(changeFrequency).append("</changefreq><priority>").append(priority).append("</priority></url>");
    }
}
