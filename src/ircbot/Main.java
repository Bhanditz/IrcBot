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
			Modules.init();

			Console.out("Installing modules");
			Modules.install(new SayModule());
			Modules.install(new ExitModule());
			Modules.install(new NamesModule());
			Modules.install(new HelpModule());
			Modules.install(new AutoOpModule());

			/*
			 * IRCnet   : open.ircnet.net
			 * freenode : irc.freenode.net
			 */
			IrcBot.connect("irc.freenode.net", 6667);
			IrcBot.join("#narven_bot");
			IrcBot.run();
			
		} catch(Exception e) {
			Console.err("Main::main", e);
			e.printStackTrace();
		}
	}

}
