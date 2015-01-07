package Service;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Joao on 19/12/2014.
 */
public class GenresManager extends Connection{
    private String GlobalNamespace="<http://www.movierecomendation.pt/ontology/movierecomendation.owl#>";
    private String owl = "<http://www.w3.org/2002/07/owl#>";
    private String rdf = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";

    String queryGetGenreByMovieId =
            "PREFIX nsowl:"+GlobalNamespace+" "+
            "SELECT ?uri WHERE { ?MovieUri nsowl:hasMediaId ?MovieId . ?MovieUri nsowl:hasGenre ?uri . FILTER regex(?MovieId, \"%s\") }";

    String queryGetAll =
            "PREFIX rdf:"+rdf+" "+
            "PREFIX owl:"+owl+" "+
            "PREFIX nsowl:"+GlobalNamespace+" "+
            "SELECT DISTINCT ?uri "+
            "WHERE {nsowl:Genre owl:equivalentClass ?node. ?node owl:oneOf ?ref. ?ref rdf:rest*/rdf:first ?uri .} "+
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
