package rest;

import Service.ProfessionManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("Professions")
public class Profession {

    ProfessionManager Service = new ProfessionManager();

    @GET
    @Path("GetByPerson/{id}")
    public String Get(@PathParam("id") String id){
        return Service.GetByPerson(id).toString();
    }

    @GET
    @Path("GetAll")
    public String GetAll(){
        return Service.GetAll().toString();
    }


}
