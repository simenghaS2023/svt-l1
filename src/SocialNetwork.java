import java.util.HashSet;
import java.util.Set;

public class SocialNetwork {
	
	private Set<Account> accounts = new HashSet<Account>();

	// join SN with a new user name
	public Account join(String userName) {
		Account newAccount = new Account(userName);
		accounts.add(newAccount);
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
			members.add(each.getUserName());
		}
		return members;
	}
	
	// from my account, send a friend request to user with userName from my account
	public void sendFriendshipTo(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.requestFriendship(me);
	}

	// from my account, accept a pending friend request from another user with userName
	public void acceptFriendshipFrom(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipAccepted(me);
	}

	public void acceptAllFriendshipsTo(Account account) {
		Set<String> incomingRequestCopy = new HashSet<String>(account.getIncomingRequests());
		for (String each : incomingRequestCopy) {
			acceptFriendshipFrom(each, account);
		}
	}

	public void rejectFriendshipFrom(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipRejected(accountForUserName);
	}

	public void rejectAllFriendshipsTo(Account account) {
		Set<String> incomingRequestCopy = new HashSet<String>(account.getIncomingRequests());
		for (String each : incomingRequestCopy) {
			rejectFriendshipFrom(each, account);
		}
	}

    public void autoAcceptFriendshipsTo(Account account) {
		account.autoAcceptFriendships();
    }

	public void sendFriendshipCancellationTo(String toName, Account fromUser) {
		Account accountForUserName = findAccountForUserName(toName);
		accountForUserName.cancelFriendship(fromUser);
	}

    public void leave(Account user) {
		for (Account account : accounts) {
			account.getFriends().remove(user.getUserName());
			account.getIncomingRequests().remove(user.getUserName());
			account.getOutgoingRequests().remove(user.getUserName());
		}
		accounts.remove(user);
    }

}
