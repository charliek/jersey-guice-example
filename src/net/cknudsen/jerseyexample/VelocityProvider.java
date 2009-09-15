package net.cknudsen.jerseyexample;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.ext.Provider;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.view.servlet.VelocityViewServlet;
import org.apache.velocity.tools.view.servlet.WebappLoader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.spi.template.TemplateProcessor;

@Provider
@Singleton
public class VelocityProvider implements TemplateProcessor {

	private final Logger log = Logger.getLogger(VelocityProvider.class.toString());
	
	@Inject
	public VelocityProvider(ServletContext context) throws Exception {
		super();
		// set the base directory to load files from
		Velocity.setApplicationAttribute(VelocityViewServlet.SERVLET_CONTEXT_KEY, context);
		Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "webapp");
		Velocity.setProperty("webapp.resource.loader.description", "Velocity Classpath Resource Loader");
		Velocity.setProperty("webapp.resource.loader.class", WebappLoader.class.getName());
		
		// set up the logger to use the jdk logger
		Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.JdkLogChute");
		
		// set the default encoding to utf-8
		Velocity.setProperty(RuntimeConstants.ENCODING_DEFAULT, "utf-8");
		try {
			Velocity.init();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error Configuring Velocity setup", e);
			throw e;
		}
	}
	
	@Override
	public String resolve(String path) {
        // accept both '/path/to/template' and '/path/to/template.vm'
		return path.endsWith("vm") ? path : path + ".vm";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeTo(String resolvedPath, Object model, OutputStream out)
			throws IOException {
        log.log(Level.FINEST,
        		"Evaluating velocity template (" + resolvedPath + ") with model of type " 
        		+( model == null ? "null" : model.getClass().getSimpleName()));
        //out.flush(); // send status + headers

        Template tmpl;
		try {
			tmpl = Velocity.getTemplate(resolvedPath);
			log.log(Level.FINEST, "OK: Resolved velocity template {0}", resolvedPath);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error processing velocity template @ " + resolvedPath, e);
        	throw new IOException("Error processing velocity template @ " + resolvedPath, e);
		}
        
        OutputStreamWriter writer = new OutputStreamWriter(out);

        VelocityContext context = new VelocityContext();
        Map<String,Object> vars;
        if ( model instanceof Map ) {
        	vars = (Map<String, Object>) model;
        	for( Entry<String, Object> e : vars.entrySet()){
    			context.put(e.getKey(), e.getValue());
    		}
        } else {
        	context.put("it", model);
        }
        
        try {    		
        	tmpl.merge(context, writer);
        	writer.flush();
        	log.log(Level.FINEST, "OK: Processed velocity template {0}", resolvedPath);
        } catch (Exception e) {
        	log.log(Level.SEVERE, "Error processing velocity template @ " + resolvedPath, e);
        	throw new IOException("Error processing velocity template @ " + resolvedPath, e);
        }
	}

}
