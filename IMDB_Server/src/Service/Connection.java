package Service;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Connection {

    String configFile = "connection.txt";
    String source, TDBdirectory;

    protected Dataset dataset;



    public Connection(){

        String Location = getClass().getSuperclass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:","").replace("Connection.class","");
        this.configFile = Location+configFile;
        if (!this.readConfig()) {
            return;
        }

        if(!(new File(TDBdirectory).exists())) {

            dataset = TDBFactory.createDataset(TDBdirectory);
            dataset.begin(ReadWrite.WRITE);
            Model tdb = dataset.getDefaultModel();

            FileManager.get().readModel(tdb, source);
            dataset.commit();
            dataset.end();
        }else{
            dataset = TDBFactory.createDataset(TDBdirectory);
        }
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

    protected JSONArray PerformQuery(String queryString){
        dataset.begin(ReadWrite.READ);
        System.out.println(queryString);
        Model model = dataset.getDefaultModel();
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(stream, results);

        qe.close();
        dataset.end();

        String JsonResult = stream.toString();
        //System.out.println(JsonResult);
        JSONObject json = new JSONObject(JsonResult);
        JSONArray elements= (JSONArray) ((JSONObject) json.get("results")).get("bindings");

        /*
        for(int i=0; i<elements.length(); i++){
            JSONObject element = (JSONObject)elements.get(i);
            Iterator<?> keys = element.keys();

            while( keys.hasNext() ) {
                String key = (String) keys.next();
                JSONObject attr = (JSONObject)element.get(key);
                element.put(key, attr.get("value"));

            }

        }
        */
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
}
