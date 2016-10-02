
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

	
@SuppressWarnings("serial")
public class OpenStackMeetingsController extends HttpServlet {
	ArrayList <String> urlsVisited = new ArrayList<String>();
	ArrayList <String> urlsData = new ArrayList<String>();
	boolean valid = false;
	boolean skip = false;
	final static String URL = "http://eavesdrop.openstack.org/meetings/";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String url = request.getRequestURL().toString();
		
		if (session.isNew()){
			String encodedURL = response.encodeURL(url);
			
			if( ! sessionStarted(request, response) ){
				session.invalidate();
				//urlsVisited.clear();
				writeResponse(request, response, "no session", encodedURL);
			}
			else{
				writeResponse(request, response, "in session", encodedURL);
			}
		}			
		else {
			String encodedURL = response.encodeURL(url);
			if(! sessionEnded(request, response))
				writeResponse(request, response, "in session", encodedURL);
			else{
				writeResponse(request, response, "session ended", encodedURL);
				session.invalidate();
				urlsVisited.clear();
			}
				
		}
	}
	private void writeResponse(HttpServletRequest request, HttpServletResponse response, String you, String url) throws IOException {
		
		print(response, "<html>");
		print(response, you + "<br>");
		
		valid = false;
		if(request.getQueryString() != null && getYear(request, response) != null && getProject(request, response) != null && getYear(request, response) > 0 && getProject(request, response) != null){
			if(! urlsVisited.contains(url + "?" + request.getQueryString()) ){
				skip = false;
				urlsVisited.add(url + "?" + request.getQueryString());
			}
			else
				skip = true;
			valid = true;
		}
		else if(sessionEnded(request, response) || sessionStarted(request,response))
			urlsVisited.add(url + "?" + request.getQueryString());
		else if (request.getQueryString() == null)
			print(response, "null parameters is null" + "<br>");
		else if (getYear(request, response) == null || getProject(request, response) == null){}
		else if (getYear(request, response) <= 0)
			print(response, "the year entry is illegal " + "<br>");
		else if (getProject(request, response) == null)
			print(response, "the project " + getProject(request, response) + " is not a valid one"+ "<br>");
		else
			print(response, "the link is broken" + "<br>");
		if(valid){
			try{
				Document doc = Jsoup.connect(URL+getProject(request, response) + "/" + getYear(request, response)).get();
				for (Element table : doc.select("table")) {
			        for (Element row : table.select("tr")) {
			        	
			        	if(row.select("td").text().indexOf(' ') > 0){
			        		if(row.text().substring(0,1).equals("P"))
				        		continue;
			        		String[] parts = row.select("td").text().split(" ");
			        		if(!skip)
			        			urlsData.add("<pre class=\"tab\">" + parts[0] + "    " + parts[1] + "    " + parts[2] + "</pre>");
			        	}
			        	else
			        		print(response, "" + row.select("td").text());
			            Elements tds = row.select("td");
			            if (tds.size() > 6) {
			                print(response, tds.get(0).text() + ":" + tds.get(1).text() + "<br>");
			            }
			        }
			    }
			}
		    catch(org.jsoup.HttpStatusException e){
		    	print(response, e.getMessage() + "<br>" + URL+getProject(request, response) + "<br");
		    }
		}
		
		print(response, "<br>visited URLS: <br>");
		printArray(response, urlsVisited, true);
		
		print(response, "URL data: <br>");
		printArray(response, urlsData, false);
		urlsData.clear();
	
		print(response, "</html>");	
		return;
	}
	/* return -1 when input is not valid	*/
	private Integer getYear (HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(request.getParameter("year") == null)
			return null;
		try {
			int x = Integer.parseInt(request.getParameter("year"));
			if(x >1900 && x <2018)
				return x;
			else throw new NumberFormatException("year " + x + " is not valid"); 
		}
		catch (NumberFormatException e) {
			return -1;
		}
	}
	private String getProject (HttpServletRequest request, HttpServletResponse response) throws IOException {
		return request.getParameter("project");
	}
	private boolean sessionStarted (HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("session") != null && request.getParameter("session").equals("start"))
			return true;
		return false;
	}
	private boolean sessionEnded (HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("session") != null && request.getParameter("session").equals("end"))
			return true;
		return false;
	}
	private void print(HttpServletResponse response, String str) throws IOException{
		response.getWriter().print(str);
	}
	private void printArray(HttpServletResponse response, ArrayList<String> str, boolean link) throws IOException{
		
		for (String x : str)
			if(link)
				print(response, "<a href=\"" + x + "\">" + x + "</a><br>");
			else
				print(response, x);
		
		print(response, "<br>");
	}
}