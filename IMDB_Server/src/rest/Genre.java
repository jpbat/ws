package rest;

import Service.GenresManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("Genres")
public class Genre {

    GenresManager Service = new GenresManager();

    @GET
    @Path("GetByMovie/{id}")
    public String Get(@PathParam("id") String id){
        return Service.GetByMovie(id).toString();
    }

    @GET
    @Path("GetAll")
    public String GetAll(){
        return Service.GetAll().toString();
    }


}
