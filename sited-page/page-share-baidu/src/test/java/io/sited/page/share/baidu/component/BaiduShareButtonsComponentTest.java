package io.sited.page.share.baidu.component;

import com.google.common.collect.Maps;
import io.sited.resource.ClasspathResourceRepository;
import io.sited.template.Template;
import io.sited.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author chi
 */
public class BaiduShareButtonsComponentTest {
    private TemplateEngine templateEngine = new TemplateEngine();

    @BeforeEach
    public void setup() {
        templateEngine.addRepository(new ClasspathResourceRepository("web"));
        BaiduShareButtonsComponent component = new BaiduShareButtonsComponent();
        templateEngine.addComponent(component);
    }

    @Test
    public void output() throws IOException {
        Template template = templateEngine.template("template/test-baidu-share.html").orElseThrow(RuntimeException::new);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        template.output(Maps.newHashMap(), output);
        assertEquals("<!doctype html><html lang=\"en-US\" xmlns:j=\"\"><head></head><body><div class=\"bdsharebuttonbox\"><a href=\"#\" class=\"bds_more\" data-cmd=\"more\"></a><a href=\"#\" class=\"bds_qzone\" data-cmd=\"qzone\" title=\"分享到QQ空间\"></a><a href=\"#\" class=\"bds_tsina\" data-cmd=\"tsina\" title=\"分享到新浪微博\"></a><a href=\"#\" class=\"bds_tqq\" data-cmd=\"tqq\" title=\"分享到腾讯微博\"></a><a href=\"#\" class=\"bds_renren\" data-cmd=\"renren\" title=\"分享到人人网\"></a><a href=\"#\" class=\"bds_weixin\" data-cmd=\"weixin\" title=\"分享到微信\"></a></div><script>window._bd_share_config = {\"common\": {\"bdSnsKey\": {}, \"bdText\": \"\", \"bdMini\": \"2\", \"bdMiniList\": false, \"bdPic\": \"\", \"bdStyle\": \"0\", \"bdSize\": \"32\"}, \"share\": {}};\n" +
            "with (document) 0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = '//bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];</script></body></html>", new String(output.toByteArray()));
    }
}