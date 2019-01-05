//package app.jweb.page.web.service.component;
//
//import app.jweb.page.web.AbstractPostComponent;
//import app.jweb.page.web.Bindings;
//import app.jweb.post.api.PostWebService;
//import app.jweb.post.api.post.PostQuery;
//import app.jweb.post.api.post.PostResponse;
//import app.jweb.template.Attributes;
//import app.jweb.template.Children;
//import app.jweb.template.IntegerAttribute;
//import app.jweb.template.StringAttribute;
//import app.jweb.util.collection.QueryResponse;
//import com.google.common.collect.Lists;
//
//import javax.inject.Inject;
//import java.io.IOException;
//import java.io.OutputStream;
//
///**
// * @author chi
// */
//public class UserPostLinkListComponent extends AbstractPostComponent {
//    @Inject
//    PostWebService postWebService;
//
//    public UserPostLinkListComponent() {
//        super("user-post-link-list", "component/user-post-link-list/user-post-link-list.html", Lists.newArrayList(
//            new StringAttribute("userId", null),
//            new IntegerAttribute("limit", 5)));
//    }
//
//    @Override
//    protected void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
//        String userId = attributes.get("userId");
//        if (userId == null) {
//            userId = bindings.post().userId();
//        }
//        if (userId == null) {
//            return;
//        }
//        PostQuery postQuery = new PostQuery();
//        postQuery.userId = userId;
//        postQuery.page = 1;
//        postQuery.limit = attributes.get("limit");
//        postQuery.sortingField = "updatedTime";
//        postQuery.desc = true;
//        QueryResponse<PostResponse> posts = postWebService.find(postQuery);
//        bindings.put("posts", posts.items);
//        template().output(bindings, out);
//    }
//}
