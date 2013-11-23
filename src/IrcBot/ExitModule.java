package IrcBot;

public class ExitModule extends Module {
	ExitModule(IrcBot bot, String name) {
		this.bot = bot;
		this.name = name;
		this.commands.add("exit");
		this.commands.add("die");
		this.commands.add("leave");
		this.commands.add("kill");
	}

	@Override
	void run(Command command) {
		this.bot.quit(command.args);
	}
}
