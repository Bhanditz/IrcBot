package ircbot.modules;

import ircbot.Command;
import ircbot.IrcBot;
import java.util.HashMap;
import java.util.Map;

public class AutoOpModule extends Module {
	//      channel         nick    mask
	HashMap<String, HashMap<String, String>> autoOpHosts = new HashMap<String, HashMap<String, String>>();
	
	public AutoOpModule() {
		this.name = "AutoOpModule";
		this.ircReplyListeners.add("JOIN");
	}
	
	public void addAutoOp(String channel, String nick, String host) {
		if (!this.autoOpHosts.containsKey(channel)) this.autoOpHosts.put(channel, new HashMap<String, String>());
		this.autoOpHosts.get(channel).put(nick, host);
	}

	@Override
	public void runCommand(Command command) { }
	
	@Override
	public void runEventListener(String[] args, String postfix) {
		switch(args[1]) {
			case "JOIN":
				if (this.autoOpHosts.containsKey(args[2])) {
					for (Map.Entry<String, String> entry: this.autoOpHosts.get(args[2]).entrySet())
						if (args[0].matches(entry.getValue()))
							IrcBot.sendRawString("MODE "+args[2]+" +o "+entry.getKey());
				}
					
				IrcBot.say(args[2], "Looks like someone just joined!");
				break;
		}
	}
}
