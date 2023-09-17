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
		sn = new SocialNetwork();
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test 
	public void canJoinSocialNetwork() {
		Account me = sn.join("Hakan");
		assertEquals("Hakan", me.getUserName());
	}
	
	@Test 
	public void twoPeopleCanJoinSocialNetworkAndSizeOfNetworkEqualsTwo() {
		sn.join("Hakan");
		sn.join("Cecile");
		Set<String> members = sn.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}
	
	@Test 
	public void becomesFriendsAfterSendingAndAcceptingFriendRequest() {
	    sn = new SocialNetwork();
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		sn.login(her);
		sn.acceptFriendshipFrom("Hakan");
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
		sn.login(me);
		sn.sendFriendshipTo("Cecile");
		assertTrue(her.getIncomingRequests().contains("Hakan"));
	}


	@Test
	public void acceptingFriendsEstablishesMutualFriendship() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(john);
		sn.sendFriendshipTo("Mary");
		sn.login(mary);
		sn.acceptFriendshipFrom("John");
		assertTrue(mary.hasFriend("John"));
		assertTrue(john.hasFriend("Mary"));
	}

	@Test
	public void acceptAllRequestsEstablishesAllFriendships() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		Account paul = sn.join("Paul");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(paul);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.acceptAllFriendships();
		assertTrue(john.hasFriend("Mary"));
		assertTrue(john.hasFriend("Paul"));
	}

	@Test
	public void rejectFriendRemovesRequests() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.rejectFriendshipFrom("John");
		assertFalse(mary.getIncomingRequests().contains("John"));
		assertFalse(john.getOutgoingRequests().contains("Mary"));
	}

	@Test
	public void rejectFriendDoesNotEstablishFriendship() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.rejectFriendshipFrom("Mary");
		assertFalse(mary.hasFriend("John"));
		assertFalse(john.hasFriend("Mary"));
	}

	@Test
	public void rejectAllRequestsRemovesAllRequests() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		Account paul = sn.join("Paul");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(paul);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.rejectAllFriendships();
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
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(paul);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.rejectAllFriendships();
		assertFalse(john.hasFriend("Mary"));
		assertFalse(john.hasFriend("Paul"));
		assertFalse(mary.hasFriend("John"));
		assertFalse(paul.hasFriend("John"));
	}

	@Test
	public void friendRequestAcceptedAutomaticallyIfAutoAcceptIsOn() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(john);
		sn.autoAcceptFriendships();
		sn.login(mary);
		sn.sendFriendshipTo("John");
		assertTrue(john.hasFriend("Mary"));
		assertTrue(mary.hasFriend("John"));
	}

	@Test
	public void autoAcceptDoesNotAffectCurrentRequests() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.autoAcceptFriendships();
		assertFalse(john.hasFriend("Mary"));
		assertFalse(mary.hasFriend("John"));
	}

	@Test
	public void duplicateRequestAfterAutoAcceptAreAccepted() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.autoAcceptFriendships();
		sn.login(mary);
		sn.sendFriendshipTo("John");
		assertTrue(john.hasFriend("Mary"));
		assertTrue(mary.hasFriend("John"));
	}

	@Test
	public void cancelFriendshipRemovesFromFriends() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.acceptFriendshipFrom("Mary");
		sn.login(mary);
		sn.sendFriendshipCancellationTo("John");
		assertFalse(john.hasFriend("Mary"));
		assertFalse(mary.hasFriend("John"));
	}

	@Test
	public void leaveRemovesUserFromNetwork() {
		Account john = sn.join("John");
		sn.join("Mary");
		sn.login(john);
		sn.leave();
		assertFalse(sn.listMembers().contains("John"));
	}

	@Test
	public void leaveRemovesUserFromFriendsLists() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.acceptFriendshipFrom("Mary");
		sn.login(john);
		sn.leave();
		assertFalse(mary.hasFriend("John"));
	}

	@Test
	public void newNetworkDoesNotHaveLoggedInUser() {
		assertNull(sn.getLoggedInUser());
	}

	@Test
	public void loginSetsLoggedInUser() {
		Account john = sn.join("John");
		sn.login(john);
		assertEquals(john, sn.getLoggedInUser());
	}

	@Test 
	public void loginReturnsPassedInUser() {
		Account john = sn.join("John");
		assertEquals(john, sn.login(john));
	}

	@Test
	public void loginOverwritesPreviousUser() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(john);
		sn.login(mary);
		assertEquals(mary, sn.getLoggedInUser());
	}

	@Test
	public void hasMemberReturnsFalseForNonMembers() {
		sn.join("Mary");
		sn.join("Paul");
		assertFalse(sn.hasMember("John"));
	}

	@Test
	public void hasMemberReturnsTrueForMembers() {
		sn.join("Mary");
		sn.join("Paul");
		assertTrue(sn.hasMember("Mary"));
		assertTrue(sn.hasMember("Paul"));
	}

	@Test
	public void friendRequestNotAutoAcceptedWhenAutoAcceptIsOff() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(john);
		sn.cancelAutoAcceptFriendships();
		sn.login(mary);
		sn.sendFriendshipTo("John");
		assertFalse(john.hasFriend("Mary"));
		assertFalse(mary.hasFriend("John"));
	}
	
	@Test
	public void friendRequestNotAutoAcceptedWhenAutoAcceptIsTurnedOffAfterTurnedOn(){
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		Account paul = sn.join("Paul");
		sn.login(john);
		sn.autoAcceptFriendships();
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.cancelAutoAcceptFriendships();
		sn.login(paul);
		sn.sendFriendshipTo("John");
		assertFalse(john.hasFriend("Paul"));
		assertFalse(paul.hasFriend("John"));
	}

	@Test
	public void blockerIsNotListedToBlockee() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.join("Paul");
		sn.login(john);
		sn.block("Mary");
		sn.login(mary);
		Set<String> membersVisibleToMary = sn.listMembers();
		assertFalse(membersVisibleToMary.contains("John"));
	}

	@Test
	public void blockPreventsBlockeeFromSendingFriendRequestToBlocker() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(john);
		sn.block("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		assertFalse(john.getIncomingRequests().contains("Mary"));
	}

	@Test
	public void blockDoesNotPreventBlockeeFromSendingFriendRequestToOthers() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		Account paul = sn.join("Paul");
		sn.login(john);
		sn.block("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("Paul");
		assertTrue(paul.getIncomingRequests().contains("Mary"));
	}

	@Test
	public void blockTerminatesCurrentFriendship() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(john);
		sn.sendFriendshipTo("Mary");
		sn.login(mary);
		sn.acceptFriendshipFrom("John");
		sn.login(john);
		sn.block("Mary");
		assertFalse(john.hasFriend("Mary"));
		assertFalse(mary.hasFriend("John"));
	}

	@Test
	public void blockRemovesPendingFriendshipRequests() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		sn.login(john);
		sn.block("Mary");
		assertFalse(mary.getOutgoingRequests().contains("John"));
		assertFalse(john.getIncomingRequests().contains("Mary"));
	}

	@Test
	public void unblockMakesBlockeeVisibleToBlockerAgain() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(john);
		sn.block("Mary");
		sn.unblock("Mary");
		sn.login(mary);
		Set<String> membersVisibleToMary = sn.listMembers();
		assertTrue(membersVisibleToMary.contains("John"));
	}

	@Test
	public void unblockAllowsBlockeeToSendFriendRequestToBlocker() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		sn.login(john);
		sn.block("Mary");
		sn.unblock("Mary");
		sn.login(mary);
		sn.sendFriendshipTo("John");
		assertTrue(john.getIncomingRequests().contains("Mary"));
	}

	@Test
	public void recommendFriendsRecommendsEligibleNonFriends() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		Account paul = sn.join("Paul");
		Account peter = sn.join("Peter");
		setUpFriendshipDiamond(sn, john, mary, paul, peter);
		sn.login(john);
		Set<String> recommendedFriends = sn.recommendFriends();
		assertTrue(recommendedFriends.contains("Peter"));
	}

	@Test
	public void recommendFriendsDoesNotRecommendUserWhoIsAlreadyFriend() {
		Account john = sn.join("John");
		Account mary = sn.join("Mary");
		Account paul = sn.join("Paul");
		Account peter = sn.join("Peter");
		setUpFriendshipDiamond(sn, john, mary, paul, peter);
		sn.login(john);
		sn.sendFriendshipTo("Peter");
		sn.login(peter);
		sn.acceptFriendshipFrom("John");
		sn.login(john);
		Set<String> recommendedFriends = sn.recommendFriends();
		assertFalse(recommendedFriends.contains("Peter"));
	}

	/**
	 * This is a helper method to set up a friendship diamond where john is friends with mary and paul, and mary and paul are friends with peter. Peter is logged in at the end.
	 * @param sn SocialNetwork where the following accounts are members
	 * @param john Account with user name "John"
	 * @param mary Account with user name "Mary"
	 * @param paul Account with user name "Paul"
	 * @param peter Account with user name "Peter"
	 */
	public void setUpFriendshipDiamond(SocialNetwork sn, Account john, Account mary, Account paul, Account peter) {
		sn.login(john);
		sn.sendFriendshipTo("Mary");
		sn.sendFriendshipTo("Paul");
		sn.login(peter);
		sn.sendFriendshipTo("Mary");
		sn.sendFriendshipTo("Paul");
		sn.login(mary);
		sn.acceptFriendshipFrom("John");
		sn.acceptFriendshipFrom("Peter");
		sn.login(paul);
		sn.acceptFriendshipFrom("John");
		sn.acceptFriendshipFrom("Peter");
		sn.login(peter);

	}

}
