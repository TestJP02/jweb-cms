//package app.jweb.page.web.service.component;
//
//import app.jweb.page.web.AbstractPageComponent;
//import app.jweb.page.web.Bindings;
//import app.jweb.page.web.service.PostService;
//import app.jweb.post.api.PageCategoryWebService;
//import app.jweb.post.api.category.CategoryResponse;
//import app.jweb.post.api.post.PostQuery;
//import app.jweb.post.api.post.PostStatus;
//import app.jweb.template.Attributes;
//import app.jweb.template.BooleanAttribute;
//import app.jweb.template.Children;
//import app.jweb.template.IntegerAttribute;
//import app.jweb.template.StringAttribute;
//import app.jweb.template.TemplateException;
//import com.google.common.collect.ImmutableList;
//
//import javax.inject.Inject;
//import java.io.IOException;
//import java.io.OutputStream;
//
///**
// * @author chi
// */
//public class CategoryPostListComponent extends AbstractPageComponent {
//    @Inject
//    PostService postService;
//    @Inject
//    PageCategoryWebService postCategoryWebService;
//
//    public CategoryPostListComponent() {
//        super("category-post-list", "component/category-post-list/category-post-list.html", ImmutableList.of(
//            new StringAttribute("categoryId", null),
//            new BooleanAttribute("paginationEnabled", true),
//            new StringAttribute("title", null),
//            new IntegerAttribute("page", null),
//            new IntegerAttribute("limit", 20)));
//    }
//
//    @Override
//    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
//        bindings.putAll(attributes);
//
//        String categoryId = attributes.get("categoryId");
//        String path = bindings.request().path();
//        String title = attributes.get("title");
//        bindings.put("title", title);
//        bindings.put("paginationEnabled", attributes.get("paginationEnabled"));
//
//        PostQuery postQuery = new PostQuery();
//        if (categoryId == null) {
//            CategoryResponse category = (CategoryResponse) bindings.get("category");
//            if (category != null) {
//                postQuery.categoryId = category.id;
//                bindings.put("category", category);
//                bindings.put("path", path == null ? category.path : path);
//            } else {
//                category = postCategoryWebService.findByPath("/").orElseThrow(() -> new TemplateException("missing category, path=/"));
//                bindings.put("category", category);
//                bindings.put("path", path == null ? category.path : path);
//            }
//        } else {
//            CategoryResponse category = postCategoryWebService.get(categoryId);
//            postQuery.categoryId = category.id;
//            bindings.put("category", category);
//            bindings.put("path", path == null ? category.path : path);
//        }
//
//        postQuery.page = attributes.get("page");
//        postQuery.limit = attributes.get("limit");
//        postQuery.sortingField = "updatedTime";
//        postQuery.desc = true;
//        postQuery.status = PostStatus.ACTIVE;
//        bindings.put("posts", postService.find(postQuery));
//        bindings.put("display", 10);
//        bindings.put("limit", postQuery.limit);
//        template().output(bindings, out);
//    }
//}
