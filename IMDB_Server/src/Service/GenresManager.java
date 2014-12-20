package Service;

import org.json.JSONArray;

/**
 * Created by Joao on 19/12/2014.
 */
public class GenresManager extends Connection{
    private String GlobalNamespace="<http://www.movierecomendation.pt/ontology/movierecomendation.owl#>";
    private String Namespace ="<http://www.movierecomendation.pt/Movie/>";
    private String owl = "<http://www.w3.org/2002/07/owl#>";
    String queryGetGenreByMovieId =
            "PREFIX nsowl:"+GlobalNamespace+" "+
            "PREFIX ns:"+Namespace+" "+
            "SELECT ?genre WHERE { ns:%s nsowl:hasGenres ?genre .}";
    String queryGetAll =
            "PREFIX owl: "+owl+" "+
            "PREFIX nsowl: "+GlobalNamespace+" "+
            "SELECT ?genres "+
            "WHERE { nsowl:Genre owl:equivalentClass ?genres.}";

    public JSONArray GetAll(){
        return this.PerformQuery(queryGetAll);
    }

    public JSONArray GetByMovie(String id){
        return this.PerformQuery(String.format(queryGetGenreByMovieId, id));
    }


}
