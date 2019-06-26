package com.mycompany.example12.boilerplate.web;

import com.mycompany.example12.boilerplate.text.NihStringUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

/**
 * Adds a CSRFToken to all urls encoded with encodeURL or encodeRedirectURL. In
 * jsps you can use c:url for this. This class checks the token for all requests
 * with a method other than GET.
 *
 * <p>
 * Should be replaced by
 * <a href="http://tomcat.apache.org/tomcat-8.5-doc/config/filter.html#CSRF_Prevention_Filter">org.apache.catalina.filters.CsrfPreventionFilter</a>.</p>
 *
 * @author kristof
 */
public class NihCsrfFilter implements Filter {

    private static final String SESSION_CSRF_TOKEN = "SESSION_CSRF_TOKEN";
    private static final String REQUEST_CSRF_TOKEN = "CSRFToken";
    private static final Logger LOGGER = Logger.getLogger(NihCsrfFilter.class.toString());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // NOOP
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest
                && response instanceof HttpServletResponse) {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            final HttpServletResponse httpResponse = (HttpServletResponse) response;
            String token;
            final String method = httpRequest.getMethod();
            if ("GET".equals(method)) {
                final HttpSession session = httpRequest.getSession(true);
                token = (String) session.getAttribute(SESSION_CSRF_TOKEN);
                if (token == null) {
                    token = uuidToBase64(UUID.randomUUID());
                    session.setAttribute(SESSION_CSRF_TOKEN, token);
                }
            } else {
                final HttpSession session = httpRequest.getSession(false);
                if (session == null) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                token = (String) session.getAttribute(SESSION_CSRF_TOKEN);
                if (!(token.equals(httpRequest.getParameter(REQUEST_CSRF_TOKEN)))) {
                    LOGGER.warning("Possible csrf hacking attempt thwarted.");
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }
            chain.doFilter(request, new NihServletResponse(httpResponse, token));
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // NOOP
    }

    private static class NihServletResponse extends HttpServletResponseWrapper {

        private final String token;

        private NihServletResponse(HttpServletResponse response, String token) {
            super(response);
            this.token = token;
        }

        @Override
        public String encodeRedirectUrl(String url) {
            return this.encodeRedirectURL(url);
        }

        @Override
        public String encodeUrl(String url) {
            return this.encodeURL(url);
        }

        @Override
        public String encodeRedirectURL(String url) {
            return super.encodeRedirectURL(addTokenToUrl(url, token));
        }

        @Override
        public String encodeURL(String url) {
            return super.encodeURL(addTokenToUrl(url, token));
        }

        private static String addTokenToUrl(String url, String token) {
            return NihStringUtil.addAttributeToQuery(url, REQUEST_CSRF_TOKEN, token);
        }

    }

    private static String uuidToBase64(UUID uuid) {
        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(ByteBuffer
                        .allocate(16)
                        .putLong(uuid.getMostSignificantBits())
                        .putLong(uuid.getLeastSignificantBits())
                        .array());
    }
}
