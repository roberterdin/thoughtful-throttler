package eu.erdin.throttler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author robert.erdin@gmail.com
 */
public class FixedLengthThrottler implements Throttler {

    @Override
    public boolean forward(HttpServletRequest request) {
        return true;
    }
}
