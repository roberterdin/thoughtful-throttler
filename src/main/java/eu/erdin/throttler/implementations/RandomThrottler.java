package eu.erdin.throttler.implementations;

import eu.erdin.throttler.Throttler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * @author robert.erdin@gmail.com
 *         created on 14/01/17.
 */
public class RandomThrottler implements Throttler {
    private static Random RANDOM = new Random();
    @Override
    public boolean forward(HttpServletRequest request, HttpServletResponse response) {
        return RANDOM.nextBoolean();
    }
}
