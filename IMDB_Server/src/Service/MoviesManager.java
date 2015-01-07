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
    private String RdfNamespace="<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    private String RdfsNamespace="<http://www.w3.org/2000/01/rdf-schema#>";

    String StandardSelect = "DISTINCT ?id ?Name ?typeUri ?Poster ?duration ?launched ?description ?classification ?start ?end ?seasons ";

    String StandardWhere = "?MovieUri ns:hasMediaName ?Name; ns:hasMediaId ?id; rdf:type ?typeUri; ns:hasMediaPoster ?Poster; ns:hasMediaDuration ?duration; ns:hasMediaDescription ?description; ns:hasMediaClassification ?classification . ?typeUri rdfs:subClassOf ns:Media. OPTIONAL { ?MovieUri ns:hasMediaLaunchDate ?launched } . OPTIONAL { ?MovieUri ns:hasSerieEnd ?end } . OPTIONAL { ?MovieUri ns:hasSerieStart ?start } . OPTIONAL { ?MovieUri ns:hasSerieSeason ?seasons } .";
    
    String queryGetAll =
                    "PREFIX ns: "+GlobalNamespace+" "+
                    "PREFIX rdf: "+RdfNamespace+" "+
                    "PREFIX rdfs: "+RdfsNamespace+" "+
                    "SELECT "+ StandardSelect +
                    "WHERE { "+ StandardWhere +" }";
    String queryGetById =
                    "PREFIX ns: "+GlobalNamespace+" "+
                    "PREFIX rdf: "+RdfNamespace+" "+
                    "PREFIX rdfs: "+RdfsNamespace+" "+
                    "SELECT "+ StandardSelect +
                    "WHERE { " + StandardWhere + " FILTER regex(?id, \"%s\") }";
    String queryGetMostRecent =
                    "PREFIX ns: "+GlobalNamespace+" "+
                    "PREFIX rdf: "+RdfNamespace+" "+
                    "PREFIX rdfs: "+RdfsNamespace+" "+
                    "SELECT "+ StandardSelect +
                    "WHERE { "+ StandardWhere +" } "+
                    "ORDER BY ?launched "+
                    "LIMIT 6";
    String querySelectGenre=
                    "PREFIX ns: "+GlobalNamespace+" "+
                    "PREFIX rdf: "+RdfNamespace+" "+
                    "PREFIX rdfs: "+RdfsNamespace+" "+
                    "SELECT "+ StandardSelect +
                    "WHERE {%s} ";
    String queryWhereGenre=
            "{ ?MovieUri ns:hasGenres ns:%s . "+ StandardWhere +" }";

    String queryGetKnownForByPersonId =
            "PREFIX ns: "+GlobalNamespace+" "+
            "PREFIX rdf: "+RdfNamespace+" "+
            "PREFIX rdfs: "+RdfsNamespace+" "+
            "SELECT "+ StandardSelect +
            "WHERE { ?PersonUri ns:hasPersonId ?PersonId . ?PersonUri ns:isKnownFor ?MovieUri . " + StandardWhere + " FILTER regex (?PersonId, \"%s\") . }";



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

    public JSONArray GetKnownById(String id){
        return this.PerformQuery(String.format(queryGetKnownForByPersonId, id));
    }
}
