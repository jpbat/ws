package Service;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Joao on 05/01/2015.
 */
public class ProfessionManager extends Connection {
    private String GlobalNamespace="<http://www.movierecomendation.pt/ontology/movierecomendation.owl#>";
    private String Namespace ="<http://www.movierecomendation.pt/Person/>";
    private String owl = "<http://www.w3.org/2002/07/owl#>";
    private String rdf = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";

    String queryGetProfessionByMovieId =
            "PREFIX nsowl:"+GlobalNamespace+" "+
                    "PREFIX ns:"+Namespace+" "+
                    "SELECT ?uri WHERE { ns:%s nsowl:hasProfession ?uri .}";

    String queryGetAll =
            "PREFIX rdf:"+rdf+" "+
                    "PREFIX owl:"+owl+" "+
                    "PREFIX nsowl:"+GlobalNamespace+" "+
                    "PREFIX ns:"+Namespace+" "+
                    "SELECT DISTINCT ?uri "+
                    "WHERE {nsowl:Profession owl:equivalentClass ?node. ?node owl:oneOf ?ref. ?ref rdf:rest*/rdf:first ?uri .} "+
                    "ORDER BY ?uri";

    private JSONArray AddInfo(JSONArray json){
        for(int i=0; i<json.length(); i++){
            JSONObject current = (JSONObject)json.get(i);
            String GenreUri = current.get("uri").toString();
            String[] parts = GenreUri.split("#");
            current.put("name",parts[1].replace("_"," "));
        }

        return json;
    }

    public JSONArray GetAll(){
        return AddInfo(this.PerformQuery(queryGetAll));
    }

    public JSONArray GetByPerson(String id){
        return AddInfo(this.PerformQuery(String.format(queryGetProfessionByMovieId, id)));
    }

}
