package ircbot;

import ircbot.modules.*;
import org.json.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class Main {
	
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

	public static void main(String[] args) {
		Console.out("Starting IrcBot");

		String nickName   = "narven-bot";
		String channel    = "#narven_bot";
		String serverHost = "irc.freenode.net";
		int    serverPort = 6667;
		/*
		 * IRCnet   : open.ircnet.net
		 * freenode : irc.freenode.net
		 */

		try {
			String jsonconfig = readFile("config/IrcBot.json", StandardCharsets.UTF_8);
			JSONObject config = new JSONObject(jsonconfig);

			IrcBotConfig botConfig = new IrcBotConfig(); 
			botConfig.nick = nickName;

			IrcBot.init(botConfig);
			
			Console.out("Initializing modules");
			Modules.init();

			Console.out("Installing modules");
			JSONArray moduleList = config.getJSONArray("modules");
			if (moduleList == null) moduleList = new JSONArray();
			
			for(int i = 0; i < moduleList.length(); i++) {
				String moduleName = moduleList.get(i).toString(); 
				Modules.install(moduleName);
			}
			Modules.loadAll();
				
			IrcBot.connect(serverHost, serverPort);
			IrcBot.join(channel);

			IrcBot.run();

			
		} catch(Exception e) {
			Console.err("Main::main", e);
			e.printStackTrace();
		}
	}

}
