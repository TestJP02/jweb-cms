package app.jweb.web;

import app.jweb.AbstractModule;
import app.jweb.Binder;
import app.jweb.Configurable;
import app.jweb.resource.ClasspathResourceRepository;
import app.jweb.resource.FileResourceRepository;
import app.jweb.template.TemplateEngine;
import com.google.common.base.Strings;
import app.jweb.web.impl.AppInfoContextProvider;
import app.jweb.web.impl.ClientInfoContextProvider;
import app.jweb.web.impl.LocalSessionRepository;
import app.jweb.web.impl.RedisSessionRepository;
import app.jweb.web.impl.RequestInfoContextProvider;
import app.jweb.web.impl.ResourceMessageBodyWriter;
import app.jweb.web.impl.SessionInfoContextProvider;
import app.jweb.web.impl.SessionRepository;
import app.jweb.web.impl.SitemapService;
import app.jweb.web.impl.TemplateMessageBodyWriter;
import app.jweb.web.impl.Theme;
import app.jweb.web.impl.ThemedResourceRepository;
import app.jweb.web.impl.WebConfigImpl;
import app.jweb.web.impl.WebFilter;
import app.jweb.web.impl.WebTemplateFunctions;
import app.jweb.web.impl.component.ThemeCSSComponent;
import app.jweb.web.impl.component.ThemeScriptComponent;
import app.jweb.web.impl.controller.FaviconResourceController;
import app.jweb.web.impl.controller.HealthCheckController;
import app.jweb.web.impl.controller.NodeModulesResourceController;
import app.jweb.web.impl.controller.RobotsResourceController;
import app.jweb.web.impl.controller.SitemapController;
import app.jweb.web.impl.controller.SitemapIndexController;
import app.jweb.web.impl.controller.StaticResourceController;
import app.jweb.web.impl.controller.SwitchLanguageWebController;
import app.jweb.web.impl.controller.ThemeStaticResourceController;
import app.jweb.web.impl.exception.BadRequestWebExceptionMapper;
import app.jweb.web.impl.exception.DefaultWebExceptionMapper;
import app.jweb.web.impl.exception.ForbiddenWebExceptionMapper;
import app.jweb.web.impl.exception.NotAuthorizedWebExceptionMapper;
import app.jweb.web.impl.exception.NotFoundWebExceptionMapper;
import app.jweb.web.impl.exception.ValidationWebExceptionMapper;
import app.jweb.web.impl.processor.HrefElementProcessor;
import app.jweb.web.impl.processor.SrcElementProcessor;
import app.jweb.web.impl.processor.ThemeProcessor;

/**
 * @author chi
 */
public final class WebModule extends AbstractModule implements Configurable<WebConfig> {
    WebOptions webOptions;
    WebRoot webRoot;
    SitemapService sitemapService;
    TemplateEngine templateEngine;

    @Override
    protected void configure() {
        webOptions = options("web", WebOptions.class);
        bind(WebOptions.class).toInstance(webOptions);

        webRoot = new WebRoot();
        webRoot.add(new ClasspathResourceRepository("web"));
        webRoot.add(new FileResourceRepository(app().dir().resolve("web")));
        if (webOptions.roots != null) {
            webOptions.roots.forEach(dir -> webRoot.add(new FileResourceRepository(app().dir().resolve(dir))));
        }
        bind(WebRoot.class).toInstance(webRoot);

        templateEngine = new TemplateEngine().setCacheEnabled(webOptions.cacheEnabled);
        templateEngine.addRepository(webRoot);

        templateEngine.addElementProcessor(new HrefElementProcessor(webOptions.cdnBaseURLs, webRoot));
        templateEngine.addElementProcessor(new SrcElementProcessor(webOptions.cdnBaseURLs, webRoot));
        templateEngine.addFunctions(null, new WebTemplateFunctions(app().message()));
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
        webConfig.controller(ThemeStaticResourceController.class);
        webConfig.controller(FaviconResourceController.class);
        webConfig.controller(RobotsResourceController.class);
        webConfig.controller(NodeModulesResourceController.class);
        webConfig.controller(SwitchLanguageWebController.class);
        webConfig.controller(SitemapController.class);
        webConfig.controller(SitemapIndexController.class);

        if (!Strings.isNullOrEmpty(webOptions.theme)) {
            Theme theme = new Theme(webOptions.theme, templateEngine);
            webConfig.addElementProcessor(new ThemeProcessor());
            webConfig.addComponent(new ThemeCSSComponent(theme));
            webConfig.addComponent(new ThemeScriptComponent(theme));
            ThemedResourceRepository themedResourceRepository = new ThemedResourceRepository(webOptions.theme, webRoot);
            templateEngine.addRepository(themedResourceRepository);
        }

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

        WebCache sitemapCache = webConfig.createCache("sitemap");
        sitemapService = new SitemapService(app().baseURL(), sitemapCache);
        bind(SitemapService.class).toInstance(sitemapService);

        message("conf/messages/web");
    }

    @Override
    public WebConfig configurator(AbstractModule module, Binder binder) {
        return new WebConfigImpl(binder, webOptions, templateEngine, sitemapService, app());
    }
}
