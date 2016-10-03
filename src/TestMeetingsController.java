import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestMeetingsController {
	
	OpenStackMeetingsController meetingsController = new OpenStackMeetingsController();
	MeetingsService mockMeeting = null;
	
	@Before
	public void setUp1() {		
		mockMeeting = mock(MeetingsService.class);		
		meetingsController.seMeetingsService(mockMeeting);
	}
	
	@Test
	public void testHelloWorld() {
		String reply = meetingsController.helloWorld();
		assertEquals("Hello World!", reply);
	}
	@Test
	public void testContentType() {
		String reply = meetingsController.getContentType("http://localhost:8080/assignment2/?User-Agent=firefox");
		assertEquals("User Agent: firefox", reply);
	}
	@Test
	public void testParams() {
		String reply = meetingsController.missingYear("http://localhost:8080/assignment2/openstackmeetings/?project=LAOS");
		assertEquals("Required parameter \"year\" missing", reply);
	}
	@Test
	public void testParams2() {
		String reply = meetingsController.missingProject("http://localhost:8080/assignment2/openstackmeetings/?year=1992");
		assertEquals("Required parameter \"project\" missing", reply);
	}
	@Test
	public void testParams3() {
		String reply = meetingsController.fullParams("http://localhost:8080/assignment2/openstackmeetings/?project=heat&year=abce");
		assertEquals("Required parameter \"year\" is not a valid integer", reply);
	}
	@Test
	public void testGetComposedEmail() {
		when(mockMeeting.composeEmail()).thenReturn("test email");
		String composedEmail = meetingsController.getComposedEmail();
		assertEquals("test email", composedEmail);
		verify(mockMeeting).composeEmail();
	}
	@Test
	public void testWithGhostObject() {
		when(mockMeeting.composeEmail()).thenReturn("test email");		
		String reply = meetingsController.getWithGhostEditor();
		assertEquals("test email.", reply);		
	}	
}
