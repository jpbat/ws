package Service;

import org.apache.jena.atlas.lib.StrUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.QueryParam;

/**
 * Created by Joao on 07/01/2015.
 */
public class UtilManager extends Connection {

    String StringMatchingQuery = StrUtils.strjoinNL
                     ( "PREFIX ns: <http://www.movierecomendation.pt/ontology/movierecomendation.owl#>"
                     , "PREFIX text: <http://jena.apache.org/text#>"
                     , "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                     , "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                     , "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                     , "SELECT * "
                     , " { ?Uri text:query ( '%s*' ) ."
                     , "   OPTIONAL { ?Uri ns:hasMediaName ?name }."
                     , "   OPTIONAL { ?Uri ns:hasPersonName ?name }."
                     , "   OPTIONAL { ?Uri ns:hasStudioName ?name }."
                     , "   OPTIONAL { ?Uri rdf:type ?typeUri. ?typeUri rdfs:subClassOf ns:Media.}."
                     , "   OPTIONAL { ?Uri rdf:type ?typeUri.?typeUri rdf:type owl:Class.}."
                     , " }") ;

    private JSONArray AddInfo(JSONArray json){
        for(int i=0; i<json.length(); i++){
            JSONObject current = (JSONObject)json.get(i);
            String GenreUri = current.get("typeUri").toString();
            String[] parts = GenreUri.split("#");
            current.put("type",parts[1]);
        }

        return json;
    }

    public JSONArray StringMatching(String text){
        return AddInfo(this.PerformQuery(String.format(StringMatchingQuery, text)));
    }

    public JSONArray StringMatching(String text, int offset, int limit){

        return AddInfo(this.PerformQuery(String.format(StringMatchingQuery, text)+" LIMIT "+limit+" OFFSET "+offset));
    }

}
