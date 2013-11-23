package IrcBot;

public class NamesModule extends Module {
	NamesModule(IrcBot bot, String name) {
		this.bot = bot;
		this.name = name;
		this.commands.add("names");
	}

	@Override
	void run(Command command) {
		this.bot.say(command.channel, "TODO: /NAMES -command");
	}
}
