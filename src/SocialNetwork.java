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
	public Set<String> listMembers() {
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
	public boolean hasMember(String userName) {
		return findAccountForUserName(userName) != null;
	}

	@Override
	public void sendFriendshipTo(String userName) {
		Account accountForUserName = findAccountForUserName(userName);
		Set<Account> recipientBlockees = blocker2blockee.get(accountForUserName);
		if (recipientBlockees.contains(loggedInUser)) {
			return;
		}
		accountForUserName.requestFriendship(loggedInUser);
	}

	@Override
	public void block(String userName) {
		if (loggedInUser == null) {
			// throw new NoUserLoggedInException("No user logged in");
			return;
		}
		Account blockeeAccount = findAccountForUserName(userName);
		if (blockeeAccount == null) {
			// throw new NoSuchUserException("No such user: " + userName);
			return;
		}
		Set<Account> blockees = blocker2blockee.get(loggedInUser);
		blockees.add(blockeeAccount);
	}

	@Override
	public void unblock(String userName) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'unblock'");
	}

	@Override
	public void sendFriendshipCancellationTo(String userName) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.cancelFriendship(loggedInUser);
	}

	@Override
	public void acceptFriendshipFrom(String userName) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipAccepted(loggedInUser);
	}

	@Override
	public void acceptAllFriendships() {
		Set<String> incomingRequestCopy = new HashSet<String>(loggedInUser.getIncomingRequests());
		for (String each : incomingRequestCopy) {
			acceptFriendshipFrom(each);
		}
	}

	@Override
	public void rejectFriendshipFrom(String userName) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipRejected(accountForUserName);
	}

	@Override
	public void rejectAllFriendships() {
		Set<String> incomingRequestCopy = new HashSet<String>(loggedInUser.getIncomingRequests());
		for (String each : incomingRequestCopy) {
			rejectFriendshipFrom(each);
		}
	}

	@Override
	public void autoAcceptFriendships() {
		loggedInUser.autoAcceptFriendships();
	}

	@Override
	public void cancelAutoAcceptFriendships() {
		loggedInUser.cancelAutoAcceptFriendships();
	}

	@Override
	public Set<String> recommendFriends() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'recommendFriends'");
	}

	@Override
	public void leave() {
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
