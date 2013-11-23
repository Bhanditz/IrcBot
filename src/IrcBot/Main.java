package IrcBot;

import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		Console.out("Starting IrcBot");

		IrcBotConfig botConfig = new IrcBotConfig(); 
		botConfig.nick = "narven-bot";
		

		try {
			IrcBot bot = new IrcBot(botConfig);
			
			Console.out("Initializing modules");
			Modules.install(new SayModule(bot, "SayModule"));
			Modules.install(new ExitModule(bot, "ExitModule"));
			Modules.install(new NamesModule(bot, "NamesModule"));

//			bot.connect("open.ircnet.net", 6667);
			bot.connect("irc.freenode.net", 6667);
			bot.join("#narven_bot");

			Thread	   thread = new Thread(bot);
			thread.start();
			
		} catch(Exception e) {
			Console.err("Main::main", e);
			e.printStackTrace();
		}
	}

}
