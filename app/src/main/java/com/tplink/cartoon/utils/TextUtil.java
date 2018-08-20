/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.utils;

public class TextUtil {
    public static String getSearchText(String key, String baseTitle) {
        String title = baseTitle;
        if (key != null) {
            title = "";
            if (!key.equals(baseTitle)) {
                String[] titles = baseTitle.split(key);
                if (titles.length != 1) {
                    title = titles[0] + "<font color='#ff9a6a'>" + key + "</font>" + titles[1];
                } else {
                    if (baseTitle.indexOf(key) == 0) {
                        title = "<font color='#ff9a6a'>" + key + "</font>" + titles[0];
                    } else if (!titles[0].equals(baseTitle)) {
                        title = titles[0] + "<font color='#ff9a6a'>" + key + "</font>";
                    } else {
                        title = baseTitle;
                    }
                }
            } else {
                title = "<font color='#ff9a6a'>" + baseTitle + "</font>";
            }
        }
        return title;
    }
}
