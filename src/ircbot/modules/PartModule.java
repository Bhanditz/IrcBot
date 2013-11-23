package ircbot.modules;

import ircbot.Command;
import ircbot.IrcBot;

public class PartModule extends Module {
	public PartModule() {
		this.name = "PartModule";
		this.commands.add("part");
		this.commands.add("leave");
	}

	@Override
	public void runCommand(Command command) {
		if (command.channel.equals(command.args))
			IrcBot.part(command.args);
		else IrcBot.say(command.channel, "You have no power here for me!");
	}
	
	@Override
	public void runEventListener(String[] args, String postfix) { }
}
