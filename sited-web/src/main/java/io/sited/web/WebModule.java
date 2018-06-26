package io.sited.web;

import com.google.common.collect.Lists;
import io.sited.AbstractModule;
import io.sited.Binder;
import io.sited.Configurable;
import io.sited.resource.ClasspathResourceRepository;
import io.sited.resource.FileResourceRepository;
import io.sited.resource.ResourceRepository;
import io.sited.template.TemplateEngine;
import io.sited.web.impl.AppInfoContextProvider;
import io.sited.web.impl.ClientInfoContextProvider;
import io.sited.web.impl.LocalSessionRepository;
import io.sited.web.impl.RedisSessionRepository;
import io.sited.web.impl.RequestInfoContextProvider;
import io.sited.web.impl.ResourceMessageBodyWriter;
import io.sited.web.impl.SessionInfoContextProvider;
import io.sited.web.impl.SessionRepository;
import io.sited.web.impl.TemplateMessageBodyWriter;
import io.sited.web.impl.WebConfigImpl;
import io.sited.web.impl.WebFilter;
import io.sited.web.impl.WebTemplateFunctions;
import io.sited.web.impl.controller.FaviconResourceController;
import io.sited.web.impl.controller.HealthCheckController;
import io.sited.web.impl.controller.NodeModulesResourceController;
import io.sited.web.impl.controller.RobotsResourceController;
import io.sited.web.impl.controller.StaticResourceController;
import io.sited.web.impl.exception.BadRequestWebExceptionMapper;
import io.sited.web.impl.exception.DefaultWebExceptionMapper;
import io.sited.web.impl.exception.ForbiddenWebExceptionMapper;
import io.sited.web.impl.exception.NotAuthorizedWebExceptionMapper;
import io.sited.web.impl.exception.NotFoundWebExceptionMapper;
import io.sited.web.impl.exception.ValidationWebExceptionMapper;
import io.sited.web.impl.template.processor.HrefElementProcessor;
import io.sited.web.impl.template.processor.SrcElementProcessor;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public final class WebModule extends AbstractModule implements Configurable<WebConfig> {
    WebOptions webOptions;
    WebRoot webRoot;
    TemplateEngine templateEngine;

    @Override
    protected void configure() {
        webOptions = options("web", WebOptions.class);
        bind(WebOptions.class).toInstance(webOptions);

        List<ResourceRepository> repositories = Lists.newArrayList(webOptions.roots.stream().map(dir -> new FileResourceRepository(Paths.get("").resolve(dir))).collect(Collectors.toList()));
        repositories.add(new FileResourceRepository(app().dir().resolve("web")));
        repositories.add(new ClasspathResourceRepository("web"));

        webRoot = new WebRoot(repositories.toArray(new ResourceRepository[0]));
        bind(WebRoot.class).toInstance(webRoot);

        templateEngine = new TemplateEngine()
            .addRepository(webRoot)
            .setCacheEnabled(webOptions.cacheEnabled);

        templateEngine.addElementProcessor(new HrefElementProcessor(webRoot));
        templateEngine.addElementProcessor(new SrcElementProcessor(webRoot));
        templateEngine.addFunctions(null, new WebTemplateFunctions(app().message(), webOptions));
        bind(TemplateEngine.class).toInstance(templateEngine);
        if (webOptions.session.redis == null) {
            bind(SessionRepository.class).toInstance(new LocalSessionRepository(webOptions.session));
        } else {
            bind(SessionRepository.class).toInstance(new RedisSessionRepository(webOptions.session));
        }
        bind(SessionManager.class);

        WebConfig webConfig = module(WebModule.class);
        webConfig.controller(HealthCheckController.class);
        webConfig.controller(StaticResourceController.class);
        webConfig.controller(FaviconResourceController.class);
        webConfig.controller(RobotsResourceController.class);
        webConfig.controller(NodeModulesResourceController.class);

//        webConfig.bind(UserInfo.class, UserInfoContextProvider.class);
        webConfig.bind(ClientInfo.class, ClientInfoContextProvider.class);
        webConfig.bind(SessionInfo.class, SessionInfoContextProvider.class);
        webConfig.bind(AppInfo.class, AppInfoContextProvider.class);
        webConfig.bind(RequestInfo.class, RequestInfoContextProvider.class);

        webConfig.bindResponseFilter(requestInjection(new WebFilter()));
        webConfig.bindMessageBodyWriter(requestInjection(new TemplateMessageBodyWriter()));
        webConfig.bindMessageBodyWriter(requestInjection(new ResourceMessageBodyWriter()));

        webConfig.bindExceptionMapper(requestInjection(new NotAuthorizedWebExceptionMapper()));
        webConfig.bindExceptionMapper(requestInjection(new NotFoundWebExceptionMapper()));
        webConfig.bindExceptionMapper(requestInjection(new ValidationWebExceptionMapper()));
        webConfig.bindExceptionMapper(requestInjection(new ForbiddenWebExceptionMapper()));
        webConfig.bindExceptionMapper(requestInjection(new BadRequestWebExceptionMapper()));
        webConfig.bindExceptionMapper(requestInjection(new DefaultWebExceptionMapper()));

        message("conf/messages/pagination");
    }

    @Override
    public WebConfig configurator(AbstractModule module, Binder binder) {
        return new WebConfigImpl(binder, templateEngine, app());
    }
}
