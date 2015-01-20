package Service;

import org.apache.jena.atlas.lib.StrUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;

public class UtilManager extends Connection {

    static ArrayList<String> ProfessionList;
    static ArrayList<String> GenreList;

    private String [] TypeFilters = new String[]{
                "",
                " || ?GenreUri = ns:%s",
                " || ?ProfessionUri = ns:%s",
                "",
                " || ?typeUri = ns:Movie",
                " || ?typeUri = ns:Serie",
                " || ?typeUri = ns:Person"
                };

    private enum Type{
        NONE(0),
        GENRE(1),
        PROFESSION(2),
        NAME(3),
        MOVIE(4),
        SERIE(5),
        PERSON(6);
        private final int value;
        private Type(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    private String StringMatchingQuery = StrUtils.strjoinNL
                     ( "PREFIX ns: <http://www.movierecomendation.pt/ontology/movierecomendation.owl#>"
                     , "PREFIX text: <http://jena.apache.org/text#>"
                     , "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                     , "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                     , "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                     , "SELECT DISTINCT ?Uri ?name ?typeUri ?img ?classification ?launched ?end ?start ?seasons ?Birth ?BirthPlace"
                     , "WHERE "
                     , " { %s"

                     , "   OPTIONAL { ?Uri ns:hasMediaName ?name }."
                     , "   OPTIONAL { ?Uri ns:hasPersonName ?name }."
                     , "   OPTIONAL { ?Uri ns:hasStudioName ?name }."

                     , "   OPTIONAL { ?Uri ns:hasMediaPoster ?img }."
                     , "   OPTIONAL { ?Uri ns:hasPersonPicture ?img }."

                     , "   OPTIONAL { ?Uri ns:hasMediaClassification ?classification }."
                     , "   OPTIONAL { ?Uri ns:hasMediaLaunchDate ?launched }. "
                     , "   OPTIONAL { ?Uri ns:hasSerieEnd ?end } ."
                     , "   OPTIONAL { ?Uri ns:hasSerieStart ?start } . "
                     , "   OPTIONAL { ?Uri ns:hasSerieSeason ?seasons } ."

                     , "   OPTIONAL { ?Uri ns:hasPersonBirth ?Birth }."
                     , "   OPTIONAL { ?Uri ns:hasPersonBirthPlace ?BirthPlace }."

                     , "   OPTIONAL { ?Uri rdf:type ?typeUri. ?typeUri rdfs:subClassOf ns:Media.}."
                     , "   OPTIONAL { ?Uri rdf:type ?typeUri.?typeUri rdf:type owl:Class.}."
                     , "   OPTIONAL { ?Uri ns:hasGenre ?GenreUri.}."
                     , "   OPTIONAL { ?Uri ns:hasProfession ?ProfessionUri.}."
                     , "   %s"
                     , " }") ;

    public JSONArray StringMatching(String text){
        return AddInfo(this.PerformQuery(String.format(StringMatchingQuery, text)));
    }

    public JSONArray StringMatching(String text, int offset, int limit){

        AddCategorys();

        text = text .replaceAll("(\\|{2})|(\\&{2})|(\\')|(\\\")|(\\*)","");

        String [] queryElements = text.toLowerCase().split("\\s+");

        Type [] ElementTypes = new Type[queryElements.length];
        String [] typeFilter = new String[Type.values().length];

        for (int i = 0; i < Type.values().length; i++) {
            typeFilter[i]="";
        }


        for (int i = 0; i < queryElements.length; i++) {

            ElementTypes[i]=Type.NONE;

            if(GenreList.contains(queryElements[i])){
                ElementTypes[i]=Type.GENRE;
            }
            if(ProfessionList.contains(queryElements[i])){
                ElementTypes[i]=Type.PROFESSION;
            }
            if(queryElements[i].equals("serie")|| queryElements[i].equals("series")){
                ElementTypes[i]=Type.SERIE;
            }
            if(queryElements[i].equals("movie")||queryElements[i].equals("movies")){
                ElementTypes[i]=Type.MOVIE;
            }
            if(queryElements[i].equals("person") || queryElements[i].equals("persons")){
                ElementTypes[i]=Type.PERSON;
            }
        }

        String queryName = "";

        for (int i = 0; i < queryElements.length; i++) {
            if(ElementTypes[i] == Type.NONE){
                queryName +=  queryElements[i] +" ";
            }else{
                Integer type;
                if(ElementTypes[i] == Type.SERIE){
                    type = Type.MOVIE.getValue();
                }else{
                    type = ElementTypes[i].getValue();
                }
                String temp = queryElements[i].substring(0, 1).toUpperCase() + queryElements[i].substring(1);
                typeFilter[type] += String.format(this.TypeFilters[ElementTypes[i].getValue()],temp) ;
            }
        }

        for (int i = 0; i < Type.values().length; i++) {
            if(typeFilter[i].length()>0){
                typeFilter[i] = typeFilter[i].replaceFirst("(\\|{2})|(\\&{2})","");
                typeFilter[i] = "FILTER ( "+typeFilter[i]+" ).";
            }
        }

        if(queryName.length()>0){
            queryName = String.format("?Uri text:query ( '%s' ) .",queryName);
        }else{
            queryName = String.format("?Uri text:query ( '%s' ) .","*");
        }

        String result = StrUtils.strjoinNL(typeFilter);

        return AddInfo(this.PerformQuery(String.format(StringMatchingQuery, queryName, result)+" LIMIT "+limit+" OFFSET "+offset));
    }

    private Boolean AddCategorys(){
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
            return true;
        }
        return false;
    }

    private JSONArray AddInfo(JSONArray json){
        for(int i=0; i<json.length(); i++){
            JSONObject current = (JSONObject)json.get(i);
            String GenreUri = current.get("typeUri").toString();
            String[] parts = GenreUri.split("#");
            current.put("type",parts[1]);
        }

        return json;
    }

}
