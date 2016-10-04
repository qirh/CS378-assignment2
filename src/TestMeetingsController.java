import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TestMeetingsController {
	
	OpenStackMeetingsController meetingsController = new OpenStackMeetingsController();
	MeetingsService mockMeeting = null;
	
	@Before
	public void setUp1() {		
		mockMeeting = mock(MeetingsService.class);		
		meetingsController.setMeetingsService(mockMeeting);
	}
	@Test
	public void testHelloWorld() {
		String reply = meetingsController.helloWorld();
		assertEquals("Hello World!", reply);
	}
	@Test
	public void testWelcome() {
		String reply = meetingsController.welcome();
		assertEquals("Welcome to OpenStack meeting statistics calculation page. Please provide project and year as query parameters.", reply);
	}
	@Test
	public void testContentType() {
		String reply = meetingsController.getContentType("firefox");
		assertEquals("User Agent: firefox", reply);
	}
	@Test
	public void testParams() {
		String reply = meetingsController.missingYear("project=LAOS");
		assertEquals("Required parameter \"year\" missing", reply);
	}
	@Test
	public void testParams2() {
		String reply = meetingsController.missingProject("year=1992");
		assertEquals("Required parameter \"project\" missing", reply);
	}
	@Test
	public void testParams3() {
		String reply = meetingsController.fullParams("heat", "abce");
		assertEquals("Required parameter \"year\" is not a valid integer", reply);
	}
	@Test
	public void testParams4() {
		String reply = meetingsController.fullParams("heat", "2014");
		String reply2 = meetingsController.fullParams("hEat", "2014");
		String reply3 = meetingsController.fullParams("heAt", "2014");
		String reply4 = meetingsController.fullParams("hEAT", "2014");
		assertEquals(reply2, reply);
		assertEquals(reply2, reply4);
		assertEquals(reply3, reply4);
	}
	@Test
	public void testParams5() {
		String reply = meetingsController.fullParams("heat", "2014");
		assertEquals("Number of meeting files: 180", reply);
	}
}