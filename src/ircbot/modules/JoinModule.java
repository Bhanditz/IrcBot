package ircbot.modules;

import ircbot.Command;
import ircbot.IrcBot;

public class JoinModule extends Module {
	public JoinModule() {
		this.name = "JoinModule";
		this.commands.add("join");
	}

	@Override
	public void runCommand(Command command) {
		IrcBot.join(command.args);
	}
	
	@Override
	public void runEventListener(String[] args, String postfix) { }
}
