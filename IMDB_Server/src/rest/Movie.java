package rest;

import Service.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;

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
            current.put("Profession",PersonService.GetActorByMovie(movieId));
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
    public String GetAll(@QueryParam("offset") int offset,@QueryParam("limit") int limit,@QueryParam("genres") String genres){
        JSONArray json;

        if( !(genres==null) && !genres.isEmpty()){
            json = Service.GetAllByGenre(genres, offset,limit);
        }else
        if(limit==0){
            json = Service.GetAll();

        }else{
            json = Service.GetAll(offset,limit);
        }

        return  AddInfo(json).toString();
    }



    @GET
    @Path("GetRecent")
    public String GetRecent(){
        return Service.GetMostRecent().toString();
    }
}
