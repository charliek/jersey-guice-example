package net.cknudsen.jerseyexample.web;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.inject.servlet.RequestScoped;
import com.sun.jersey.api.view.Viewable;

@Path("/")
@RequestScoped
public class GuiceResource {

    @QueryParam("x") String x;

    @GET
    @Produces("text/html")
    public Viewable getIt() {
    	final Map<String,Object> vars = new HashMap<String,Object>();
        vars.put( "msg", "hello world" );
        return new Viewable( "/index", vars );
    }
}
