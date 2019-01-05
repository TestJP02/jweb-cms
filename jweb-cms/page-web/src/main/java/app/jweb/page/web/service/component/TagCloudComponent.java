//package app.jweb.page.web.service.component;
//
//import app.jweb.page.web.AbstractPostComponent;
//import app.jweb.page.web.Bindings;
//import app.jweb.post.api.PostTagWebService;
//import app.jweb.post.api.tag.PostTagQuery;
//import app.jweb.post.api.tag.PostTagResponse;
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
//public class TagCloudComponent extends AbstractPostComponent {
//    @Inject
//    PostTagWebService postTagWebService;
//
//    public TagCloudComponent() {
//        super("tag-cloud", "component/post-tag-cloud/post-tag-cloud.html",
//            Lists.newArrayList(new IntegerAttribute("limit", 20), new StringAttribute("title", null)));
//    }
//
//    @Override
//    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
//        PostTagQuery postTagQuery = new PostTagQuery();
//        postTagQuery.page = 1;
//        postTagQuery.limit = attributes.get("limit");
//        QueryResponse<PostTagResponse> tags = postTagWebService.find(postTagQuery);
//        bindings.putAll(attributes);
//        bindings.put("tags", tags);
//        template().output(bindings, out);
//    }
//}
