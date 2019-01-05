//package app.jweb.page.web.service.component;
//
//import app.jweb.ApplicationException;
//import app.jweb.page.web.AbstractPageComponent;
//import app.jweb.page.web.Bindings;
//import app.jweb.page.web.ContentEngine;
//import app.jweb.page.web.PageInfo;
//import app.jweb.page.web.service.PostService;
//import app.jweb.post.api.PostStatisticsWebService;
//import app.jweb.post.api.statistics.PostStatisticsResponse;
//import app.jweb.template.Attributes;
//import app.jweb.template.BooleanAttribute;
//import app.jweb.template.Children;
//import com.google.common.collect.ImmutableList;
//
//import javax.inject.Inject;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.Optional;
//
///**
// * @author chi
// */
//public class PostComponent extends AbstractPageComponent {
//    @Inject
//    PostStatisticsWebService postStatisticsWebService;
//
//    @Inject
//    PostService postService;
//
//    public PostComponent() {
//        super("post", "component/page-page/page-page.html", ImmutableList.of(
//            new BooleanAttribute("titleEnabled", true),
//            new BooleanAttribute("statisticsEnabled", true)));
//    }
//
//    @Override
//    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
//        PageInfo post = bindings.post();
//        Boolean titleEnabled = attributes.get("titleEnabled");
//        bindings.put("titleEnabled", titleEnabled);
//
//        Optional<PostStatisticsResponse> postStatisticsResponse = postStatisticsWebService.findById(post.id());
//        if (postStatisticsResponse.isPresent()) {
//            bindings.put("statistics", postStatisticsResponse.get());
//        } else {
//            PostStatisticsResponse statisticsResponse = new PostStatisticsResponse();
//            statisticsResponse.totalVisited = 0;
//            statisticsResponse.totalCommented = 0;
//            statisticsResponse.totalLiked = 0;
//            bindings.put("statistics", statisticsResponse);
//        }
//
//        if (post.content() != null) {
//            String engineName = post.fields().get("engine");
//            if (engineName == null) {
//                engineName = "markdown";
//            }
//            ContentEngine contentEngine = postService.engine(engineName);
//            if (contentEngine == null) {
//                throw new ApplicationException("missing content engine, name={}", engineName);
//            }
//            bindings.put("content", contentEngine.render(post.content()));
//        } else {
//            bindings.put("content", "");
//        }
//        template().output(bindings, out);
//    }
//}
