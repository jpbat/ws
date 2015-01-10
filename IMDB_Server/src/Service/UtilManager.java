package Service;

import org.apache.jena.atlas.lib.StrUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;


public class UtilManager extends Connection {

    static ArrayList<String> ProfessionList;
    static ArrayList<String> GenreList;

    private enum Type{
        NONE,
        GENRE,
        PROFESSION,
        NAME,
        MOVIE,
        SERIE}

    private String StringMatchingQuery = StrUtils.strjoinNL
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

        if(GenreList==null || ProfessionList ==null){
            ProfessionManager pService = new ProfessionManager();
            GenresManager gService = new GenresManager();

            ProfessionList = new ArrayList<String>();
            GenreList = new ArrayList<String>();

            JSONArray GenreJson = gService.GetAll();
            JSONArray ProfessionJson = pService.GetAll();

            for (int i = 0; i <GenreJson.length(); i++) {
                JSONObject aux = (JSONObject)GenreJson.get(i);
                GenreList.add(aux.getString("name").toLowerCase());
            }

            for (int i = 0; i <ProfessionJson.length(); i++) {
                JSONObject aux = (JSONObject)ProfessionJson.get(i);
                ProfessionList.add(aux.getString("name").toLowerCase());
            }
        }

        String [] queryElements = text.toLowerCase().split("\\s+");

        Type [] ElementTypes = new Type[queryElements.length];

        for (int i = 0; i < queryElements.length; i++) {
            if(GenreList.contains(queryElements[i])){
                ElementTypes[i]=Type.GENRE;
                System.out.println("Genre");
                continue;
            }
            if(ProfessionList.contains(queryElements[i])){
                System.out.println("Profession");
                ElementTypes[i]=Type.PROFESSION;
                continue;
            }
            if("serie"==queryElements[i]){
                System.out.println("Serie");
                ElementTypes[i]=Type.SERIE;
                continue;
            }
            if("movie"==queryElements[i]){
                System.out.println("movie");
                ElementTypes[i]=Type.MOVIE;
                continue;
            }

            ElementTypes[i]=Type.NONE;
            System.out.println("NONE");
        }
        return AddInfo(this.PerformQuery(String.format(StringMatchingQuery, text)+" LIMIT "+limit+" OFFSET "+offset));
    }

}
