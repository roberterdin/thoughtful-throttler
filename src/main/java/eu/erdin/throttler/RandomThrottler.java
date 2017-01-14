package eu.erdin.throttler;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @author robert.erdin@gmail.com
 *         created on 14/01/17.
 */
public class RandomThrottler implements Throttler{
    private static Random RANDOM = new Random();
    @Override
    public boolean forward(HttpServletRequest request) {
        return RANDOM.nextBoolean();
    }
}
