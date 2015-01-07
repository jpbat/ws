package Service;

import org.json.JSONArray;

/**
 * Created by Joao on 16/12/2014.
 */
public class PersonManager extends Connection{



    private String GlobalNamespace="<http://www.movierecomendation.pt/ontology/movierecomendation.owl#>";
    private String Namespace ="<http://www.movierecomendation.pt/Person/>";

    String queryGetAll =
            "PREFIX nsowl: "+GlobalNamespace+" "+
            "SELECT ?id ?Name ?Picture ?Birth ?BirthPlace ?MiniBio " +
            "WHERE { ?b nsowl:hasPersonId ?id ; nsowl:hasPersonName ?Name ;nsowl:hasPersonPicture ?Picture ;nsowl:hasPersonBirth ?Birth ;nsowl:hasPersonBirthPlace ?BirthPlace ;nsowl:hasPersonMiniBio ?MiniBio .}";
    String queryGetById =
            "PREFIX ns: "+Namespace+" "+
            "PREFIX nsowl: "+GlobalNamespace+" "+
            "SELECT DISTINCT ?id ?Name ?Picture ?Birth ?BirthPlace ?MiniBio " +
            "WHERE { ns:%s a ?type ;  nsowl:hasPersonId ?id ; nsowl:hasPersonName ?Name ;nsowl:hasPersonPicture ?Picture ;nsowl:hasPersonBirth ?Birth ;nsowl:hasPersonBirthPlace ?BirthPlace ;nsowl:hasPersonMiniBio ?MiniBio .}";
    String queryGetDirectorByMovieId =
            "PREFIX nsowl:"+GlobalNamespace+" "+
            "SELECT ?id ?name ?Picture ?Birth ?BirthPlace ?MiniBio "+
            "WHERE { ?MovieUri nsowl:hasMediaId ?MovieId . ?MovieUri nsowl:hasDirector ?rid . ?rid nsowl:hasPersonName ?name ; nsowl:hasPersonId ?id ; nsowl:hasPersonPicture ?Picture ;nsowl:hasPersonBirth ?Birth ;nsowl:hasPersonBirthPlace ?BirthPlace ;nsowl:hasPersonMiniBio ?MiniBio . FILTER regex(?MovieId, \"%s\") }";

    String queryGetActorByMovieId =
            "PREFIX nsowl:"+GlobalNamespace+" "+
            "SELECT ?id ?name ?Picture ?Birth ?BirthPlace ?MiniBio "+
            "WHERE { ?MovieUri nsowl:hasMediaId ?MovieId . ?MovieUri nsowl:hasActor ?rid . ?rid nsowl:hasPersonName ?name ; nsowl:hasPersonId ?id ; nsowl:hasPersonPicture ?Picture ;nsowl:hasPersonBirth ?Birth ;nsowl:hasPersonBirthPlace ?BirthPlace ;nsowl:hasPersonMiniBio ?MiniBio . FILTER regex(?MovieId, \"%s\") } ";

    public JSONArray GetAll(){
        return this.PerformQuery(queryGetAll);
    }

    public JSONArray GetAll(int offset,int limit){return this.PerformQuery(queryGetAll+" LIMIT "+limit+" OFFSET "+offset);}

    public JSONArray Get(String id){
        return this.PerformQuery(String.format(queryGetById, id));
    }

    public JSONArray GetDirectorByMovie(String id){return this.PerformQuery(String.format(queryGetDirectorByMovieId, id));}

    public JSONArray GetActorByMovie(String id){return this.PerformQuery(String.format(queryGetActorByMovieId, id));
    }
}