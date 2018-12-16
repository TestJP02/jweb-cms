package app.jweb.user.api.oauth;

/**
 * @author chi
 */
public enum Provider {
    GITHUB("https://api.github.com/user", "user", true),
    GOOGLE("https://www.googleapis.com/oauth2/v1/userinfo", "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile", true),
    FACEBOOK("https://graph.facebook.com/v2.8/me", null, true),
    TWITTER("https://api.twitter.com/1.1/account/verify_credentials.json?include_email=true", null, false),
    WEIBO("https://api.weibo.com/2/users/show.json?uid=%s", null, true),
    WX(null, null, false);

    public final String protectedResourceUrl;
    public final String scope;
    public final Boolean oauth2;

    Provider(String protectedResourceUrl, String scope, Boolean oauth2) {
        this.protectedResourceUrl = protectedResourceUrl;
        this.scope = scope;
        this.oauth2 = oauth2;
    }
}
