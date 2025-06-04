package de.whs.wi.friends_and_places;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Use the "test" profile for testing
class FriendsAndPlacesApplicationTests {

	@Test
	void contextLoads() {
		// This test simply checks if the application context loads successfully.
	}

}
