package eu.erdin.throttler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author robert.erdin@gmail.com
 */
public interface Throttler {
    boolean forward(HttpServletRequest request);
}
