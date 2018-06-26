package io.sited.page.web.service;

/**
 * @author chi
 */
public class PureCSSTemplateBuilderTest {
    /*private PureCSSTemplateBuilder pureCSSTemplateBuilder;

    @Before
    public void setup() {
        WebRoot webRoot = new WebRoot(new ClasspathResourceRepository("web"));
        TemplateManager templateManager = new TemplateManager(templateEngine);
        PageRelatedLinkListComponent pageRelatedLinkListComponent = new PageRelatedLinkListComponent(null, null);
        templateManager.addPageComponent(pageRelatedLinkListComponent.name(), pageRelatedLinkListComponent);
        ImageSliderComponent imageSliderComponent = new ImageSliderComponent();
        templateManager.addPageComponent(imageSliderComponent.name(), imageSliderComponent);
        pureCSSTemplateBuilder = new PureCSSTemplateBuilder(webRoot, templateManager);
    }

    @Test
    public void build() throws IOException {
        TemplateResource templateSource = pureCSSTemplateBuilder.build(pageLayoutResponse());
        String html = templateSource.toText(Charsets.UTF_8);
        Files.write(Paths.get("C:\\Users\\chi\\Workspace\\sited-page-project\\page-web\\src\\test\\resources\\web\\template\\1.html"), html.getBytes());
        assertNotNull(html);
    }

    private PageLayoutResponse pageLayoutResponse() {
        PageLayoutResponse pageLayoutResponse = new PageLayoutResponse();
        pageLayoutResponse.templatePath = "template/index.html";
        pageLayoutResponse.sections = Lists.newArrayList(headerSection(), bodySection(), footerSection());
        return pageLayoutResponse;
    }


    private PageLayoutSectionView bodySection() {
        PageLayoutSectionView body = new PageLayoutSectionView();
        body.id = UUID.randomUUID().toString();
        body.name = "body";
        body.contentSection = false;
        body.children = Lists.newArrayList(mainSection(), asideSection());
        return body;
    }

    private PageLayoutSectionView mainSection() {
        PageLayoutSectionView main = new PageLayoutSectionView();
        main.id = UUID.randomUUID().toString();
        main.name = "main";
        main.widths = mainWidth();
        main.contentSection = true;
        return main;
    }

    private PageLayoutSectionView asideSection() {
        PageLayoutSectionView aside = new PageLayoutSectionView();
        aside.id = UUID.randomUUID().toString();
        aside.name = "aside";
        aside.widths = asideWidth();
        aside.contentSection = false;

        PageComponentView pageRelatedListComponent = new PageComponentView();
        pageRelatedListComponent.name = "page-related-list";
        pageRelatedListComponent.id = UUID.randomUUID().toString();
        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("j:page", "page");
        attributes.put("size", "10");
        pageRelatedListComponent.attributes = attributes;

        aside.components = Lists.newArrayList(pageRelatedListComponent);
        return aside;
    }

    private PageLayoutSectionView footerSection() {
        PageLayoutSectionView footer = new PageLayoutSectionView();
        footer.id = UUID.randomUUID().toString();
        footer.widths = fullWidth();
        footer.contentSection = false;
        footer.name = "footer";

        PageComponentView footerComponent = new PageComponentView();
        footerComponent.name = "footer";
        footerComponent.id = UUID.randomUUID().toString();
        footerComponent.attributes = Maps.newHashMap();

        footer.components = Lists.newArrayList(footerComponent);
        return footer;
    }

    private PageLayoutSectionView headerSection() {
        PageLayoutSectionView header = new PageLayoutSectionView();
        header.id = UUID.randomUUID().toString();
        header.name = "header";
        header.contentSection = false;
        header.widths = fullWidth();

        PageComponentView headerComponent = new PageComponentView();
        headerComponent.name = "header";
        headerComponent.id = UUID.randomUUID().toString();
        headerComponent.attributes = Maps.newHashMap();

        header.components = Lists.newArrayList(headerComponent);
        return header;
    }

    private List<PageLayoutSectionWidthView> fullWidth() {
        PageLayoutSectionWidthView alwaysWidth = new PageLayoutSectionWidthView();
        alwaysWidth.screenWidth = "always";
        alwaysWidth.width = 1;
        return Lists.newArrayList(alwaysWidth);
    }

    private List<PageLayoutSectionWidthView> mainWidth() {
        PageLayoutSectionWidthView smallWidth = new PageLayoutSectionWidthView();
        smallWidth.screenWidth = "sm";
        smallWidth.width = 1;

        PageLayoutSectionWidthView mediumWidth = new PageLayoutSectionWidthView();
        mediumWidth.screenWidth = "md";
        mediumWidth.width = 18;

        PageLayoutSectionWidthView largeWidth = new PageLayoutSectionWidthView();
        largeWidth.screenWidth = "lg";
        largeWidth.width = 20;

        return Lists.newArrayList(smallWidth, mediumWidth, largeWidth);
    }

    private List<PageLayoutSectionWidthView> asideWidth() {
        PageLayoutSectionWidthView smallWidth = new PageLayoutSectionWidthView();
        smallWidth.screenWidth = "sm";
        smallWidth.width = 1;

        PageLayoutSectionWidthView mediumWidth = new PageLayoutSectionWidthView();
        mediumWidth.screenWidth = "md";
        mediumWidth.width = 6;

        PageLayoutSectionWidthView largeWidth = new PageLayoutSectionWidthView();
        largeWidth.screenWidth = "lg";
        largeWidth.width = 4;

        return Lists.newArrayList(smallWidth, mediumWidth, largeWidth);
    }*/
}