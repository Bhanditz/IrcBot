package ircbot;

import ircbot.modules.*;

import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		Console.out("Starting IrcBot");

		IrcBotConfig botConfig = new IrcBotConfig(); 
		botConfig.nick = "narven-bot";
		

		try {
			IrcBot.init(botConfig);
			
			Console.out("Initializing modules");
			Modules.install(new SayModule());
			Modules.install(new ExitModule());
			Modules.install(new NamesModule());
			Modules.install(new HelpModule());

//			bot.connect("open.ircnet.net", 6667);
			IrcBot.connect("irc.freenode.net", 6667);
			IrcBot.join("#narven_bot");

//			Thread thread = new Thread(IrcBot);
//			thread.start();
			IrcBot.run();
			
		} catch(Exception e) {
			Console.err("Main::main", e);
			e.printStackTrace();
		}
	}

}
