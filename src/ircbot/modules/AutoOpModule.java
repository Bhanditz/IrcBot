package ircbot.modules;

import ircbot.Command;
import ircbot.IrcBot;

public class AutoOpModule extends Module {
	public AutoOpModule() {
		this.name = "AutoOpModule";
		this.ircReplyListeners.add("JOIN");
	}

	@Override
	public void runCommand(Command command) {
	}
	
	@Override
	public void runEventListener(String[] args, String postfix) {
		switch(args[1]) {
			case "JOIN":
				IrcBot.say(args[2], "Looks like someone just joined!");
				break;
		}
	}
}
