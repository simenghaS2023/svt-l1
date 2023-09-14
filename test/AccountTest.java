import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class AccountTest {
	
	Account me, her, another;
    
    // these are some example tests: you can merge them with your own tests from A0 
	
	@Before
	public void setUp() throws Exception {
		me = new Account("Hakan");
		her = new Account("Serra");
		another = new Account("Cecile");
	}
	
	@Test
	public void cannotInitiateFriendshipWithAnExistingFriend() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		assertTrue(her.hasFriend(me.getUserName()));
		me.requestFriendship(her);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
		assertFalse(her.getIncomingRequests().contains(me.getUserName()));
	}

	@Test
	public void canBeFriendsWithAnother() {
		me.requestFriendship(her);
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void noFriendRequests() {
		assertEquals(0, me.getIncomingRequests().size());
	}
	
	@Test
	public void testMultipleFriendRequests() {
		me.requestFriendship(her);
		me.requestFriendship(another);
		assertEquals(2, me.getIncomingRequests().size());
		assertTrue(me.getIncomingRequests().contains(another.getUserName()));
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void doubleFriendRequestsAreOk() {
		me.requestFriendship(her);
		me.requestFriendship(her);
		assertEquals(1, me.getIncomingRequests().size());
	}
	
	@Test
	public void afterAcceptingFriendRequestWhoWantsToBeFriendsUpdated() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void everybodyAreFriends() {
		me.requestFriendship(her);
		me.requestFriendship(another);
		her.requestFriendship(another);
		her.friendshipAccepted(me);
		another.friendshipAccepted(her);
		another.friendshipAccepted(me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(me.hasFriend(another.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
		assertTrue(her.hasFriend(another.getUserName()));
		assertTrue(another.hasFriend(her.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
	}
	
	@Test
	public void cannotBeFriendsWithAnExistingFriend() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		assertTrue(her.hasFriend(me.getUserName()));
		me.requestFriendship(her);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
		assertFalse(her.getIncomingRequests().contains(me.getUserName()));
	}

	@Test
	public void canRegisterOutgoingRequestsForNewFriends() {
		her.requestFriendship(me);
		assertTrue(me.getOutgoingRequests().contains(her.getUserName()));
	}

	@Test
	public void noDuplicateOutgoingRequests() {
		her.requestFriendship(me);
		her.requestFriendship(me);
		assertEquals(1, me.getOutgoingRequests().size());
	}

	@Test
	public void outgoingRequestsAreRemovedWhenAccepted() {
		her.requestFriendship(me);
		me.friendshipAccepted(her);
		assertFalse(me.getOutgoingRequests().contains(her.getUserName()));
	}

	@Test
	public void noOutgoingRequestIfAlreadyFriends() {
		her.requestFriendship(me);
		me.friendshipAccepted(her);
		her.requestFriendship(me);
		assertEquals(0, me.getOutgoingRequests().size());
	}

	@Test
	public void outgoingRequestRemovedIfRejected() {
		her.requestFriendship(me);
		me.friendshipRejected(her);
		assertFalse(me.getOutgoingRequests().contains(her.getUserName()));
	}

	@Test
	public void incomingRequestRemovedIfRejected() {
		her.requestFriendship(me);
		me.friendshipRejected(her);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
	}

	@Test
	public void rejectionDoesNotAffectCurrentFriends() {
		her.requestFriendship(me);
		me.friendshipAccepted(her);
		me.requestFriendship(another);
		me.friendshipRejected(another);
		assertTrue(me.hasFriend(her.getUserName()));
		assertFalse(me.hasFriend(another.getUserName()));
	}

	@Test
	public void autoAcceptEstablishesFriendshipAutomatically() {
		her.autoAcceptFriendships();
		her.requestFriendship(me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
	}

	@Test
	public void autoAcceptDoesNotAffectCurrentRequests() {
		her.requestFriendship(me);
		her.autoAcceptFriendships();
		assertTrue(her.getIncomingRequests().contains(me.getUserName()));
		assertTrue(me.getOutgoingRequests().contains(her.getUserName()));
	}

	@Test
	public void duplicateRequestAfterAutoAcceptAreAccepted() {
		her.requestFriendship(me);
		her.autoAcceptFriendships();
		her.requestFriendship(me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
	}

	@Test
	public void cancelFriendshipRemovesFromFriends() {
		her.requestFriendship(me);
		me.friendshipAccepted(her);
		her.cancelFriendship(me);
		assertFalse(me.hasFriend(her.getUserName()));
		assertFalse(her.hasFriend(me.getUserName()));
	}

	@Test
	public void cancelFriendshipDoesNotAffectPendingRequests() {
		her.requestFriendship(me);
		her.cancelFriendship(me);
		assertTrue(me.getOutgoingRequests().contains(her.getUserName()));
		assertTrue(her.getIncomingRequests().contains(me.getUserName()));
	}

}
