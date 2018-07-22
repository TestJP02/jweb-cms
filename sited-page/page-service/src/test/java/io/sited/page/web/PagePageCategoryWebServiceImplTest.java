package io.sited.page.web;

import com.google.common.collect.Lists;
import io.sited.database.DatabaseModule;
import io.sited.message.MessageModule;
import io.sited.page.PageModuleImpl;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.category.CategoryNodeResponse;
import io.sited.page.api.category.CategoryQuery;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.category.CategoryTreeQuery;
import io.sited.page.api.category.CreateCategoryRequest;
import io.sited.scheduler.SchedulerModule;
import io.sited.service.ServiceModule;
import io.sited.test.AppExtension;
import io.sited.test.Install;
import io.sited.util.collection.QueryResponse;
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
@Install({PageModuleImpl.class, DatabaseModule.class, MessageModule.class, ServiceModule.class, SchedulerModule.class})
public class PagePageCategoryWebServiceImplTest {
    @Inject
    PageCategoryWebService pageCategoryWebService;

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
        assertEquals(3, categories.items.size());
    }

    @Test
    public void tree() {
        CategoryTreeQuery categoryTreeQuery = new CategoryTreeQuery();
        List<CategoryNodeResponse> tree = pageCategoryWebService.tree(categoryTreeQuery);
        assertEquals("node1", tree.get(0).displayName);
        assertEquals(2, tree.get(0).children.size());
    }

    @Test
    public void subTree() {
        List<CategoryNodeResponse> nodes = pageCategoryWebService.subTree(Lists.newArrayList(parentCategory.id));
        assertEquals(2, nodes.size());
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