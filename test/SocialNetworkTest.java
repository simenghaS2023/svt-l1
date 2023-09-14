import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SocialNetworkTest {

	// these are some example tests: you can merge them with your own tests from A0 
    
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test 
	public void canJoinSocialNetwork() {
		SocialNetwork sn = new SocialNetwork();
		Account me = sn.join("Hakan");
		assertEquals("Hakan", me.getUserName());
	}
	
	@Test 
	public void canListSingleMemberOfSocialNetworkAfterOnePersonJoiningAndSizeOfNetworkEqualsOne() {
		SocialNetwork sn = new SocialNetwork();
		sn.join("Hakan");
		Set<String> members = sn.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}
	
	@Test 
	public void twoPeopleCanJoinSocialNetworkAndSizeOfNetworkEqualsTwo() {
		SocialNetwork sn = new SocialNetwork();
		sn.join("Hakan");
		sn.join("Cecile");
		Set<String> members = sn.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}
	
	@Test 
	public void sendAndAcceptFriendRequestToBecomeFriends() {
		// test sending friend request
	    sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}
	

}
