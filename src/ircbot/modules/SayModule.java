package ircbot.modules;

import ircbot.Command;
import ircbot.IrcBot;

public class SayModule extends Module {
	public SayModule() {
		this.name = "SayModule";
		this.commands.add("say");
		this.commands.add("sano");
	}

	@Override
	public void runCommand(Command command) {
		IrcBot.say(command.channel, command.args);
	}
	
	@Override
	public void runEventListener(String[] args, String postfix) {
		
	}
}
