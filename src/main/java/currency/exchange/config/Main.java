package currency.exchange.config;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        Context ctx = tomcat.addContext("", null);

        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(WebConfig.class);

        DispatcherServlet servlet = new DispatcherServlet(appContext);
        Tomcat.addServlet(ctx, "dispatcher", servlet);
        ctx.addServletMappingDecoded("/", "dispatcher");

        tomcat.start();
        tomcat.getServer().await();
    }
}
