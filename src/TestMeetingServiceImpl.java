import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class TestMeetingServiceImpl {

	MeetingsServiceImpl meeting = null;
	
	@Before
	public void setUp() {
		meeting = new MeetingsServiceImpl();
	}
	@Test
	public void testTestRun() {
		String reply = meeting.testRun();
		assertEquals("runs well !", reply);
	}
	@Test
	public void testGetName() {
		String reply = meeting.getName();
		assertEquals("Meeting", reply);
	}
	@Test
	public void testGetResponseFromEavesDrop() {
		try {
			String exampleString = "2";
			String result = meeting.getResponseFromEavesDrop("kosmos");
			assertEquals(exampleString, result);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testReadDataFromEavesdrop() {
		try {
			String exampleString = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">";
			exampleString += "<html>";
			exampleString += "</html>";
			
			URLConnection connection = mock(URLConnection.class); // Create mock dependency: mock()
			InputStream i = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));		
			when(connection.getInputStream()).thenReturn(i); // Setting up the expectations
			String result = meeting.readDataFromEavesdrop(connection);
			assertEquals(exampleString, result);			
			verify(connection).getInputStream();	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}