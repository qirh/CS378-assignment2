import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OpenStackMeetingsController {
	
	private MeetingsService meetingsService;
	public OpenStackMeetingsController() {}
	public OpenStackMeetingsController(MeetingsService meetingsService) {
		this.meetingsService = meetingsService;
	}
	public String getWithGhostEditor() {
		meetingsService = new MeetingsServiceImpl();
        return meetingsService.testRun();		
	}
	
	@ResponseBody
    @RequestMapping(value = "/")
    public String helloWorld(){
        return "Hello World!";
    }
	@ResponseBody
	@RequestMapping(value = "/", params = {"User-Agent"}, method=RequestMethod.GET) 	
	public String getContentType(@RequestParam("User-Agent") String userAgent){
		return "User Agent: " + userAgent;
	}
	@ResponseBody
	@RequestMapping("/headers") 	
	public String getAllHeaders(@RequestHeader HttpHeaders headers){
		Set<String> keys = headers.keySet();
		String response = "";
		Iterator<String> i = keys.iterator();
		while(i.hasNext()) {
			String key = i.next();
			List<String> value = headers.get(key);
			response += key + " " + value;
		}
		return response;
	}
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings")
    public String welcome()
    {
		return "Welcome to OpenStack meeting statistics calculation page. Please provide project and year as query parameters.";
    }
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings", params = {"year"}, method=RequestMethod.GET)
    public String missingProject(@RequestParam("year") String year)
    {
		return "Required parameter \"project\" missing";
    }
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings", params = {"project"}, method=RequestMethod.GET)
    public String missingYear(@RequestParam("project") String project)
    {
		return "Required parameter \"year\" missing";
    }
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings", params = {"project", "year"}, method=RequestMethod.GET)
    public String fullParams(@RequestParam("project") String project, @RequestParam("year") String year)
    {
		if( year.matches("^-?\\d+$") ){
			String x =  new MeetingsServiceImpl().getResponseFromEavesDrop(project.toLowerCase());
			return "Number of meeting files: " + x;
		}
			
		else
			return "Required parameter \"year\" is not a valid integer";
    }
	public void setMeetingsService(MeetingsService meetingsService) {
		this.meetingsService = meetingsService;
	}
}
