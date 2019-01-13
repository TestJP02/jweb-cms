package app.jweb.user.admin.web.ajax;


import app.jweb.captcha.web.CaptchaCode;
import app.jweb.user.admin.UserAdminOptions;
import app.jweb.user.admin.web.ajax.user.BatchDeleteUserAJAXRequest;
import app.jweb.user.admin.web.ajax.user.ChangePasswordAJAXRequest;
import app.jweb.user.admin.web.ajax.user.ChangePasswordRequest;
import app.jweb.user.admin.web.ajax.user.LoginAJAXRequest;
import app.jweb.user.admin.web.ajax.user.LoginAJAXResponse;
import app.jweb.user.admin.web.ajax.user.UserAJAXResponse;
import app.jweb.user.admin.web.ajax.user.UserCreateAJAXRequest;
import app.jweb.user.admin.web.ajax.user.UserFindAJAXRequest;
import app.jweb.user.admin.web.ajax.user.UserUpdateAJAXRequest;
import app.jweb.user.api.UserGroupWebService;
import app.jweb.user.api.UserWebService;
import app.jweb.user.api.group.BatchGetRequest;
import app.jweb.user.api.user.BatchDeleteUserRequest;
import app.jweb.user.api.user.CreateUserRequest;
import app.jweb.user.api.user.LoginRequest;
import app.jweb.user.api.user.LoginResponse;
import app.jweb.user.api.user.UpdatePasswordRequest;
import app.jweb.user.api.user.UpdateUserRequest;
import app.jweb.user.api.user.UserQuery;
import app.jweb.user.api.user.UserResponse;
import app.jweb.user.api.user.UserStatus;
import app.jweb.util.collection.QueryResponse;
import app.jweb.web.Cookies;
import app.jweb.web.SessionInfo;
import app.jweb.web.UserInfo;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Path("/admin/api/user")
public class UserAdminAJAXController {
    @Inject
    UserWebService userWebService;
    @Inject
    UserGroupWebService userGroupWebService;
    @Inject
    UserAdminOptions userAdminOptions;
    @Inject
    CaptchaCode captchaCode;
    @Inject
    SessionInfo sessionInfo;
    @Inject
    ContainerRequestContext requestContext;
    @Inject
    UserInfo userInfo;

    @Path("/login")
    @POST
    public Response login(LoginAJAXRequest loginAJAXRequest) {
        captchaCode.validate(loginAJAXRequest.captchaCode);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = loginAJAXRequest.username;
        loginRequest.password = loginAJAXRequest.password;
        loginRequest.autoLogin = loginAJAXRequest.autoLogin;
        LoginResponse authenticationResponse = userWebService.login(loginRequest);

        sessionInfo.put("USER_ID", authenticationResponse.user.id);

        LoginAJAXResponse loginAJAXResponse = new LoginAJAXResponse();
        Cookie cookie = requestContext.getCookies().get("fromURL");
        loginAJAXResponse.fromURL = cookie == null ? null : cookie.getValue();
        String autoLoginCookie = Boolean.TRUE.equals(loginAJAXRequest.autoLogin) ? authenticationResponse.autoLoginToken : null;
        return Response.ok().entity(loginAJAXResponse).cookie(new NewCookie(userAdminOptions.autoLoginCookie, autoLoginCookie, "/", null, null, Integer.MAX_VALUE, false))
            .build();
    }

    @RolesAllowed("LIST")
    @Path("/self")
    @GET
    public Boolean self() {
        return userInfo.isAuthenticated();
    }

    @RolesAllowed("GET")
    @Path("/{id}")
    @GET
    public UserAJAXResponse get(@PathParam("id") String id) {
        return response(userWebService.get(id));
    }

    @RolesAllowed("LIST")
    @Path("/find")
    @PUT
    public QueryResponse<UserAJAXResponse> find(UserFindAJAXRequest request) {
        return userWebService.find(query(request)).map(this::response);
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}")
    @PUT
    public UserAJAXResponse update(@PathParam("id") String id, UserUpdateAJAXRequest request) {
        return response(userWebService.update(id, updateUserRequest(request)));
    }

    @RolesAllowed("UPDATE")
    @Path("/self/password")
    @PUT
    public void changePassword(ChangePasswordRequest request) {
        LoginRequest authenticationRequest = new LoginRequest();
        authenticationRequest.username = request.user;
        authenticationRequest.password = request.oldPassword;
        UserResponse user = userWebService.login(authenticationRequest).user;

        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.password = request.newPassword;
        updatePasswordRequest.requestBy = "user-admin";
        userWebService.updatePassword(user.id, updatePasswordRequest);
    }


    @RolesAllowed("UPDATE")
    @Path("/password")
    @PUT
    public void changePassword(ChangePasswordAJAXRequest request) {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.password = request.password;
        updatePasswordRequest.requestBy = "user-admin";
        userWebService.updatePassword(request.userId, updatePasswordRequest);
    }

    @RolesAllowed("CREATE")
    @POST
    public UserAJAXResponse create(UserCreateAJAXRequest request) {
        return response(userWebService.create(createUserRequest(request)));
    }

    @RolesAllowed("DELETE")
    @Path("/{id}")
    @DELETE
    public void delete(@PathParam("id") String id) {
        BatchDeleteUserRequest batchDeleteUserRequest = new BatchDeleteUserRequest();
        batchDeleteUserRequest.ids = Lists.newArrayList(id);
        batchDeleteUserRequest.requestBy = "user-admin";
        userWebService.batchDelete(batchDeleteUserRequest);
    }

    @RolesAllowed("UPDATE")
    @Path("/{id}/revert")
    @PUT
    public void revert(@PathParam("id") String id, Request request) {
        userWebService.revert(id, userInfo.username());
    }

    @RolesAllowed("DELETE")
    @PUT
    public void batchDelete(BatchDeleteUserAJAXRequest request) {
        BatchDeleteUserRequest batchDeleteUserRequest = new BatchDeleteUserRequest();
        batchDeleteUserRequest.ids = request.ids;
        batchDeleteUserRequest.requestBy = "user-admin";
        userWebService.batchDelete(batchDeleteUserRequest);
    }

    @Path("/logout")
    @GET
    public Response logout() {
        sessionInfo.invalidate();
        return Response.ok().cookie(Cookies.removeCookie(userAdminOptions.autoLoginCookie)).build();
    }

    private UserQuery query(UserFindAJAXRequest request) {
        UserQuery instance = new UserQuery();
        instance.query = request.query;
        instance.status = request.status;
        instance.userGroupId = request.userGroupId;
        instance.page = request.page;
        instance.limit = request.limit;
        instance.sortingField = request.sortingField;
        instance.desc = request.desc;
        return instance;
    }

    private UpdateUserRequest updateUserRequest(UserUpdateAJAXRequest ajaxRequest) {
        UpdateUserRequest request = new UpdateUserRequest();
        request.username = ajaxRequest.username;
        request.nickname = ajaxRequest.nickname;
        request.email = ajaxRequest.email;
        request.phone = ajaxRequest.phone;
        request.imageURL = ajaxRequest.imageURL;
        request.userGroupIds = ajaxRequest.userGroupIds;
        request.description = Objects.requireNonNullElse(ajaxRequest.description, "");
        request.tags = ajaxRequest.tags;
        request.fields = ajaxRequest.fields;
        request.requestBy = "user-admin";
        return request;
    }

    private CreateUserRequest createUserRequest(UserCreateAJAXRequest ajaxRequest) {
        CreateUserRequest request = new CreateUserRequest();
        request.username = ajaxRequest.username;
        request.nickname = ajaxRequest.nickname;
        request.password = ajaxRequest.password;
        request.email = ajaxRequest.email;
        request.phone = ajaxRequest.phone;
        request.imageURL = ajaxRequest.imageURL;
        request.type = ajaxRequest.type;
        //todo get current language
        request.language = Locale.getDefault().toLanguageTag();
        request.userGroupIds = ajaxRequest.userGroupIds;
        request.status = UserStatus.ACTIVE;
        request.description = ajaxRequest.description;
        request.requestBy = "user-admin";
        request.tags = ajaxRequest.tags;
        request.fields = ajaxRequest.fields;
        return request;
    }

    private UserAJAXResponse response(UserResponse response) {
        UserAJAXResponse ajaxResponse = new UserAJAXResponse();
        ajaxResponse.id = response.id;
        ajaxResponse.username = response.username;
        ajaxResponse.nickname = response.nickname;
        ajaxResponse.imageURL = response.imageURL;
        ajaxResponse.email = response.email;
        ajaxResponse.phone = response.phone;
        ajaxResponse.userGroupIds = response.userGroupIds;
        BatchGetRequest request = new BatchGetRequest();
        request.ids = response.userGroupIds;
        ajaxResponse.userGroup = Joiner.on(",").join(userGroupWebService.batchGet(request).stream().map(userGroupResponse -> userGroupResponse.name).collect(Collectors.toList()));
        ajaxResponse.type = response.type;
        ajaxResponse.createdTime = response.createdTime;
        ajaxResponse.createdBy = response.createdBy;
        ajaxResponse.updatedTime = response.updatedTime;
        ajaxResponse.updatedBy = response.updatedBy;
        ajaxResponse.status = response.status;
        ajaxResponse.description = response.description;
        ajaxResponse.tags = response.tags;
        ajaxResponse.fields = response.fields;
        return ajaxResponse;
    }
}
