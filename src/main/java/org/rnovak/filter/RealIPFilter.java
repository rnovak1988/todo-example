package org.rnovak.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by rnovak on 10/5/15.
 */
public class RealIPFilter implements Filter {

    private static final Logger logger = Logger.getLogger(RealIPFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.error("IN DO FILTER");
        logger.error("Class: " + request.getClass().toString());
        if (request instanceof HttpServletRequest) {
            chain.doFilter(new RealIPWrapper((HttpServletRequest)request), response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
