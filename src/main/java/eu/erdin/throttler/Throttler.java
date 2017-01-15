package eu.erdin.throttler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author robert.erdin@gmail.com
 */
public interface Throttler {
    boolean forward(HttpServletRequest request, HttpServletResponse response);
}
