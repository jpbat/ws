package Service;


import com.google.gson.Gson;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MoviesManager extends Connection{
    private String GlobalNamespace="<http://www.movierecomendation.pt/ontology/movierecomendation.owl#>";
    private String Namespace ="<http://www.movierecomendation.pt/Movie/%s>";

    String queryGetAll =
                    "PREFIX ns: "+GlobalNamespace+" "+
                    "SELECT ?id ?Name ?Poster ?score ?duration ?launched ?description ?classification " +
                    "WHERE { ?b ns:hasMovieName ?Name; ns:hasMovieId ?id; ns:hasMoviePoster ?Poster; " +
                               "ns:hasMovieDuration ?duration; ns:hasMovieLaunchDate ?launched; ns:hasMovieDescription ?description; ns:hasMovieClassification ?classification .}";
    String queryGetById =
                    "PREFIX nsowl: "+GlobalNamespace+" "+
                    "SELECT ?id ?Name ?Poster ?score ?duration ?launched ?description ?classification " +
                    "WHERE { "+Namespace+" a nsowl:Movie;  nsowl:hasMovieName ?Name; nsowl:hasMovieId ?id; nsowl:hasMoviePoster ?Poster; "+
                            "nsowl:hasMovieDuration ?duration; nsowl:hasMovieLaunchDate ?launched; nsowl:hasMovieDescription ?description; nsowl:hasMovieClassification ?classification .}";

    public JSONArray GetAll(){
        return this.PerformQuery(queryGetAll);
    }

    public JSONArray Get(String id){
        return this.PerformQuery(String.format(queryGetById, id));
    }

}
