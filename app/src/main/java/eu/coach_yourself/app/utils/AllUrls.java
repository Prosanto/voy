package eu.coach_yourself.app.utils;


import java.util.Vector;

public class AllUrls {

//    public static String HTTP = "http://kgninfosoft.com/voy/";
    public static String HTTP = "https://www.key-meditation.com/voy/";
    public static String BASEURL = HTTP + "webservices/";
    public static String jkey = "v786oy";

    private static String getcommonURLWithParamAndAction(String action,
                                                         Vector<KeyValue> params) {
        if (params == null || params.size() == 0) {
            return BASEURL + action;
        } else {
            String pString = "";
            for (final KeyValue obj : params) {
                String tex = UrlUtils.encode(obj.getValue().trim());
                pString += obj.getKey().trim() + "=" + tex
                        + "&";
            }
            if (pString.endsWith("&")) {
                pString = pString.subSequence(0, pString.length() - 1)
                        .toString();
            }
            return BASEURL + action + "?" + pString;
        }
    }
    public static String getCategoryURL(boolean language) {
        final Vector<KeyValue> temp = new Vector<KeyValue>();
        temp.addElement(new KeyValue("jkey", jkey));
        if (language)
            temp.addElement(new KeyValue("language", "English"));
        else
            temp.addElement(new KeyValue("language", "German"));
        return getcommonURLWithParamAndAction(
                "category.php", temp);

    }

    public static String getcontentURL(String category_id) {
        final Vector<KeyValue> temp = new Vector<KeyValue>();
        temp.addElement(new KeyValue("jkey", jkey));
        temp.addElement(new KeyValue("category_id", category_id));
        return getcommonURLWithParamAndAction(
                "content.php", temp);
    }


}
