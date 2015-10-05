package org.rnovak.filter;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by rnovak on 10/5/15.
 */
public class RealIPWrapper extends HttpServletRequestWrapper {

    private static final Logger logger = Logger.getLogger(RealIPWrapper.class);

    public RealIPWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public String getRemoteAddr() {
        String realIP = super.getHeader("X-Real-IP");
        logger.error("ip: " + realIP);

        return realIP != null ? realIP : super.getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        try {
            String hostname = InetAddress.getByName(this.getRemoteAddr()).getHostName();
            logger.error("Hostname: " + hostname);
            return InetAddress.getByName(this.getRemoteAddr()).getHostName();
        } catch (UnknownHostException|NullPointerException e) {
            return getRemoteAddr();
        }
    }

}
