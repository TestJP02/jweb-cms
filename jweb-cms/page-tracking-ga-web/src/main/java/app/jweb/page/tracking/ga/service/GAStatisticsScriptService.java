package app.jweb.page.tracking.ga.service;

/**
 * @author chi
 */
public class GAStatisticsScriptService {
    private final String id;

    public GAStatisticsScriptService(String id) {
        this.id = id;
    }

    public String script() {
        if (id == null) {
            return "";
        }
        return "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){"
            + "(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),"
            + "m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)"
            + "})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');"
            + "ga('create', '" + id + "', 'auto');"
            + "ga('send', 'pageview');";
    }

    public boolean isEnabled() {
        return id != null;
    }
}
