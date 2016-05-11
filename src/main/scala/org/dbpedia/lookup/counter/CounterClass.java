package org.dbpedia.lookup.counter;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Paolo Albano, Politecnico di Bari on 5/10/16.
 */
public class CounterClass {
    Map<String, Integer> count = new HashMap<String,Integer>();
    private final Logger logger = LoggerFactory.getLogger(CounterClass.class);

    public CounterClass(String wiki, String counter, String propertyCounter){

        try (BufferedReader br = new BufferedReader(new FileReader(wiki))) {
            String line;
            int c=0;
            while ((line = br.readLine()) != null) {
                String[] rdf = line.split(" ");
                if(rdf.length>3 && rdf[3].equals(".")) {
                    c++;
                    if(count.containsKey(rdf[2])){
                        count.put(rdf[2],count.get(rdf[2])+1);
                    }
                    else
                        count.put(rdf[2],1);
                }
            }
            logger.info(c+" rdf triple");
            br.close();
            //write rdf from hashmap
            try{
                FileWriter fw = new FileWriter(counter);
                for (Map.Entry<String, Integer> lineCount : count.entrySet()){
                    fw.write(lineCount.getKey()+" <"+propertyCounter+ "> \""+lineCount.getValue()+"\"^^<http://www.w3.org/2001/XMLSchema#integer> . \n");
                }
                fw.close();
                logger.info("Count file saved in "+counter);
            }
            catch(IOException e){
                logger.error(e.getMessage());
            }




        } catch (IOException e1) {
            //logger.info(e1.getMessage().toString());
            System.out.println(e1.toString());
        }
    }

}
