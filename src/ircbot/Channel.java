package ircbot;

import java.util.HashMap;

public class Channel {
	public String channelName;
	public HashMap<String, Usermode> userMap = new HashMap<String, Usermode>();
	
	public Channel(String channelName) {
		this.channelName = channelName;
	}
	public static boolean isChannel(String channelName) {
		return channelName.startsWith("#") || channelName.startsWith("&") || channelName.startsWith("!") || channelName.startsWith("+");
	}
	
	public void init(String userList) {
		Console.out("Initializing channel "+channelName);
		String[] list = userList.split(" ");
		Usermode mode;
		for(String user : list) {
			mode = null;
			if (user.startsWith("@"))      mode = Usermode.o;
			else if (user.startsWith("+")) mode = Usermode.v;
			if (mode != null) user = user.substring(1);
			Console.out("user: ", mode==null?"null":mode, user);
			userMap.put(user, mode);
		}
	}
	
	public void userJoin(String user) {
		userMap.put(user, null);
	}
	
	public void userPart(String user) {
		userMap.remove(user);
	}
	
	public void userMode(String user, String mode) {
		if (userMap.containsKey(user)) {
			if (mode.startsWith("+")) {
				if (mode.substring(1).equals("v")) {
					userMap.put(user, Usermode.v);
				} else if (mode.substring(1).equals("o")) {
					userMap.put(user, Usermode.o);
				}
			} else if (mode.startsWith("-")) {
				userMap.put(user, null);
			}
		}
	}
}
