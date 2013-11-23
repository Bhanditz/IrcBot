package IrcBot;

import java.util.HashMap;

public class Channel {
	public String channelName;
	public HashMap<String, String> userMap = new HashMap<String, String>();
	
	public Channel(String channelName) {
		this.channelName = channelName;
	}
	
	public void init(String userList) {
		
	}
	
	public void userJoin(String user) {
		
	}
	
	public void userPart(String user) {
		
	}
}
