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
public class EmailController {

	private EditorService englishEditorService;
	private EditorService spanishEditorService;

	public EmailController() {
		
	}
	
	public EmailController(EditorService editorService) {
		this.englishEditorService = editorService;
	}
	
	@ResponseBody
	@RequestMapping("/headers") 	
	public String getAllHeaders(@RequestHeader HttpHeaders headers)
	{
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
	@RequestMapping("/userAgent") 	
	public String getContentType(@RequestHeader("User-Agent") String userAgent)
	{
		return "User Agent:" + userAgent;
	}	
	
	@ResponseBody
    @RequestMapping(value = "/calculator", params = {"values", "operator"}, method=RequestMethod.GET)
    public String calculator(@RequestParam("values") String values, @RequestParam("operator") String operator)
    {
		String ret = "";
		if (values != null && operator != null) {
			String vals [] = values.split(",");
			long result = Long.parseLong(vals[0]);
			for(int i=1; i<vals.length; i++) {
				switch (operator) {
					case "add":
						result = result + Integer.parseInt(vals[i]);
						break;
					case "subtract":
						result = result - Integer.parseInt(vals[i]);
						break;
					case "multiply":
						result = result * Integer.parseInt(vals[i]);
						break;
					case "divide":
						result = result / Integer.parseInt(vals[i]);
						break;
				}
			}
			ret = String.valueOf(result);
		}
		else {
			return "Required parameters, values and operator, missing.";
		}
		return "Result is:" + ret;
    }

	@ResponseBody
    @RequestMapping(value = "/", params = {"action"}, method=RequestMethod.GET)
    public String getGreeting(@RequestParam("action") String action)
    {
		String ret = "";
		if (action.equalsIgnoreCase("compose")) {
			ret = "Invoking editor service: " + englishEditorService.composeEmail();
		}
		return ret;
    }
	
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings")
    public String bar()
    {
		return "Welcome to OpenStack meeting statistics calculation page. Please provide project and year as query parameters.";
    }
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings", params = {"year"}, method=RequestMethod.GET)
    public String foo1(@RequestParam("year") String year, @RequestParam("project") String project)
    {
		return "Required parameter <project> missing";
    }
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings", params = {"project"}, method=RequestMethod.GET)
    public String foo2(@RequestParam("year") String year, @RequestParam("project") String project)
    {
		return "Required parameter <year> missing";
    }
	
	@ResponseBody
    @RequestMapping(value = "/openstackmeetings", params = {"project", "year"}, method=RequestMethod.GET)
    public String foo(@RequestParam("year") String year, @RequestParam("project") String project)
    {
		if( year.matches("^-?\\d+$") )
			return "1- Year = " + year + " - Project = " + project.toLowerCase();
		else
			return "2- Year = " + year + " - Project = " + project.toLowerCase();
    }
}
