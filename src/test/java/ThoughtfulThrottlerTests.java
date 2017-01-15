import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author robert.erdin@gmail.com
 *         created on 15/01/17.
 */
public class ThoughtfulThrottlerTests {

    @Test
    public void testCacheSize() throws ExecutionException {

        Cache<String, String> cache =  CacheBuilder.newBuilder()
                .maximumSize(2)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build();


        cache.get("foo", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "bar";
            }
        });

        cache.get("bar", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "bar";
            }
        });

        cache.get("baz", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "bar";
            }
        });

        cache.get("baz", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "bar";
            }
        });

    }
}
