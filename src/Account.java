import java.util.HashSet;
import java.util.Set;


public class Account  {
	
	// the unique user name of account owner
	private String userName;
	
	// list of members who are awaiting an acceptance response from this account's owner 
	private Set<String> incomingRequests = new HashSet<String>();

	// list of members whom this account's owner has sent a friend request to and are awaiting a response
	private Set<String> outgoingRequests = new HashSet<String>();
	
	// list of members who are friends of this account's owner
	private Set<String> friends = new HashSet<String>();

	private boolean autoAcceptFriendships = false;
	
	public Account(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	// return list of members who had sent a friend request to this account's owner 
	// and are still waiting for a response
	public Set<String> getIncomingRequests() {
		return incomingRequests; 
	}

	// an incoming friend request to this account's owner from another member account
	public void requestFriendship(Account fromAccount) {
		if (!friends.contains(fromAccount.getUserName())) {
			incomingRequests.add(fromAccount.getUserName());
			fromAccount.outgoingRequests.add(this.getUserName());
			if (autoAcceptFriendships) {
				friendshipAccepted(fromAccount);
			}
		}
	}

	// check if account owner has a member with user name userName as a friend
	public boolean hasFriend(String userName) {
		return friends.contains(userName);
	}

	// receive an acceptance from a member to whom a friend request has been sent and from whom no response has been received
	public void friendshipAccepted(Account toAccount) {
		friends.add(toAccount.getUserName());
		toAccount.friends.add(this.getUserName());
		toAccount.incomingRequests.remove(this.getUserName());
		this.outgoingRequests.remove(toAccount.getUserName());
	}
	
	public Set<String> getFriends() {
		return friends;
	}

	public Set<String> getOutgoingRequests() {
		return outgoingRequests;
	}

	public void friendshipRejected(Account toAccount) {
		if (this.outgoingRequests.contains(toAccount.getUserName())){
			toAccount.incomingRequests.remove(this.getUserName());
			this.outgoingRequests.remove(toAccount.getUserName());
		}
	}

	public void autoAcceptFriendships() {
		autoAcceptFriendships = true;
	}

	public void cancelFriendship(Account initiatingAccount) {
		friends.remove(initiatingAccount.getUserName());
		initiatingAccount.friends.remove(this.getUserName());
	}
}
