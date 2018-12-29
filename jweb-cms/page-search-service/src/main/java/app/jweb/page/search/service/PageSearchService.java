package app.jweb.page.search.service;

import app.jweb.App;
import app.jweb.ApplicationException;
import app.jweb.page.search.api.page.SearchPageQuery;
import app.jweb.post.api.PostContentWebService;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.content.PostContentResponse;
import app.jweb.post.api.post.BatchGetPostRequest;
import app.jweb.post.api.post.PostCreatedMessage;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.api.post.PostStatus;
import app.jweb.post.api.post.PostUpdatedMessage;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageSearchService {
    private static final String HIGHLIGHT_PRE_TAG = "<span class='highlight'>";
    private static final String HIGHLIGHT_POST_TAG = "</span>";
    private static final Integer BATCH_SIZE = 1000;
    private final Logger logger = LoggerFactory.getLogger(PageSearchService.class);
    private final Analyzer analyzer = new SmartChineseAnalyzer(true);
    @Inject
    PostWebService postWebService;
    @Inject
    PostContentWebService postContentWebService;
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
            logger.warn("failed to create index searcher", e);
            return null;
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

    public QueryResponse<PostResponse> search(SearchPageQuery request) {
        IndexSearcher indexSearcher = indexSearcher();
        if (indexSearcher == null) {
            QueryResponse<PostResponse> queryResponse = new QueryResponse<>();
            queryResponse.items = ImmutableList.of();
            queryResponse.total = 0L;
            queryResponse.page = request.page;
            queryResponse.limit = request.limit;
            return queryResponse;
        }

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


            Sort sort = new Sort();
            if (!Strings.isNullOrEmpty(request.sortingField)) {
                SortField sortField = new SortField(request.sortingField, SortField.Type.STRING, Boolean.TRUE.equals(request.desc));
                sort.setSort(sortField);
            }
            BooleanQuery searchQuery = builder.build();
            TopDocs results = indexSearcher.search(searchQuery, page * limit, sort);

            QueryResponse<PostResponse> queryResponse = new QueryResponse<>();
            queryResponse.page = page;
            queryResponse.limit = limit;
            queryResponse.total = results.totalHits;

            int start = (page - 1) * limit;
            int end = (int) Math.min(page * limit, results.totalHits);

            List<String> postIds = Lists.newArrayList();
            for (int i = start; i < end; i++) {
                Document document = indexSearcher.doc(results.scoreDocs[i].doc);
                postIds.add(document.get("id"));
            }
            BatchGetPostRequest batchGetPageRequest = new BatchGetPostRequest();
            batchGetPageRequest.ids = postIds;
            List<PostResponse> posts = postWebService.batchGet(batchGetPageRequest);
            if (Boolean.TRUE.equals(request.highlightEnabled)) {
                Highlighter highlighter = highlighter(searchQuery);
                for (PostResponse postResponse : posts) {
                    postResponse.title = highlight("title", postResponse.title, highlighter);
                    postResponse.description = highlight("description", postResponse.description, highlighter);
                }
            }
            queryResponse.items = posts;
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

    public void index(PostCreatedMessage message) {
        try {
            PostContentResponse content = postContentWebService.getByPostId(message.id);
            Document document = document(message, content);
            index(document);
            indexWriter.commit();
            resetIndexSearcher();
        } catch (IOException e) {
            throw new ApplicationException("failed to index page, id={}", message.id, e);
        }
    }

    public void index(PostUpdatedMessage message) {
        try {
            PostContentResponse content = postContentWebService.getByPostId(message.id);
            Document document = document(message, content);
            index(document);
            indexWriter.commit();
            resetIndexSearcher();
        } catch (IOException e) {
            throw new ApplicationException("failed to index page, id={}", message.id, e);
        }
    }

    public void index(Document document) {
        try {
            IndexSearcher indexSearcher = indexSearcher();
            if (indexSearcher == null) {
                indexWriter.addDocument(document);
                return;
            }
            Term id = new Term("id", document.get("id"));
            TermQuery termQuery = new TermQuery(id);
            TopDocs search = indexSearcher.search(termQuery, 1);
            if (search.totalHits > 0) {
                indexWriter.updateDocument(id, document);
            } else {
                indexWriter.addDocument(document);
            }
        } catch (IOException e) {
            throw new ApplicationException("failed to index page, id={}, title={}", document.get("id"), document.get("title"), e);
        }
    }


    public void removeIndex(String postId) {
        try {
            indexWriter.deleteDocuments(new Term("id", postId));
            indexWriter.commit();
        } catch (IOException e) {
            throw new ApplicationException("failed to delete page, id={}", postId, e);
        }
    }

    public void fullIndex() {
        try {
            indexWriter.deleteAll();
            indexWriter.commit();

            PostQuery postQuery = new PostQuery();
            postQuery.status = PostStatus.ACTIVE;
            postQuery.page = 1;
            postQuery.limit = BATCH_SIZE;
            List<PostResponse> list;
            do {
                QueryResponse<PostResponse> posts = postWebService.find(postQuery);
                list = posts.items;
                if (list.isEmpty()) {
                    break;
                }
                List<String> postIds = posts.items.stream().map(post -> post.id).collect(Collectors.toList());
                Map<String, PostContentResponse> contents = postContentWebService.batchGetByPostIds(postIds).stream().collect(Collectors.toMap(content -> content.postId, content -> content));
                for (PostResponse post : posts) {
                    PostContentResponse content = contents.get(post.id);
                    index(document(post, content));
                }
                indexWriter.commit();
                resetIndexSearcher();
                postQuery.page += 1;
            } while (list.size() == BATCH_SIZE);
        } catch (IOException e) {
            throw new ApplicationException("failed to full index pages", e);
        }
    }

    private void resetIndexSearcher() {
        indexSearcher = null;
    }

    private Document document(PostResponse post, PostContentResponse content) {
        Document document = new Document();
        document.add(new StringField("id", post.id, Field.Store.YES));
        document.add(new StringField("path", post.path, Field.Store.NO));
        if (post.categoryId != null) {
            document.add(new StringField("categoryId", post.categoryId, Field.Store.NO));
        }
        String createdTime = DateTools.dateToString(Date.from(post.createdTime.toInstant()), DateTools.Resolution.MILLISECOND);
        document.add(new StringField("createdTime", createdTime, Field.Store.NO));
        document.add(new SortedDocValuesField("createdTime", new BytesRef(createdTime)));
        document.add(new StringField("updatedTime", DateTools.dateToString(Date.from(post.updatedTime.toInstant()), DateTools.Resolution.MILLISECOND), Field.Store.NO));
        if (post.userId != null) {
            document.add(new TextField("userId", post.userId, Field.Store.NO));
        }

        if (post.title != null) {
            document.add(new TextField("title", post.title, Field.Store.NO));
        }
        if (post.description != null) {
            document.add(new TextField("description", post.description, Field.Store.NO));
        }
        if (content != null) {
            document.add(new TextField("content", content(content), Field.Store.NO));
        }

        if (post.tags != null) {
            document.add(new TextField("tags", Joiner.on(" ").join(post.tags), Field.Store.NO));
        }

        if (post.keywords != null) {
            document.add(new TextField("keywords", Joiner.on(" ").join(post.keywords), Field.Store.NO));
        }
        return document;
    }

    private Document document(PostCreatedMessage post, PostContentResponse content) {
        Document document = new Document();
        document.add(new StringField("id", post.id, Field.Store.YES));
        document.add(new StringField("path", post.path, Field.Store.YES));
        if (post.categoryId != null) {
            document.add(new StringField("categoryId", post.categoryId, Field.Store.YES));
        }
        String createdTime = DateTools.dateToString(Date.from(post.createdTime.toInstant()), DateTools.Resolution.MILLISECOND);
        document.add(new StringField("createdTime", createdTime, Field.Store.YES));
        document.add(new SortedDocValuesField("createdTime", new BytesRef(createdTime)));
        document.add(new StringField("updatedTime", DateTools.dateToString(Date.from(post.updatedTime.toInstant()), DateTools.Resolution.MILLISECOND), Field.Store.YES));
        if (post.userId != null) {
            document.add(new TextField("userId", post.userId, Field.Store.NO));
        }

        if (post.title != null) {
            document.add(new TextField("title", post.title, Field.Store.YES));
        }
        if (post.description != null) {
            document.add(new TextField("description", post.description, Field.Store.YES));
        }
        if (content != null) {
            document.add(new TextField("content", content(content), Field.Store.YES));
        }

        if (post.tags != null) {
            document.add(new TextField("tags", Joiner.on(" ").join(post.tags), Field.Store.YES));
        }

        if (post.keywords != null) {
            document.add(new TextField("keywords", Joiner.on(" ").join(post.keywords), Field.Store.YES));
        }
        return document;
    }

    private Document document(PostUpdatedMessage post, PostContentResponse content) {
        Document document = new Document();
        document.add(new StringField("id", post.id, Field.Store.YES));
        document.add(new StringField("path", post.path, Field.Store.YES));
        if (post.categoryId != null) {
            document.add(new StringField("categoryId", post.categoryId, Field.Store.YES));
        }
        String createdTime = DateTools.dateToString(Date.from(post.createdTime.toInstant()), DateTools.Resolution.MILLISECOND);
        document.add(new StringField("createdTime", createdTime, Field.Store.YES));
        document.add(new SortedDocValuesField("createdTime", new BytesRef(createdTime)));
        document.add(new StringField("updatedTime", DateTools.dateToString(Date.from(post.updatedTime.toInstant()), DateTools.Resolution.MILLISECOND), Field.Store.YES));
        if (post.userId != null) {
            document.add(new TextField("userId", post.userId, Field.Store.NO));
        }

        if (post.title != null) {
            document.add(new TextField("title", post.title, Field.Store.YES));
        }
        if (post.description != null) {
            document.add(new TextField("description", post.description, Field.Store.YES));
        }
        if (content != null) {
            document.add(new TextField("content", content(content), Field.Store.YES));
        }

        if (post.tags != null) {
            document.add(new TextField("tags", Joiner.on(" ").join(post.tags), Field.Store.YES));
        }

        if (post.keywords != null) {
            document.add(new TextField("keywords", Joiner.on(" ").join(post.keywords), Field.Store.YES));
        }
        return document;
    }

    private String content(PostContentResponse content) {
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
