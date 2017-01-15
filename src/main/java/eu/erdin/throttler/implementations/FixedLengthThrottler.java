package eu.erdin.throttler.implementations;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.stoyanr.evictor.ConcurrentMapWithTimedEviction;
import com.stoyanr.evictor.EvictionScheduler;
import com.stoyanr.evictor.map.ConcurrentHashMapWithTimedEviction;
import com.stoyanr.evictor.scheduler.RegularTaskEvictionScheduler;
import eu.erdin.throttler.Throttler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author robert.erdin@gmail.com
 */
public class FixedLengthThrottler implements Throttler {

    private static final int CONCURRENT_SESSIONS = 300;

    // probably significantly too low
    private static final int CONCURRENCY_LEVEL = 16;

    // in ms
    private static final long SESSION_LENGTH = 5L;
    private static final String COOKIE_NAME = "thirsty";



    private final TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();
    private final ConcurrentMapWithTimedEviction<String, Integer> activeSessions;


    public FixedLengthThrottler(){
        // Create regular task eviction scheduler with a delay of 750 microseconds
        EvictionScheduler<String, Integer> scheduler =
                new RegularTaskEvictionScheduler<>(750, TimeUnit.MICROSECONDS);

        activeSessions = new ConcurrentHashMapWithTimedEviction<>(CONCURRENT_SESSIONS, 0.75f, CONCURRENCY_LEVEL, scheduler);
    }

    @Override
    /**
     * This is not threadsafe but accidental forwards/rejects do not impact the functionality since active sessions will
     * always be forwarded.
     */
    public boolean forward(HttpServletRequest request, HttpServletResponse response) {

        String session = getSessionOrNull(request);

        if (session == null && activeSessions.size() >= CONCURRENT_SESSIONS){
            return false;
        }else if (session != null && !activeSessions.containsKey(session)){
            // recurring user with expired session
            if (activeSessions.size() >= CONCURRENT_SESSIONS){
                return false;
            }else {
                activeSessions.put(session,1,SESSION_LENGTH);
                return true;
            }
        }else if (session != null && activeSessions.containsKey(session)){
            // recurring user with active session
            activeSessions.replace(session, 1, SESSION_LENGTH);
            return true;
        }

        // create new session
        Cookie cookie = new Cookie(COOKIE_NAME, timeBasedGenerator.generate().toString());
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        response.addCookie(cookie);

        return true;
    }

    private static String getSessionOrNull(HttpServletRequest request){
        for (Cookie cookie : request.getCookies()){
            if (cookie.getName().equals(COOKIE_NAME)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
