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
                    "SELECT ?id ?Name ?Poster ?duration ?launched ?description ?classification " +
                    "WHERE { ?b ns:hasMovieName ?Name; ns:hasMovieId ?id; ns:hasMoviePoster ?Poster; " +
                               "ns:hasMovieDuration ?duration; ns:hasMovieLaunchDate ?launched; ns:hasMovieDescription ?description; ns:hasMovieClassification ?classification .}";
    String queryGetById =
                    "PREFIX nsowl: "+GlobalNamespace+" "+
                    "SELECT ?id ?Name ?Poster ?duration ?launched ?description ?classification " +
                    "WHERE { "+Namespace+" a nsowl:Movie;  nsowl:hasMovieName ?Name; nsowl:hasMovieId ?id; nsowl:hasMoviePoster ?Poster; "+
                            "nsowl:hasMovieDuration ?duration; nsowl:hasMovieLaunchDate ?launched; nsowl:hasMovieDescription ?description; nsowl:hasMovieClassification ?classification .}";

    String queryGetMostRecent =
            "PREFIX ns: "+GlobalNamespace+" "+
                    "SELECT ?id ?Name ?Poster ?duration ?launched ?description ?classification " +
                    "WHERE { ?b ns:hasMovieName ?Name; ns:hasMovieId ?id; ns:hasMoviePoster ?Poster; " +
                    "ns:hasMovieDuration ?duration; ns:hasMovieLaunchDate ?launched; ns:hasMovieDescription ?description; ns:hasMovieClassification ?classification .} "+
                    "ORDER BY ?launched "+
                    "LIMIT 6";
    String querySelectGenre=
            "PREFIX ns: "+GlobalNamespace+" "+
                    "SELECT DISTINCT ?id ?Name ?Poster ?duration ?launched ?description ?classification " +
                    "WHERE {%s} ";
    String queryWhereGenre=
            "{ ?b ns:hasGenres ns:%s ; ns:hasMovieName ?Name; ns:hasMovieId ?id; ns:hasMoviePoster ?Poster; ns:hasMovieDuration ?duration; ns:hasMovieLaunchDate ?launched; ns:hasMovieDescription ?description; ns:hasMovieClassification ?classification .}"

            ;


    public JSONArray GetAll(int offset,int limit){
        return this.PerformQuery(queryGetAll+" LIMIT "+limit+" OFFSET "+offset);
    }

    public JSONArray GetAllByGenre(String genres,int offset,int limit){

        String query = querySelectGenre;
        String [] aGenres = genres.split("\\|");

        String temp="";

        for(int i=0;i<aGenres.length;i++){
            temp += String.format(queryWhereGenre,aGenres[i]);
            if(!(i==(aGenres.length-1))){
                temp +=" UNION ";
            }
        }

        query = String.format(query,temp);

        return this.PerformQuery(query+" LIMIT "+limit+" OFFSET "+offset);
    }


    public JSONArray GetAll(){
        return this.PerformQuery(queryGetAll);
    }

    public JSONArray GetMostRecent(){
        return this.PerformQuery(queryGetMostRecent);
    }

    public JSONArray Get(String id){
        return this.PerformQuery(String.format(queryGetById, id));
    }

}
