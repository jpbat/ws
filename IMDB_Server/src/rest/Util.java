package rest;

import Service.UtilManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("Util")
public class Util {

    UtilManager Service = new UtilManager();

    @GET
    @Path("Search/")
    public String Get(@QueryParam("query") String text,@QueryParam("offset") int offset,@QueryParam("limit") int limit){
        if(limit==0){
            return Service.StringMatching(text).toString();
        }else{
            return Service.StringMatching(text,offset,limit).toString();
        }

    }
}
