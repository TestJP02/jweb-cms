package app.jweb.lancher.setup.web;


import app.jweb.App;
import app.jweb.lancher.Launcher;
import app.jweb.lancher.setup.service.SetupManager;
import app.jweb.lancher.setup.web.setup.SetupRequest;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Collections;

/**
 * @author chi
 */
@Path("/web/api/install")
public class SetupWebController {
    @Inject
    App app;

    @Inject
    SetupManager setupManager;

    @Inject
    Launcher launcher;

    @POST
    public Response install(SetupRequest request) {
        setupManager.validateDatabase(request.database);
        setupManager.createProfile(app.dir().resolve("conf/app.yml"), request);
        launcher.restart(request.user);
        return Response.ok(Collections.singletonMap("success", "true")).status(200).build();
    }
}
