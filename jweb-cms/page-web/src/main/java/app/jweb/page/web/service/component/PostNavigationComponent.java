//package app.jweb.page.web.service.component;
//
//import app.jweb.page.web.AbstractPostComponent;
//import app.jweb.page.web.Bindings;
//import app.jweb.post.api.PostWebService;
//import app.jweb.post.api.post.PostNavigationResponse;
//import app.jweb.template.Attributes;
//import app.jweb.template.Children;
//import app.jweb.template.StringAttribute;
//import com.google.common.collect.Lists;
//
//import javax.inject.Inject;
//import java.io.IOException;
//import java.io.OutputStream;
//
///**
// * @author chi
// */
//public class PostNavigationComponent extends AbstractPostComponent {
//    @Inject
//    PostWebService postWebService;
//
//    public PostNavigationComponent() {
//        super("post-navigation", "component/post-navigation/post-navigation.html", Lists.newArrayList(new StringAttribute("postId", null)));
//    }
//
//    @Override
//    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
//        String postId = attributes.get("postId");
//        if (postId == null) {
//            postId = bindings.post().id();
//        }
//        if (postId == null) {
//            return;
//        }
//        PostNavigationResponse navigation = postWebService.navigation(postId);
//        bindings.put("prev", navigation.prev);
//        bindings.put("next", navigation.next);
//        template().output(bindings, out);
//    }
//}
