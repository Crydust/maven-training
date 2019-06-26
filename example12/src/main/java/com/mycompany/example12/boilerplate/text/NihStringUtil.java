package com.mycompany.example12.boilerplate.text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mycompany.example12.boilerplate.BoilerplateException;

public final class NihStringUtil {

    private static final Map<String, String> HTML_MAP;
    private static final Pattern HTML_PATTERN;

    static {
        HTML_MAP = new HashMap<>();
        final boolean paranoid = true;
        if (paranoid) {
            for (int i = 0; i < 256; i++) {
                if (Character.isLetterOrDigit(i)) {
                    continue;
                }
                HTML_MAP.put(
                        String.valueOf((char) i),
                        String.format("&#x%02X;", i));
            }
        } else {
            final int i = 39;
            HTML_MAP.put(
                    String.valueOf((char) i),
                    String.format("&#x%02X;", i));
        }
        HTML_MAP.put("\"", "&quot;");
        HTML_MAP.put("&", "&amp;");
        HTML_MAP.put("<", "&lt;");
        HTML_MAP.put(">", "&gt;");
        final StringBuilder sb = new StringBuilder(2 + HTML_MAP.size() * 5);
        sb.append('[');
        for (String key : HTML_MAP.keySet()) {
            sb.append(Pattern.quote(key));
        }
        sb.append(']');
        HTML_PATTERN = Pattern.compile(sb.toString());
    }

    private NihStringUtil() {
        // NOOP
    }

    /**
     * Escapes all non-alphanumeric characters in a String into html entities.
     * <p>
     * Relies on HTML_MAP which should be initialized first.</p>
     * <p>
     * You should probably use commons-text
     * <a href="https://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/StringEscapeUtils.html">
     * StringEscapeUtils</a> instead.
     *
     * @param text
     * @return
     */
    public static String escapeHtml4(String text) {
        // see http://wonko.com/post/html-escaping
        // see https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet
        if (text == null || text.isEmpty()) {
            return text;
        }
        if (text.length() == 1) {
            return HTML_MAP.getOrDefault(text, text);
        }
        final Matcher matcher = HTML_PATTERN.matcher(text);
        if (!matcher.find()) {
            return text;
        }
        int position = 0;
        final StringBuilder sb = new StringBuilder();
        do {
            sb.append(text, position, matcher.start());
            sb.append(HTML_MAP.get(matcher.group()));
            position = matcher.end();
        } while (matcher.find());
        sb.append(text, position, text.length());
        return sb.toString();
    }

    /**
     * This method adds an attribute to the querystring of a url.
     * <p>
     * I'm obviously reinventing the wheel here. See
     * <a href="https://stackoverflow.com/questions/26177749/how-can-i-append-a-query-parameter-to-an-existing-url">How
     * can I append a query parameter to an existing URL?</a> for better
     * approaches to this problem</p>
     *
     * @param url
     * @param key
     * @param value
     * @return
     */
    public static String addAttributeToQuery(String url, String key, String value) {
        if (url == null) {
            return null;
        }
        Objects.requireNonNull(key, "key is null");
        Objects.requireNonNull(value, "value is null");
        int queryStart = url.indexOf('?');
        final int fragmentStart = url.indexOf('#');
        final String encodedKeyValuePair;
        try {
            encodedKeyValuePair = URLEncoder.encode(key, "UTF-8") + '=' + URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // impossible
            throw new BoilerplateException(ex);
        }
        if (queryStart == -1 && fragmentStart == -1) {
            return url + '?' + encodedKeyValuePair;
        }
        if (fragmentStart != -1 && queryStart > fragmentStart) {
            queryStart = -1;
        }
        final String prefix;
        final String query;
        final String fragment;
        if (queryStart != -1) {
            prefix = url.substring(0, queryStart);
            if (fragmentStart != -1) {
                query = url.substring(queryStart + 1, fragmentStart);
                fragment = url.substring(fragmentStart + 1);
            } else {
                query = url.substring(queryStart + 1);
                fragment = null;
            }
        } else {
            assert fragmentStart != -1;
            prefix = url.substring(0, fragmentStart);
            query = null;
            fragment = url.substring(fragmentStart + 1);
        }
        final int length = url.length() + encodedKeyValuePair.length() + (query == null || !query.isEmpty() ? 1 : 0);
        final StringBuilder sb = new StringBuilder(length);
        sb.append(prefix);
        sb.append('?');
        if (query != null && !query.isEmpty()) {
            sb.append(query);
            sb.append('&');
        }
        sb.append(encodedKeyValuePair);
        if (fragment != null) {
            sb.append('#');
            sb.append(fragment);
        }
        final String result = sb.toString();
        assert result.length() == length;
        return result;
    }

}
