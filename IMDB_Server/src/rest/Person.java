package rest;

import Service.MoviesManager;
import Service.PersonManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("Persons")
public class Person {

    PersonManager Service = new PersonManager();

    @GET
    @Path("Get/{id}")
    public String Get(@PathParam("id") String id){
        return Service.Get(id).toString();
    }

    @GET
    @Path("GetDirectorByMovie/{id}")
    public String GetDirectorByMovie(@PathParam("id") String id){
        return Service.GetDirectorByMovie(id).toString();
    }
    @GET
    @Path("GetActorByMovie/{id}")
    public String GetActorByMovie(@PathParam("id") String id){
        return Service.GetActorByMovie(id).toString();
    }

    @GET
    @Path("GetAll")
    public String GetAll(){
        return Service.GetAll().toString();
    }

}
