package rest;

import Service.MoviesManager;
import Service.PersonManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("Persons")
public class Person {

    PersonManager Service = new PersonManager();

    @GET
    @Path("Get/{id}")
    public String Get(@PathParam("id") String id){
        JSONArray json = Service.Get(id);
        JSONObject elements= (JSONObject) json.get(0);

        return elements.toString();
    }

    @GET
    @Path("Get")
    public String GetbyId(@QueryParam("id") String id) {
        JSONArray json = Service.Get(id);
        JSONObject elements= (JSONObject) json.get(0);

        return elements.toString();
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
    public String GetAll(@QueryParam("offset") int offset,@QueryParam("limit") int limit){

        if(limit==0){
            return Service.GetAll().toString();

        }else{
            return Service.GetAll(offset,limit).toString();
        }
    }

}
