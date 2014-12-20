package Service;

import org.json.JSONArray;

/**
 PREFIX nsowl:<http://www.movierecomendation.pt/ontology/movierecomendation.owl#>
 PREFIX ns:<http://www.movierecomendation.pt/Studio/>
 PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
 SELECT ?id ?rid ?name
 WHERE {?rid rdf:type nsowl:Studio; nsowl:hasStudioId ?id ; nsowl:hasStudioName ?name .}
 */
public class StudiosManager extends Connection{

    private String GlobalNamespace="<http://www.movierecomendation.pt/ontology/movierecomendation.owl#>";
    private String Namespace ="<http://www.movierecomendation.pt/Studio/>";
    private String MovieNamespace ="<http://www.movierecomendation.pt/Movie/>";
    private String RDFNamespace ="<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";

    private String queryGetAll =
            "PREFIX rdf:"+RDFNamespace+" "+
                    "PREFIX nsowl:"+GlobalNamespace+" "+
                    "SELECT ?id ?rid ?name "+
                    "WHERE {?rid rdf:type nsowl:Studio; nsowl:hasStudioId ?id ; nsowl:hasStudioName ?name .}";

    String queryGetById =
            "PREFIX ns: "+Namespace+" "+
                    "PREFIX nsowl: "+GlobalNamespace+" "+
                    "SELECT ?id ?Name " +
                    "WHERE { ns:%s a nsowl:Sudio;  nsowl:hasStudioId ?id ; nsowl:hasStudioName ?name .}";
    String queryGetStudioByMovieId =
            "PREFIX nsowl:"+GlobalNamespace+" "+
                    "PREFIX ns:"+MovieNamespace+" "+
                    "SELECT ?id ?name "+
                    "WHERE {{ ns:%s nsowl:hasStudio ?rid . ?rid nsowl:hasStudioId ?id ; nsowl:hasStudioName ?name .} }";



    public JSONArray GetAll(){
        return this.PerformQuery(queryGetAll);
    }

    public JSONArray Get(String id){return this.PerformQuery(String.format(queryGetById, id));}

    public JSONArray GetbyMovie(String id){return this.PerformQuery(String.format(queryGetStudioByMovieId, id));}

}
