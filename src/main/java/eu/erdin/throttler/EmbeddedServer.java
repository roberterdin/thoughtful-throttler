package eu.erdin.throttler;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * @author robert.erdin@gmail.com
 */
public class EmbeddedServer {
    public static void main(String[] args) throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool(4, 4);



        final Server server = new Server(threadPool);

        ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setName("unsecured"); // named connector
        httpConnector.setPort(8080);

        server.addConnector(httpConnector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
//        final Client httpClient = AsyncClientBuilder.newClient();

        ServletHolder servletHolder = new ServletHolder(SmileyFiberProxyServlet.class);
        servletHolder.setInitParameter("targetUri", "https://raw.githubusercontent.com/nicolasbaer/thirstythrottler/master/README.md");
        servletHolder.setInitParameter("log", "false");
        context.addServlet(servletHolder, "/proxy");

        server.start();
        server.join();
    }
}
