package net.cknudsen.jerseyexample;

import java.util.HashMap;
import java.util.Map;

import net.cknudsen.jerseyexample.web.GuiceResource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class WebConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			
			@Override
			protected void configureServlets() {
				// providers
				// bind(VelocityProvider.class);
				
				// web resources
				bind(GuiceResource.class);
				Map<String, String> params = new HashMap<String, String>();
				//params.put("com.sun.jersey.config.feature.ImplicitViewables", "true");
				//params.put("com.sun.jersey.config.feature.Redirect", "true");
				serveRegex("/(images|css|jsp)/.*").with(DefaultWrapperServlet.class);
				params.put("com.sun.jersey.config.property.packages", "net.cknudsen.jerseyexample.web");
				serve("/*").with(GuiceContainer.class, params);
			}
		});
	}
}
