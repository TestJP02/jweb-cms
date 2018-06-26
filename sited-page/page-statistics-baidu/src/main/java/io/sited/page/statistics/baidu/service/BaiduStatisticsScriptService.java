package io.sited.page.statistics.baidu.service;

/**
 * @author chi
 */
public class BaiduStatisticsScriptService {
    private final String id;

    public BaiduStatisticsScriptService(String id) {
        this.id = id;
    }

    public String script() {
        if (id == null) {
            return "";
        }
        return "var _hmt = _hmt || [];(function() {" + "  var hm = document.createElement(\"script\"); hm.src = \"https://hm.baidu.com/hm.js?" + id + "\"; "
            + "var s = document.getElementsByTagName(\"script\")[0]; s.parentNode.insertBefore(hm, s);})();";
    }
}
