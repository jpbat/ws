package rest;

import Service.GenresManager;
import Service.MoviesManager;
import Service.PersonManager;
import Service.StudiosManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("Movies")
public class Movie {

    MoviesManager Service = new MoviesManager();
    PersonManager PersonService = new PersonManager();
    StudiosManager StudioService = new StudiosManager();
    GenresManager GenreService = new GenresManager();

    private JSONArray AddInfo(JSONArray json){
        for(int i=0; i<json.length(); i++){
            JSONObject current = (JSONObject)json.get(i);
            String movieId = current.get("id").toString();

            current.put("Actors",PersonService.GetActorByMovie(movieId));
            current.put("Directors",PersonService.GetDirectorByMovie(movieId));
            current.put("Studios",StudioService.GetbyMovie(movieId));
            current.put("Genres",GenreService.GetByMovie(movieId));
        }

        return json;
    }

    @GET
    @Path("Get/{id}")
    public String Get(@PathParam("id") String id) {
        JSONArray json = Service.Get(id);
        JSONObject elements= (JSONObject) AddInfo(json).get(0);

        return elements.toString();
    }

    @GET
    @Path("Get")
    public String GetbyId(@QueryParam("id") String id) {
        JSONArray json = Service.Get(id);
        JSONObject elements= (JSONObject) AddInfo(json).get(0);

        return elements.toString();
    }

    @GET
    @Path("GetAll")
    public String GetAll(){
        JSONArray json = Service.GetAll();

        return  AddInfo(json).toString();
    }

}
