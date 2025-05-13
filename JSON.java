import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;

import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Accessing live data and parsing a JSON file. 
 *
 * Inspiration: https://medium.com/swlh/getting-json-data-from-a-restful-api-using-java-b327aafb3751
 * Data: https://www.weather.gov/documentation/services-web-api
 * 
 * @author (Mr. J. L. Oswald )
 * @version (2025.05.10.01)
 */
public abstract class JSON
{
    // @44.4061172,-69.9877673
    public static void main(String args[]) {
        try {
            URL pointsURL = new URL("https://api.weather.gov/points/44.4061172,-69.9877673");  
            JSONObject json = jsonEndpointToJSONObject( pointsURL );
            JSONObject properties = (JSONObject) json.get("properties");
            String forecastURL = (String) properties.get("forecastHourly");
            json = jsonEndpointToJSONObject( new URL(forecastURL) );
            
            
        } catch (Exception e) {
            System.out.println("something bad happened");
            e.printStackTrace();
        } finally {
            System.out.println("everything good happened");
        }
        //Getting the response code           
    }
    
    public static JSONObject jsonEndpointToJSONObject( URL jsonEndpoint ) throws java.io.IOException, org.json.simple.parser.ParseException {
        return stringToJSONObject( jsonEndpointToString( jsonEndpoint ) );
    }
    
    public static JSONObject stringToJSONObject( String jsonString ) throws org.json.simple.parser.ParseException {
        //Using the JSON simple library parse the string into a json object
        JSONParser parse = new JSONParser();
        return (JSONObject) parse.parse(jsonString);
    }
    
    public static String jsonEndpointToString(URL jsonEndpoint) throws java.io.IOException {
        String jsonString = "";
        HttpURLConnection pointsConnection = getConnection( jsonEndpoint );             
        Scanner scanner = new Scanner(jsonEndpoint.openStream());
        
        //Write all the JSON data into a string using a scanner
        while (scanner.hasNext()) {
            jsonString += scanner.nextLine();
        }
        //Close the scanner
        scanner.close();            
            
        return jsonString;
    }
    
    public static HttpURLConnection getConnection(URL endpoint) throws java.io.IOException {
        HttpURLConnection connection = null;
        int responseCode = 0;

        connection = (HttpURLConnection) endpoint.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        responseCode = connection.getResponseCode();
        if( responseCode != 200 ) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        }
          
        return connection;
    }
}
