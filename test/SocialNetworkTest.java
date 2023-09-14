import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SocialNetworkTest {

	SocialNetwork sn;
	Account me, her, him;

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

	@Test 
	public void newUserHasCorrectName() {
		Account me = sn.join("Hakan");
		assertNotNull(me);
		assertEquals("Hakan", me.getUserName());
	}
	
	@Test 
	public void canListSingleMemberOfSocialNetworkAfterOnePersonJoiningAndSizeOfNetworkEqualsOne() {
		sn.join("Hakan");
		Collection<String> members = sn.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}

	@Test
	public void twoPeopleCanJoinSocialNetwork() {
		Account user1 = sn.join("Hakan");
		Account user2 = sn.join("Cecile");
		assertNotNull(user1);
		assertNotNull(user2);
	}

	@Test
	public void canSendFriendRequest() {
		Account me = sn.join("Hakan");
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(her.getIncomingRequests().contains("Hakan"));
	}


	@Test
	public void acceptingFriendsEstablishesMutualFriendship() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.sendFriendshipTo("Mary", john);
		sn.acceptFriendshipFrom("John", mary);
		assertTrue(mary.hasFriend("John"));
		assertTrue(john.hasFriend("Mary"));
	}

	@Test
	public void acceptAllRequestsEstablishesAllFriendships() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		Account paul = sn.join("Paul");
		sn.sendFriendshipTo("John", mary);
		sn.sendFriendshipTo("John", paul);
		sn.acceptAllFriendshipsTo(john);
		assertTrue(john.hasFriend("Mary"));
		assertTrue(john.hasFriend("Paul"));
	}

	@Test
	public void rejectFriendRemovesRequests() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.sendFriendshipTo("John", mary);
		sn.rejectFriendshipFrom("John", mary);
		assertFalse(mary.getIncomingRequests().contains("John"));
		assertFalse(john.getOutgoingRequests().contains("Mary"));
	}

	@Test
	public void rejectFriendDoesNotEstablishFriendship() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.sendFriendshipTo("John", mary);
		sn.rejectFriendshipFrom("John", mary);
		assertFalse(mary.hasFriend("John"));
		assertFalse(john.hasFriend("Mary"));
	}

	@Test
	public void rejectAllRequestsRemovesAllRequests() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		Account paul = sn.join("Paul");
		sn.sendFriendshipTo("John", mary);
		sn.sendFriendshipTo("John", paul);
		sn.rejectAllFriendshipsTo(john);
		assertFalse(john.getOutgoingRequests().contains("Mary"));
		assertFalse(john.getOutgoingRequests().contains("Paul"));
		assertFalse(mary.getIncomingRequests().contains("John"));
		assertFalse(paul.getIncomingRequests().contains("John"));
	}

	@Test
	public void rejectAllRequestsDoesNotEstablishAnyFriendships() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		Account paul = sn.join("Paul");
		sn.sendFriendshipTo("John", mary);
		sn.sendFriendshipTo("John", paul);
		sn.rejectAllFriendshipsTo(john);
		assertFalse(john.hasFriend("Mary"));
		assertFalse(john.hasFriend("Paul"));
		assertFalse(mary.hasFriend("John"));
		assertFalse(paul.hasFriend("John"));
	}

	@Test
	public void friendRequestAcceptedAutomaticallyIfAutoAcceptIsOn() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.autoAcceptFriendshipsTo(john);
		sn.sendFriendshipTo("John", mary);
		assertTrue(john.hasFriend("Mary"));
		assertTrue(mary.hasFriend("John"));
	}

	@Test
	public void autoAcceptDoesNotAffectCurrentRequests() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.sendFriendshipTo("John", mary);
		sn.autoAcceptFriendshipsTo(john);
		assertFalse(john.hasFriend("Mary"));
		assertFalse(mary.hasFriend("John"));
	}

	@Test
	public void duplicateRequestAfterAutoAcceptAreAccepted() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.sendFriendshipTo("John", mary);
		sn.autoAcceptFriendshipsTo(john);
		sn.sendFriendshipTo("John", mary);
		assertTrue(john.hasFriend("Mary"));
		assertTrue(mary.hasFriend("John"));
	}

	@Test
	public void cancelFriendshipRemovesFromFriends() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.sendFriendshipTo("John", mary);
		sn.acceptFriendshipFrom("Mary", john);
		sn.sendFriendshipCancellationTo("John", mary);
		assertFalse(john.hasFriend("Mary"));
		assertFalse(mary.hasFriend("John"));
	}

	@Test
	public void leaveRemovesUserFromNetwork() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.leave(john);
		assertFalse(sn.listMembers().contains("John"));
	}

	@Test
	public void leaveRemovesUserFromFriendsLists() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.sendFriendshipTo("John", mary);
		sn.acceptFriendshipFrom("Mary", john);
		sn.leave(john);
		assertFalse(mary.hasFriend("John"));
	}
	

}
