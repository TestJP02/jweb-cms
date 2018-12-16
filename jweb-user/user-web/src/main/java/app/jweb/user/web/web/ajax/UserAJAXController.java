package app.jweb.user.web.web.ajax;


import app.jweb.pincode.api.PinCodeWebService;
import app.jweb.pincode.api.pincode.CreatePinCodeRequest;
import app.jweb.pincode.api.pincode.PinCodeResponse;
import app.jweb.pincode.web.PinCode;
import app.jweb.user.api.UserWebService;
import app.jweb.user.api.user.ApplyPasswordRequest;
import app.jweb.user.api.user.CreateUserRequest;
import app.jweb.user.api.user.LoginRequest;
import app.jweb.user.api.user.LoginResponse;
import app.jweb.user.api.user.ResetPasswordRequest;
import app.jweb.user.api.user.UpdatePasswordRequest;
import app.jweb.user.api.user.UpdateUserRequest;
import app.jweb.user.api.user.UserResponse;
import app.jweb.user.web.UserWebOptions;
import app.jweb.user.web.web.ajax.user.ChangePasswordRequest;
import app.jweb.user.web.web.ajax.user.LoginAJAXRequest;
import app.jweb.user.web.web.ajax.user.ResetPasswordAJAXRequest;
import app.jweb.user.web.web.ajax.user.UpdateEmailAJAXRequest;
import app.jweb.user.web.web.ajax.user.UserAJAXResponse;
import app.jweb.captcha.web.CaptchaCode;
import app.jweb.user.web.service.UserInfoContextProvider;
import app.jweb.user.web.service.UsernameStrategy;
import app.jweb.user.web.util.Email;
import app.jweb.user.web.web.ajax.user.ApplyPasswordAJAXRequest;
import app.jweb.user.web.web.ajax.user.LoginAJAXResponse;
import app.jweb.user.web.web.ajax.user.RegisterAJAXRequest;
import app.jweb.user.web.web.ajax.user.UpdatePhoneAJAXRequest;
import app.jweb.user.web.web.ajax.user.UpdateUserAJAXRequest;
import app.jweb.user.web.web.ajax.user.UserInfoResponse;
import app.jweb.util.exception.Exceptions;
import app.jweb.web.ClientInfo;
import app.jweb.web.Cookies;
import app.jweb.web.LoginRequired;
import app.jweb.web.SessionInfo;
import app.jweb.web.UserInfo;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author chi
 */
@Path("/web/api/user")
public class UserAJAXController {
    public static final String COOKIE_FROM_URL = "_from_url";
    @Inject
    UserWebService userWebService;
    @Inject
    UserWebOptions userWebOptions;
    @Inject
    PinCodeWebService pinCodeWebService;

    @Inject
    SessionInfo sessionInfo;

    @Inject
    CaptchaCode captchaCode;

    @Inject
    ClientInfo clientInfo;

    @Inject
    UserInfo userInfo;

    @Inject
    ContainerRequestContext requestContext;

    @Inject
    PinCode pinCode;

    @Path("/login")
    @POST
    public Response login(LoginAJAXRequest loginAJAXRequest) throws IOException {
        captchaCode.validate(loginAJAXRequest.captchaCode);

        LoginRequest authenticationRequest = new LoginRequest();
        authenticationRequest.username = loginAJAXRequest.username;
        authenticationRequest.password = loginAJAXRequest.password;
        authenticationRequest.autoLogin = loginAJAXRequest.autoLogin;

        LoginResponse authenticationResponse = userWebService.login(authenticationRequest);
        UserResponse user = authenticationResponse.user;
        sessionInfo.put(UserInfoContextProvider.SESSION_USER_ID, user.id);

        if (Boolean.TRUE.equals(loginAJAXRequest.autoLogin)) {
            return Response.ok().entity(loginAJAXResponse(user.id))
                .cookie(new NewCookie(userWebOptions.autoLoginCookie, authenticationResponse.autoLoginToken, "/", null, null, userWebOptions.autoLoginMaxAge, false))
                .cookie(Cookies.removeCookie(COOKIE_FROM_URL)).build();
        } else {
            return Response.ok(loginAJAXResponse(user.id)).cookie(Cookies.removeCookie(COOKIE_FROM_URL)).build();
        }
    }

    @Path("/register")
    @POST
    public Response register(RegisterAJAXRequest registerAJAXRequest) throws IOException {
        UserResponse user;
        boolean pinCodeValidate = pinCode.validate(registerAJAXRequest.pinCode);
        if (!pinCodeValidate) {
            throw Exceptions.badRequestException("pinCode", "user.error.pinCodeInvalid");
        }
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.username = registerAJAXRequest.username;
        createUserRequest.nickname = registerAJAXRequest.nickname;
        createUserRequest.email = email(registerAJAXRequest.username, registerAJAXRequest.email);
        createUserRequest.phone = phone(registerAJAXRequest.username, registerAJAXRequest.phone);
        createUserRequest.requestBy = "user-web";
        createUserRequest.password = registerAJAXRequest.password;
        createUserRequest.language = clientInfo.language();
        user = userWebService.create(createUserRequest);

        if (Boolean.TRUE.equals(userWebOptions.registerAutoLoginEnabled)) {
            sessionInfo.put(UserInfoContextProvider.SESSION_USER_ID, user.id);
        }
        return Response.ok().entity(loginAJAXResponse(user.id))
            .cookie(Cookies.removeCookie(COOKIE_FROM_URL)).build();
    }

    private String email(String username, String email) {
        if (userWebOptions.usernameStrategy == UsernameStrategy.EMAIL) {
            return email == null ? username : email;
        } else if (userWebOptions.usernameStrategy == UsernameStrategy.EMAIL_PHONE) {
            return email == null && Email.isEmail(username) ? username : email;
        } else {
            return email;
        }
    }

    private String phone(String username, String phone) {
        if (userWebOptions.usernameStrategy == UsernameStrategy.PHONE) {
            return phone == null ? username : phone;
        } else if (userWebOptions.usernameStrategy == UsernameStrategy.EMAIL_PHONE) {
            return phone == null && !Email.isEmail(username) ? username : phone;
        } else {
            return phone;
        }
    }

    @LoginRequired
    @Path("/self")
    @PUT
    public void update(UpdateUserAJAXRequest userAJAXRequest) throws IOException {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.nickname = userAJAXRequest.nickname;
        if (userAJAXRequest.imageURL != null) {
            updateUserRequest.imageURL = userAJAXRequest.imageURL;
        }
        if (userAJAXRequest.description == null) {
            updateUserRequest.description = "";
        } else {
            updateUserRequest.description = userAJAXRequest.description;
        }
        userWebService.update(userInfo.id(), updateUserRequest);
    }

    @LoginRequired
    @Path("/self")
    @GET
    public UserAJAXResponse self() {
        UserAJAXResponse response = new UserAJAXResponse();
        response.id = userInfo.id();
        response.username = userInfo.username();
        response.email = userInfo.email();
        response.phone = userInfo.phone();
        response.nickname = userInfo.nickname();
        response.imageURL = userInfo.imageURL();
        return response;
    }

    @LoginRequired
    @Path("/self/phone")
    @PUT
    public void updatePhone(UpdatePhoneAJAXRequest updatePhoneAJAXRequest) throws IOException {
        if (userInfo.username().equals(userInfo.phone())) {
            throw Exceptions.badRequestException("phone", "not allowed to change username");
        }
        pinCode.validate(updatePhoneAJAXRequest.pinCode);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.phone = updatePhoneAJAXRequest.phone;
        updateUserRequest.requestBy = "user-web";
        userWebService.update(userInfo.id(), updateUserRequest);
        sessionInfo.delete("pinCode");
    }

    @LoginRequired
    @Path("/self/email")
    @PUT
    public void updateEmail(UpdateEmailAJAXRequest updateEmailAJAXRequest) throws IOException {
        if (userInfo.username().equals(userInfo.email())) {
            throw Exceptions.badRequestException("phone", "not allowed to change username");
        }

        pinCode.validate(updateEmailAJAXRequest.pinCode);
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.email = updateEmailAJAXRequest.email;
        updateUserRequest.requestBy = "user-web";
        userWebService.update(userInfo.id(), updateUserRequest);
        sessionInfo.delete("pinCode");
    }

    @Path("/logout")
    @GET
    public Response logout() throws IOException {
        if (userInfo.isAuthenticated()) {
            userWebService.deleteAutoLoginToken(userInfo.id());
            sessionInfo.invalidate();
        }
        return Response.ok().cookie(new NewCookie(userWebOptions.autoLoginCookie, null)).build();
    }

    @Path("/reset-password")
    @POST
    public void resetPassword(ResetPasswordAJAXRequest resetPasswordAJAXRequest) throws IOException {
        captchaCode.validate(resetPasswordAJAXRequest.captchaCode);

        CreatePinCodeRequest createPinCodeRequest = new CreatePinCodeRequest();
        if (resetPasswordAJAXRequest.username.contains("@")) {
            createPinCodeRequest.email = resetPasswordAJAXRequest.username;
        } else {
            createPinCodeRequest.phone = resetPasswordAJAXRequest.username;
        }
        createPinCodeRequest.ip = clientInfo.ip();
        createPinCodeRequest.requestBy = "user-web";
        PinCodeResponse pinCodeResponse = pinCodeWebService.create(createPinCodeRequest);
        sessionInfo.put("pinCode", pinCodeResponse.code);

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.username = resetPasswordAJAXRequest.username;
        resetPasswordRequest.token = pinCodeResponse.code;
        userWebService.resetPassword(resetPasswordRequest);

    }

    @Path("/reset-password/apply")
    @POST
    public void applyResetPassword(ApplyPasswordAJAXRequest applyPasswordAJAXRequest) throws IOException {
        ApplyPasswordRequest applyPasswordRequest = new ApplyPasswordRequest();
        applyPasswordRequest.username = applyPasswordAJAXRequest.username;
        applyPasswordRequest.pinCode = applyPasswordAJAXRequest.pinCode;
        applyPasswordRequest.newPassword = applyPasswordAJAXRequest.newPassword;
        applyPasswordRequest.requestBy = "user-web";
        userWebService.applyPassword(applyPasswordRequest);
        sessionInfo.delete("pinCode");
    }

    @LoginRequired
    @Path("/self/password")
    @PUT
    public void changePassword(ChangePasswordRequest changePasswordRequest) throws IOException {
        LoginRequest authenticationRequest = new LoginRequest();
        authenticationRequest.username = userInfo.username();
        authenticationRequest.password = changePasswordRequest.oldPassword;
        try {
            userWebService.login(authenticationRequest);
        } catch (BadRequestException e) {
            throw (BadRequestException) Exceptions.badRequestException("oldPassword", "user.error.inactive").initCause(e);
        }

        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.password = changePasswordRequest.newPassword;
        updatePasswordRequest.requestBy = "user-web";
        userWebService.updatePassword(userInfo.id(), updatePasswordRequest);
    }

    private LoginAJAXResponse loginAJAXResponse(String userId) {
        LoginAJAXResponse response = new LoginAJAXResponse();
        response.userId = userId;
        Cookie url = requestContext.getCookies().get(COOKIE_FROM_URL);
        if (url != null) {
            response.fromURL = url.getValue();
        }
        return response;
    }

    @Path("/user-info")
    @GET
    public Response userInfo() {
        UserInfoResponse infoResponse = new UserInfoResponse();
        infoResponse.id = userInfo.id();
        infoResponse.username = userInfo.username();
        infoResponse.nickname = userInfo.nickname();
        infoResponse.description = userInfo.description();
        infoResponse.roles = userInfo.roles();
        infoResponse.imageURL = userInfo.imageURL();
        infoResponse.authenticated = userInfo.isAuthenticated();
        return Response.ok(infoResponse).build();
    }
}
