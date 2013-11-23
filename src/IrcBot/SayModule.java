package IrcBot;

public class SayModule extends Module {
	SayModule(IrcBot bot, String name) {
		this.bot = bot;
		this.name = name;
		this.commands.add("say");
		this.commands.add("sano");
	}

	@Override
	void run(Command command) {
		this.bot.say(command.channel, command.args);
	}
}
