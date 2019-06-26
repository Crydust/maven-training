package com.mycompany.example12.boilerplate.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Add security related headers to each response.
 *
 * <p>Headers:</p>
 * <ul>
 * <li>X-Frame-Options:SAMEORIGIN</li>
 * <li>X-XSS-Protection: 1; mode=block</li>
 * <li>X-Content-Type-Options: nosniff</li>
 * </ul>
 *
 * <p>Should be replaced by <a href="http://tomcat.apache.org/tomcat-8.5-doc/config/filter.html#HTTP_Header_Security_Filter">org.apache.catalina.filters.HttpHeaderSecurityFilter</a>.</p>
 * @author kristof
 */
public class NihSecurityHeadersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // NOOP
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            final HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
            httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
            httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // NOOP
    }

}
