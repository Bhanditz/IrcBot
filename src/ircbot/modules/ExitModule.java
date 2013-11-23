package ircbot.modules;

import ircbot.Command;
import ircbot.IrcBot;

public class ExitModule extends Module {
	public ExitModule() {
		this.name = "ExitModule";
		this.commands.add("exit");
		this.commands.add("die");
		this.commands.add("leave");
		this.commands.add("kill");
	}

	@Override
	public void run(Command command) {
		IrcBot.quit(command.args);
	}
}
