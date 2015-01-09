package Service;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.util.QueryExecUtils;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDFS;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.text.EntityDefinition;
import org.apache.jena.query.text.TextDatasetFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.json.JSONArray;
import org.apache.jena.query.text.TextQuery;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Connection {

    String configFile = "connection.txt";
    String source, TDBdirectory;

    protected static Dataset dataset;

    public Connection(){
        TextQuery.init();
        String Location = getClass().getSuperclass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:","").replace("Connection.class","");
        this.configFile = Location+configFile;
        if (!this.readConfig()) {
            return;
        }

        if(dataset == null) {
            Dataset ds = DatasetFactory.assemble(Location + "assembler.ttl", "http://localhost/jena_example/#text_dataset");
            loadData(ds, source);

            dataset = ds;
        }

    }

    protected JSONArray PerformQuery(String queryString){
        TextQuery.init();
        System.out.println(queryString);

        dataset.begin(ReadWrite.READ);

        Model model = dataset.getDefaultModel();
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, dataset);

        ResultSet results = qe.execSelect();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(stream, results);

        qe.close();
        dataset.end();

        String JsonResult = stream.toString();

        JSONObject json = new JSONObject(JsonResult);
        JSONArray elements= (JSONArray) ((JSONObject) json.get("results")).get("bindings");

        JsonFilter(elements);

        return elements;
    }

    private Object JsonFilter(Object json){

        if(json instanceof JSONArray){
            for(int i=0; i<((JSONArray)json).length(); i++){
                JsonFilter(((JSONArray)json).get(i));
            }
        }else if(json instanceof JSONObject){
            JSONObject element = (JSONObject)json;
            if(element.has("type") && element.has("value")){
                return element.get("value");
            }

            Iterator<?> keys = element.keys();

            while( keys.hasNext() ) {
                String key = (String) keys.next();
                element.put(key, JsonFilter(element.get(key)));
            }

        }
        return null;
    }

    private boolean readConfig() {
        BufferedReader br = null;
        ArrayList<String> info = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(this.configFile));
            String line;
            while ((line = br.readLine()) != null) {
                info.add(line);
            }
            br.close();
        } catch (Exception e) {
            return false;
        }

        this.source = info.get(0);
        this.TDBdirectory = info.get(1);

        return true;
    }

    public static void loadData(Dataset dataset, String file){


        dataset.begin(ReadWrite.WRITE) ;
        try {
            Model m = dataset.getDefaultModel() ;
            RDFDataMgr.read(m, file) ;
            dataset.commit() ;
        } finally { dataset.end() ; }

    }

}
