package Service;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Joao on 19/12/2014.
 */
public class GenresManager extends Connection{
    private String GlobalNamespace="<http://www.movierecomendation.pt/ontology/movierecomendation.owl#>";
    private String Namespace ="<http://www.movierecomendation.pt/Movie/>";
    private String owl = "<http://www.w3.org/2002/07/owl#>";
    private String rdf = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";

    String queryGetGenreByMovieId =
            "PREFIX nsowl:"+GlobalNamespace+" "+
                    "PREFIX ns:"+Namespace+" "+
                    "SELECT ?uri WHERE { ns:%s nsowl:hasGenres ?uri .}";

    String queryGetAll =
            "PREFIX rdf:"+rdf+" "+
            "PREFIX owl:"+owl+" "+
            "PREFIX nsowl:"+GlobalNamespace+" "+
            "PREFIX ns:"+Namespace+" "+
                    "SELECT DISTINCT ?uri "+
                    "WHERE { ?uri rdf:type owl:NamedIndividual } "+
                    "ORDER BY ?uri";

    private JSONArray AddInfo(JSONArray json){
        for(int i=0; i<json.length(); i++){
            JSONObject current = (JSONObject)json.get(i);
            String GenreUri = current.get("uri").toString();
            String[] parts = GenreUri.split("#");
            current.put("name",parts[1]);
        }

        return json;
    }

    public JSONArray GetAll(){
        return AddInfo(this.PerformQuery(queryGetAll));
    }

    public JSONArray GetByMovie(String id){
        return AddInfo(this.PerformQuery(String.format(queryGetGenreByMovieId, id)));
    }

}
