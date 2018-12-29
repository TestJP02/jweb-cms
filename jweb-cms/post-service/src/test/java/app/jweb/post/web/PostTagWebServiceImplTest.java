package app.jweb.post.web;


import app.jweb.post.PostModuleImpl;
import app.jweb.post.api.PostTagWebService;
import app.jweb.post.api.tag.BatchCreatePostTagRequest;
import app.jweb.post.api.tag.BatchGetTagRequest;
import app.jweb.post.api.tag.PostTagResponse;
import app.jweb.post.api.tag.PostTagView;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install(PostModuleImpl.class)
class PostTagWebServiceImplTest {
    @Inject
    PostTagWebService pageTagWebService;

    @BeforeEach
    void setup() {
        BatchCreatePostTagRequest request = new BatchCreatePostTagRequest();
        request.tags = Lists.newArrayList();

        PostTagView tag1 = new PostTagView();
        tag1.displayName = "1";
        request.tags.add(tag1);

        PostTagView tag2 = new PostTagView();
        tag2.displayName = "2";
        request.tags.add(tag2);

        pageTagWebService.batchCreate(request);
    }

    @Test
    void batchGet() {
        BatchGetTagRequest request = new BatchGetTagRequest();
        request.tags = Lists.newArrayList("1", "2");
        List<PostTagResponse> tags = pageTagWebService.batchGet(request);
        assertEquals(2, tags.size());
    }
}