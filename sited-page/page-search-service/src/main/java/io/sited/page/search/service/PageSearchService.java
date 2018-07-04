package io.sited.page.search.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.sited.App;
import io.sited.ApplicationException;
import io.sited.page.api.PageContentWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.content.PageContentResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageStatus;
import io.sited.page.search.api.page.SearchPageRequest;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageSearchService {
    public static final String HIGHLIGHT_PRE_TAG = "<b><font color='red'>";
    public static final String HIGHLIGHT_POST_TAG = "</font></b>";
    private static final Integer BATCH_SIZE = 1000;
    private final Logger logger = LoggerFactory.getLogger(PageSearchService.class);
    private final Analyzer analyzer = new SmartChineseAnalyzer(true);
    @Inject
    PageWebService pageWebService;
    @Inject
    PageContentWebService pageContentWebService;
    @Inject
    App app;
    private Directory directory;
    private IndexWriter indexWriter;
    private volatile IndexSearcher indexSearcher;

    public void start() {
        directory = createDirectory(app.dir().resolve("index/page"));
        indexWriter = createIndexWriter(directory);
    }

    private IndexSearcher indexSearcher() {
        try {
            if (indexSearcher != null) {
                return indexSearcher;
            }
            DirectoryReader reader = DirectoryReader.open(directory);
            indexSearcher = new IndexSearcher(reader);
            return indexSearcher;
        } catch (IOException e) {
            throw new ApplicationException("failed to create index searcher", e);
        }
    }

    private Directory createDirectory(Path dir) {
        try {
            Files.createDirectories(dir);
            return FSDirectory.open(dir);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private IndexWriter createIndexWriter(Directory directory) {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try {
            return new IndexWriter(directory, indexWriterConfig);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    public QueryResponse<PageResponse> search(SearchPageRequest request) {
        int page = request.page == null ? 1 : request.page;
        int limit = request.limit == null ? 20 : request.limit;

        try {
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            if (!Strings.isNullOrEmpty(request.query)) {
                BooleanQuery.Builder query = new BooleanQuery.Builder();
                query.add(parse("title", request.query), BooleanClause.Occur.SHOULD);
                query.add(parse("description", request.query), BooleanClause.Occur.SHOULD);
                query.add(new QueryParser("content", analyzer).parse(request.query), BooleanClause.Occur.SHOULD);
                builder.add(query.build(), BooleanClause.Occur.MUST);
            } else {
                builder.add(new MatchAllDocsQuery(), BooleanClause.Occur.SHOULD);
            }

            if (!Strings.isNullOrEmpty(request.categoryId)) {
                builder.add(new TermQuery(new Term("categoryId", request.categoryId)), BooleanClause.Occur.MUST);
            }

            if (request.tags != null && !request.tags.isEmpty()) {
                builder.add(new QueryParser("tags", analyzer).parse(Joiner.on(' ').join(request.tags)), BooleanClause.Occur.MUST);
            }

            IndexSearcher indexSearcher = indexSearcher();

            Sort sort = new Sort();
            if (!Strings.isNullOrEmpty(request.sortingField)) {
                SortField sortField = new SortField(request.sortingField, SortField.Type.STRING, Boolean.TRUE.equals(request.desc));
                sort.setSort(sortField);
            }
            BooleanQuery searchQuery = builder.build();
            TopDocs results = indexSearcher.search(searchQuery, page * limit, sort);

            QueryResponse<PageResponse> queryResponse = new QueryResponse<>();
            queryResponse.page = page;
            queryResponse.limit = limit;
            queryResponse.total = results.totalHits;
            queryResponse.items = Lists.newArrayList();

            Highlighter highlighter = highlighter(searchQuery);
            int start = (page - 1) * limit;
            int end = (int) Math.min(page * limit, results.totalHits);
            for (int i = start; i < end; i++) {
                Document document = indexSearcher.doc(results.scoreDocs[i].doc);
                queryResponse.items.add(response(document, highlighter));
            }
            return queryResponse;
        } catch (IOException | ParseException e) {
            throw new ApplicationException("failed to search, query={}", JSON.toJSON(request), e);
        }
    }

    private Query parse(String field, String query) throws ParseException {
        QueryParser parser = new QueryParser(field, analyzer);
        parser.setDefaultOperator(QueryParser.Operator.AND);
        return parser.parse(query);
    }

    public boolean index(String pageId) {
        PageResponse page = pageWebService.get(pageId);
        PageContentResponse content = pageContentWebService.getByPageId(page.id);
        return index(page, content);
    }

    public boolean index(PageResponse page, PageContentResponse content) {
        try {
            if (indexSearcher != null) {
                Term id = new Term("id", page.id);
                TermQuery termQuery = new TermQuery(id);
                TopDocs search = indexSearcher.search(termQuery, 1);
                if (search.totalHits > 0) {
                    indexWriter.updateDocument(id, document(page, content));
                    return true;
                }
            }
            indexWriter.addDocument(document(page, content));
            indexWriter.commit();
            resetIndexSearcher();
            return true;
        } catch (IOException e) {
            throw new ApplicationException("failed to index page, id={}, title={}", page.id, page.title, e);
        }
    }


    public boolean removeIndex(String pageId) {
        try {
            indexWriter.deleteDocuments(new Term("id", pageId));
            indexWriter.commit();
            return true;
        } catch (IOException e) {
            throw new ApplicationException("failed to delete page, id={}", pageId, e);
        }
    }

    public void fullIndex() {
        try {
            indexWriter.deleteAll();
            indexWriter.commit();

            PageQuery pageQuery = new PageQuery();
            pageQuery.status = PageStatus.ACTIVE;
            pageQuery.page = 1;
            pageQuery.limit = BATCH_SIZE;
            List<PageResponse> list;
            do {
                QueryResponse<PageResponse> pages = pageWebService.find(pageQuery);
                list = pages.items;
                if (list.isEmpty()) {
                    break;
                }

                List<String> pageIds = pages.items.stream().map(page -> page.id).collect(Collectors.toList());
                Map<String, PageContentResponse> contents = pageContentWebService.batchGetByPageIds(pageIds).stream().collect(Collectors.toMap(content -> content.pageId, content -> content));
                batchIndex(pages.items, contents);
                pageQuery.page += 1;
            } while (list.size() == BATCH_SIZE);
            indexWriter.commit();
        } catch (IOException e) {
            throw new ApplicationException("failed to full index pages", e);
        }
    }

    private void batchIndex(List<PageResponse> pages, Map<String, PageContentResponse> contents) throws IOException {
        for (PageResponse page : pages) {
            PageContentResponse content = contents.get(page.id);
            indexWriter.addDocument(document(page, content));
        }
    }

    private void resetIndexSearcher() {
        indexSearcher = null;
    }

    private PageResponse response(Document document, Highlighter highlighter) {
        PageResponse response = new PageResponse();
        response.id = document.get("id");
        response.categoryId = document.get("categoryId");
        response.path = document.get("path");
        response.title = highlight("title", document.get("title"), highlighter);
        response.description = highlight("description", document.get("description"), highlighter);
        response.userId = document.get("userId");
        response.imageURL = document.get("imageURL");
        String tags = document.get("tags");
        if (tags != null) {
            response.tags = Splitter.on(" ").splitToList(tags);
        }
        try {
            Instant createdTime = Instant.ofEpochMilli(DateTools.stringToTime(document.get("createdTime")));
            response.createdTime = OffsetDateTime.ofInstant(createdTime, ZoneId.of("UTC"));

            Instant updatedTime = Instant.ofEpochMilli(DateTools.stringToTime(document.get("updatedTime")));
            response.updatedTime = OffsetDateTime.ofInstant(updatedTime, ZoneId.of("UTC"));
        } catch (java.text.ParseException e) {
            throw new ApplicationException(e);
        }
        return response;
    }

    private Document document(PageResponse page, PageContentResponse content) {
        Document document = new Document();
        document.add(new StringField("id", page.id, Field.Store.YES));
        document.add(new StringField("path", page.path, Field.Store.YES));
        if (page.categoryId != null) {
            document.add(new StringField("categoryId", page.categoryId, Field.Store.YES));
        }
        String createdTime = DateTools.dateToString(Date.from(page.createdTime.toInstant()), DateTools.Resolution.MILLISECOND);
        document.add(new StringField("createdTime", createdTime, Field.Store.YES));
        document.add(new SortedDocValuesField("createdTime", new BytesRef(createdTime)));
        document.add(new StringField("updatedTime", DateTools.dateToString(Date.from(page.updatedTime.toInstant()), DateTools.Resolution.MILLISECOND), Field.Store.YES));
        document.add(new TextField("userId", page.userId, Field.Store.YES));

        if (page.title != null) {
            document.add(new TextField("title", page.title, Field.Store.YES));
        }
        if (page.description != null) {
            document.add(new TextField("description", page.description, Field.Store.YES));
        }
        if (content != null) {
            document.add(new TextField("content", content(content), Field.Store.YES));
        }

        if (page.tags != null) {
            document.add(new TextField("tags", Joiner.on(" ").join(page.tags), Field.Store.YES));
        }

        if (page.keywords != null) {
            document.add(new TextField("keywords", Joiner.on(" ").join(page.keywords), Field.Store.YES));
        }
        return document;
    }

    private String content(PageContentResponse content) {
        if (content.content == null) {
            return "";
        }
        return content.content;
    }

    private Highlighter highlighter(Query searchQuery) {
        QueryScorer scorer = new QueryScorer(searchQuery);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(HIGHLIGHT_PRE_TAG, HIGHLIGHT_POST_TAG);
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        return highlighter;
    }

    private String highlight(String fieldName, String text, Highlighter highlighter) {
        if (text == null) {
            return "";
        }
        TokenStream tokenStream = analyzer.tokenStream(fieldName, new StringReader(text));
        try {
            String result = highlighter.getBestFragment(tokenStream, text);
            if (Strings.isNullOrEmpty(result)) {
                return text;
            }
            return result;
        } catch (Exception e) {
            logger.error("highlight error,field={},text={}", fieldName, text);
            logger.error(e.getMessage(), e);
            return text;
        }
    }
}
