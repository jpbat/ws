package rest;

import Service.UtilManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by Joao on 07/01/2015.
 */
@Path("Util")
public class Util {

    UtilManager Service = new UtilManager();

    @GET
    @Path("Search/")
    public String Get(@QueryParam("query") String text){
        return Service.StringMatching(text).toString();
    }
}
