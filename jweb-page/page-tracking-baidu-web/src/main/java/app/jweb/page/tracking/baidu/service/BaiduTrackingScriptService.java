package app.jweb.page.tracking.baidu.service;

/**
 * @author chi
 */
public class BaiduTrackingScriptService {
    private final String id;

    public BaiduTrackingScriptService(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return id != null;
    }

    public String script() {
        if (id == null) {
            return "";
        }
        return "var _hmt = _hmt || [];(function() {" + "  var hm = document.createElement(\"script\"); hm.src = \"https://hm.baidu.com/hm.js?" + id + "\"; "
            + "var s = document.getElementsByTagName(\"script\")[0]; s.parentNode.insertBefore(hm, s);})();";
    }
}
