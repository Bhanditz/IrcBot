package IrcBot;

public class ExitModule extends Module {
	ExitModule() {
		this.name = "ExitModule";
		this.commands.add("exit");
		this.commands.add("die");
		this.commands.add("leave");
		this.commands.add("kill");
	}

	@Override
	void run(Command command) {
		IrcBot.quit(command.args);
	}
}
