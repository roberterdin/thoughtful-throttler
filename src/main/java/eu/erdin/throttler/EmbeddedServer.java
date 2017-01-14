package eu.erdin.throttler;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author robert.erdin@gmail.com
 */
public class EmbeddedServer {
    public static void main(String[] args) throws Exception {
        final Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
//        final Client httpClient = AsyncClientBuilder.newClient();

        ServletHolder servletHolder = new ServletHolder(SmileyFiberProxyServlet.class);
        servletHolder.setInitParameter("targetUri", "http://www.tagesanzeiger.ch/");
        servletHolder.setInitParameter("log", "true");
        context.addServlet(servletHolder, "/proxy");

        server.start();
        server.join();
    }
}
