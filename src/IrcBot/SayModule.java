package IrcBot;

public class SayModule extends Module {
	SayModule() {
		this.name = "SayModule";
		this.commands.add("say");
		this.commands.add("sano");
	}

	@Override
	void run(Command command) {
		IrcBot.say(command.channel, command.args);
	}
}
