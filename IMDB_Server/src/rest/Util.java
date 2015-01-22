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
    @Path("Recommended/")
    public String GetRecommended(@QueryParam("director") String director, @QueryParam("genres") String genres,@QueryParam("actors")  String actors,@QueryParam("studios")  String studios ,@QueryParam("personId")  String personId){
        return Service.Recommend( director, genres,  actors, studios,personId).toString();
    }


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
