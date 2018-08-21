package com.gaochen.novel.downloader.util;


import java.net.MalformedURLException;
import java.net.URL;

/**
 * url and html utils.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class UrlUtils {

    /**
     * canonicalizeUrl
     * <br>
     * Borrowed from Jsoup.
     *
     * @param url url
     * @param refer refer
     * @return canonicalizeUrl
     */
    public static String canonicalizeUrl(String url, String refer) {
        URL base;
        try {
            try {
                base = new URL(refer);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute may be abs on its own, so try that
                URL abs = new URL(refer);
                return abs.toExternalForm();
            }
            // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
            if (url.startsWith("?"))
                url = base.getPath() + url;
            URL abs = new URL(base, url);
            return abs.toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }

}
