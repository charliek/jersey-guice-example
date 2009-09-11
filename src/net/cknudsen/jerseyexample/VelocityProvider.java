package net.cknudsen.jerseyexample;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.spi.template.TemplateProcessor;

@Provider
public class VelocityProvider implements TemplateProcessor {

	@Override
	public String resolve(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeTo(String resolvedPath, Object model, OutputStream out)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Context
	public void setServletContext(ServletContext context) {

	}

}
