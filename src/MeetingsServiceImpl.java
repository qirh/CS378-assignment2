import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.util.StringUtils;

public class MeetingsServiceImpl implements MeetingsService {

	String name = "Meeting";
	URL eavesdropURL = null;
	private String baseURL = "http://eavesdrop.openstack.org";
	private String extension = "/meetings/";
	
	public MeetingsServiceImpl() {
		try {
			eavesdropURL = new URL(baseURL);
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public String testRun() {
		return "runs well !";
	}
	public String getName() {
		return this.name;
	}
	public String getResponseFromEavesDrop(String projectName, String year) {
		String retVal = "";		
		baseURL += extension + projectName + "/" + year + "/";
		String readData = "";
		try {			
			eavesdropURL = new URL(baseURL);
			URLConnection connection = eavesdropURL.openConnection();
			readData = readDataFromEavesdrop(connection);
			retVal = parseOutput(readData);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	protected String readDataFromEavesdrop(URLConnection connection) {
		String retVal = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				retVal += inputLine;
			}
			in.close();
		} 
		catch(java.io.FileNotFoundException e){
			return "";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	//reads how many directories are in a page
	protected String parseOutput(String inputString) {

		return ""+ StringUtils.countOccurrencesOf(inputString, "[TXT]");
	}
	public void thisIsVoidFunction() {
		// TODO Auto-generated method stub	
	}
}