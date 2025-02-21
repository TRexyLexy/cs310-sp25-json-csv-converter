package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.io.StringWriter;
import java.util.Arrays;



public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try {
        
            // INSERT YOUR CODE HERE
            
           CSVReader reader = new CSVReader(new StringReader(csvString));
           List<String[]> lines = reader.readAll();

           JsonObject obj = new JsonObject();
           JsonArray prodNums = new JsonArray();
           JsonArray colHeadings = new JsonArray();
           JsonArray data = new JsonArray();
           
           String[] headers = lines.get(0);
           colHeadings.addAll(Arrays.asList(headers));

           
           for(int i = 1; i < lines.size(); i++){
               String[] values = lines.get(i);
               prodNums.add(values[0]);
                           
               JsonArray rowData = new JsonArray();   
               for(int j = 1; j < values.length; j++){
                   String value = values[j];
                   
                 try{
                     rowData.add(Integer.parseInt(value));
                     
                 }catch (NumberFormatException e){
                     rowData.add(value);
                 }
               }

               data.add(rowData);
            }
            obj.put("ProdNums", prodNums);
            obj.put("ColHeadings", colHeadings);
            obj.put("Data", data);

            return obj.toJson();
            
        }catch (Exception e) {
            e.printStackTrace();
        }

        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try{
            
            // INSERT YOUR CODE HERE            
            JsonObject obj = (JsonObject) Jsoner.deserialize(jsonString);
            JsonArray prodNums = (JsonArray) obj.get("ProdNums");
            JsonArray colHeadings = (JsonArray) obj.get("ColHeadings");
            JsonArray data = (JsonArray) obj.get("Data");
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");

            String[] headings = new String[colHeadings.size()];
            for(int i = 0; i < colHeadings.size(); i++){
                headings[i] = colHeadings.getString(i).trim();
            }
            csvWriter.writeNext(headings);
            
            for(int i = 0; i < data.size(); i++){
                JsonArray rowData = (JsonArray) data.get(i);
                String[] csvData = new String[rowData.size() + 1];
                
                csvData[0] = prodNums.getString(i).trim();
                
                for(int j = 0; j < rowData.size(); j++){
                    Object value = rowData.get(j);
                    csvData[j + 1] = rowData.get(j) != null ? value.toString() : "";
                    
                }
                    csvWriter.writeNext(csvData);
                               
            }
            return writer.toString();
                    
    
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
