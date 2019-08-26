package locateme;
import org.json.*;
import java.awt.Desktop;
import java.io.*;
import java.net.*;

public class LocationService {

	public static void main(String[] args) throws Exception {
		String ip = getIp();
		String location = "";
        JSONObject jsonObj = null;
        String maplink =""; 
        
        location = getLocationData(ip);
        jsonObj = new JSONObject(location);
        maplink = "https://www.google.com/maps/?q="
        +jsonObj.getString("loc");

        openURL(maplink);
    }
	
	//get public IP address using aws API
	public static String getIp() throws Exception {
		
        URL deviceip = new URL("http://checkip.amazonaws.com");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    deviceip.openStream()));
            String ip = br.readLine();
            return ip;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	//open google map url using default browser 
	public static void openURL(String urlString) {
		if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(urlString));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + urlString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	// get location data from ipinfo.io API
    public static String getLocationData(String ip) {
        URL url;
        String response = "";
        
        if (!ip.equals("")) 
        {
        	ip = "/" + ip ;
        }
        
        try {
            //construct URL
            String link="https://ipinfo.io"+ip+"/json";
            url = new URL(link);
            URLConnection connection = url.openConnection();

            // open the input stream and put it into buffered reader
            BufferedReader br = new BufferedReader(
                               new InputStreamReader(connection.getInputStream()));

            String input; 
            while ((input = br.readLine()) != null) {
                    response = response + input;
            }
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return response;
    }
}

