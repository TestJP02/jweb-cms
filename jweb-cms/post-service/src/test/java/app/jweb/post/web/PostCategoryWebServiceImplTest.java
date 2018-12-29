package app.jweb.post.web;

import app.jweb.database.DatabaseModule;
import app.jweb.message.MessageModule;
import app.jweb.post.PostModuleImpl;
import app.jweb.post.api.PostCategoryWebService;
import app.jweb.post.api.category.CategoryNodeResponse;
import app.jweb.post.api.category.CategoryQuery;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.post.api.category.CategoryTreeQuery;
import app.jweb.post.api.category.CreateCategoryRequest;
import app.jweb.scheduler.SchedulerModule;
import app.jweb.service.ServiceModule;
import app.jweb.test.AppExtension;
import app.jweb.test.Install;
import app.jweb.util.collection.QueryResponse;
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
@Install({PostModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class, SchedulerModule.class})
public class PostCategoryWebServiceImplTest {
    @Inject
    PostCategoryWebService pageCategoryWebService;

    CategoryResponse parentCategory;

    @BeforeEach
    public void setup() {
        parentCategory = pageCategoryWebService.create(createCategoryRequest("node1", "/node1", null, Lists.newArrayList("tag1")));
        pageCategoryWebService.create(createCategoryRequest("node2", "/node2", parentCategory.id, Lists.newArrayList("tag2", "tag21")));
        pageCategoryWebService.create(createCategoryRequest("node3", "/node3", parentCategory.id, Lists.newArrayList("tag3", "tag31")));
    }

    @Test
    public void find() {
        CategoryQuery query = new CategoryQuery();
        QueryResponse<CategoryResponse> categories = pageCategoryWebService.find(query);
        assertEquals(4, categories.items.size());
    }

    @Test
    public void tree() {
        CategoryTreeQuery categoryTreeQuery = new CategoryTreeQuery();
        List<CategoryNodeResponse> tree = pageCategoryWebService.tree(categoryTreeQuery);
        assertEquals("node1", tree.get(0).displayName);
        assertEquals(2, tree.get(0).children.size());
    }

    private CreateCategoryRequest createCategoryRequest(String name, String path, String parentId, List<String> tags) {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.displayName = name;
        createCategoryRequest.parentId = parentId;
        createCategoryRequest.description = "description";
        createCategoryRequest.path = path;
        createCategoryRequest.keywords = Lists.newArrayList();
        createCategoryRequest.tags = tags;
        return createCategoryRequest;
    }
}