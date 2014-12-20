package rest;

import Service.PersonManager;
import Service.StudiosManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class Studio {
  StudiosManager Service = new StudiosManager();

  @GET
  @Path("Get/{id}")
    public String Get(@PathParam("id") String id){
    return Service.Get(id).toString();
  }

  @GET
  @Path("GetbyMovie/{id}")
    public String GetbyMovie(@PathParam("id") String id){
    return Service.GetbyMovie(id).toString();
  }


  @GET
  @Path("GetAll")
  public String GetAll(){
  return Service.GetAll().toString();
  }

}
