package app.jweb.captcha.simple.web;

import app.jweb.captcha.simple.CaptchaOptions;
import com.google.common.collect.Lists;
import app.jweb.web.SessionInfo;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.FlatColorBackgroundProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static app.jweb.captcha.simple.service.SimpleCaptchaImpl.SESSION_KEY;

/**
 * @author chi
 */
@Path("/captcha.jpg")
public class CaptchaController {
    @Inject
    CaptchaOptions options;

    @Inject
    SessionInfo sessionInfo;

    @GET
    @Consumes
    public Response image() throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String code = timestamp.substring(timestamp.length() - options.length, timestamp.length());
        sessionInfo.put(SESSION_KEY, code);
        Captcha captcha = new Captcha.Builder(100, 50)
            .addText(() -> code, new DefaultWordRenderer(Lists.newArrayList(Color.BLACK), Lists.newArrayList(new Font("Arial", 1, 40), new Font("Georgia", 1, 40))))
            .addBackground(new FlatColorBackgroundProducer(Color.WHITE))
            .addNoise()
            .build();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(captcha.getImage(), "jpg", out);
        return Response.ok(out.toByteArray()).type("image/jpeg").build();
    }
}
