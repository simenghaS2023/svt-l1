import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SocialNetwork implements ISocialNetwork{
	
	private Set<Account> accounts = new HashSet<Account>();
	private Account loggedInUser = null;
	private Map<Account, Set<Account>> blocker2blockee = new HashMap<Account, Set<Account>>();

	// join SN with a new user name
	public Account join(String userName) {
		Account newAccount = new Account(userName);
		accounts.add(newAccount);
		blocker2blockee.put(newAccount, new HashSet<Account>());
		return newAccount;
	}

	// find a member by user name 
	private Account findAccountForUserName(String userName) {
		// find account with user name userName
		// not accessible to outside because that would give a user full access to another member's account
		for (Account each : accounts) {
			if (each.getUserName().equals(userName)) 
					return each;
		}
		return null;
	}
	
	// list user names of all members
	public Set<String> listMembers() throws NoUserLoggedInException {
		ensureLoggedIn();
		Set<String> members = new HashSet<String>();
		for (Account each : accounts) {
			Set<Account> thisBlockees = blocker2blockee.get(each);
			if (thisBlockees.contains(loggedInUser)) {
				continue;
			}
			members.add(each.getUserName());
		}
		return members;
	}

	@Override
	public Account login(Account me) {
		loggedInUser = me;
		return loggedInUser;
	}

	@Override
	public boolean hasMember(String userName) throws NoUserLoggedInException {
		ensureLoggedIn();
		return listMembers().contains(userName);
	}

	private void ensureLoggedIn() throws NoUserLoggedInException {
		if (loggedInUser == null) {
			throw new NoUserLoggedInException();
		}
	}

	@Override
	public void sendFriendshipTo(String userName) throws NoUserLoggedInException {
		ensureLoggedIn();
		Account accountForUserName = findAccountForUserName(userName);
		Set<Account> recipientBlockees = blocker2blockee.get(accountForUserName);
		if (recipientBlockees.contains(loggedInUser)) {
			return;
		}
		accountForUserName.requestFriendship(loggedInUser);
	}


	@Override
	public void block(String userName) throws NoUserLoggedInException {
		ensureLoggedIn();
		Account blockeeAccount = findAccountForUserName(userName);
		if (blockeeAccount == null) {
			// throw new NoSuchUserException("No such user: " + userName);
			return;
		}
		Set<Account> blockees = blocker2blockee.get(loggedInUser);
		blockees.add(blockeeAccount);

		// remove existing friendship between loggedInUser and blockeeAccount if any
		loggedInUser.getFriends().remove(blockeeAccount.getUserName());
		blockeeAccount.getFriends().remove(loggedInUser.getUserName());

		// remove existing friendship request between loggedInUser and blockeeAccount if any
		loggedInUser.getIncomingRequests().remove(blockeeAccount.getUserName());
		loggedInUser.getOutgoingRequests().remove(blockeeAccount.getUserName());
		blockeeAccount.getIncomingRequests().remove(loggedInUser.getUserName());
		blockeeAccount.getOutgoingRequests().remove(loggedInUser.getUserName());
		
	}

	@Override
	public void unblock(String userName) throws NoUserLoggedInException{
		ensureLoggedIn();
		Set<Account> blockees = blocker2blockee.get(loggedInUser);
		Account accountToUnblock = findAccountForUserName(userName);
		blockees.remove(accountToUnblock);
	}

	@Override
	public void sendFriendshipCancellationTo(String userName) throws NoUserLoggedInException {
		ensureLoggedIn();
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.cancelFriendship(loggedInUser);
	}

	@Override
	public void acceptFriendshipFrom(String userName) throws NoUserLoggedInException {
		ensureLoggedIn();
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipAccepted(loggedInUser);
	}

	@Override
	public void acceptAllFriendships() throws NoUserLoggedInException {
		ensureLoggedIn();
		Set<String> incomingRequestCopy = new HashSet<String>(loggedInUser.getIncomingRequests());
		for (String each : incomingRequestCopy) {
			acceptFriendshipFrom(each);
		}
	}

	@Override
	public void rejectFriendshipFrom(String userName) throws NoUserLoggedInException {
		ensureLoggedIn();
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipRejected(accountForUserName);
	}

	@Override
	public void rejectAllFriendships() throws NoUserLoggedInException{
		ensureLoggedIn();
		Set<String> incomingRequestCopy = new HashSet<String>(loggedInUser.getIncomingRequests());
		for (String each : incomingRequestCopy) {
			rejectFriendshipFrom(each);
		}
	}

	@Override
	public void autoAcceptFriendships() throws NoUserLoggedInException{
		ensureLoggedIn();
		loggedInUser.autoAcceptFriendships();
	}

	@Override
	public void cancelAutoAcceptFriendships() throws NoUserLoggedInException{
		ensureLoggedIn();
		loggedInUser.cancelAutoAcceptFriendships();
	}

	@Override
	public Set<String> recommendFriends() throws NoUserLoggedInException{
		ensureLoggedIn();
		Set<String> immediateFriends = loggedInUser.getFriends();
		Map<String, Integer> secondDegreeFriends2FriendshipWithImmediateFriendsCount = new HashMap<String, Integer>();
		for (String immediateFriendUsername : immediateFriends) {
			Account immediateFriendAccount = findAccountForUserName(immediateFriendUsername);
			Set<String> secondDegreeFriends = immediateFriendAccount.getFriends();
			for (String secondDegreeFriend : secondDegreeFriends) {
				int friendshipCount = secondDegreeFriends2FriendshipWithImmediateFriendsCount.getOrDefault(secondDegreeFriend, 0);
				friendshipCount++;
				secondDegreeFriends2FriendshipWithImmediateFriendsCount.put(secondDegreeFriend, friendshipCount);
			}
		}
		Set<String> recommendedFriends = new HashSet<String>();
		for (String secondDegreeFriend : secondDegreeFriends2FriendshipWithImmediateFriendsCount.keySet()) {
			if (immediateFriends.contains(secondDegreeFriend)) {
				continue;
			}
			if (secondDegreeFriends2FriendshipWithImmediateFriendsCount.get(secondDegreeFriend) >= 2) {
				recommendedFriends.add(secondDegreeFriend);
			}
		}
		return recommendedFriends;

	}

	@Override
	public void leave() throws NoUserLoggedInException{
		ensureLoggedIn();
		for (Account account : accounts) {
			account.getFriends().remove(loggedInUser.getUserName());
			account.getIncomingRequests().remove(loggedInUser.getUserName());
			account.getOutgoingRequests().remove(loggedInUser.getUserName());
		}
		accounts.remove(loggedInUser);
	}

	public Account getLoggedInUser() {
		return loggedInUser;
	}

}
