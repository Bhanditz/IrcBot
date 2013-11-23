package ircbot;

import ircbot.modules.Module;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class IrcBot {
	public static String nick = null;
	public static String realname = null;
	public static String username = null;
	public static String serverHost = null;
	public static int serverPort;
	public static String mode = null;
	public static HashMap<String, Channel> channelMap = new HashMap<String, Channel>();
	public static LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<String>(1000);
	private static Socket socket = null;
	private static PrintWriter out;
	private static BufferedReader in;
	
	private static Thread outThread = new Thread() {
		public void run() {
			try {
				OutputStream out = socket.getOutputStream();
				while (true) {
					String s = outQueue.take();
					System.out.println(IrcBot.getIDName() + " >>> " + s);
					s = s.replace("\n", "").replace("\r", "");
					s = s + "\r\n";
					out.write(s.getBytes("UTF-8"));
					out.flush();
				}
			} catch (Exception e) {
				Console.err("IrcBot::outThread", "Outqueue died", e);
				outQueue.clear();
				outQueue = null;
			}
		}
	};
	
	public static void init(IrcBotConfig config) {
		Console.out("IrcBot", "Creating IrcBot", config);
		nick = config.nick;
		realname = config.realname;
		username = config.username;
	}
	
	public static boolean connect(String host, int port) throws IOException {
		Console.out("IrcBot", "Connecting to "+host+":"+port);
		serverHost = host;
		serverPort = port;

		socket = new Socket(host, port);

		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

		sendRawString("NICK " + nick);
		sendRawString("USER " + username + " 0 * :" + realname);

		return false;
	}
	public static boolean connect(String host) throws IOException {
		return connect(host, 6667);
	}

	public static void sendRawString(String s) {
		outQueue.add(s);
	}
	public static void say(String channelName, String msg) {
		sendRawString("PRIVMSG "+channelName+" :"+msg);
	}
	public static void quit(String msg) {
		synchronized(socket) {
			try {
				sendRawString("QUIT :"+msg);
				socket.close();
			} catch(IOException e) {
				Console.err("ircBot::quit", e);
			}
		} 
	}

	public static boolean part(String channel_str) {
		Console.out("IrcBot", "Parting to "+channel_str);

		sendRawString("PART " + channel_str);

		if (!channelMap.containsKey(channel_str)) return true;

		channelMap.remove(channel_str);
		
		return false;
	}
	public static boolean join(String channel_str) {
		Console.out("IrcBot", "Joinig to "+channel_str);

		sendRawString("JOIN " + channel_str);

		if (channelMap.containsKey(channel_str)) return false;

		Channel channel = new Channel(channel_str);
		channelMap.put(channel_str, channel);
		
		return false;
	}

	private static void act(String prefix, String[] args, String postfix) {
		String from = args[0];
		String command_str = prefix;

		if (args.length > 1) command_str = args[1];
		
		switch(prefix) {
			case "PING":
				Console.out("Answering to PING");
				sendRawString("PONG :"+args[0]);
				break;
			default:
				String[] parts = from.split("!");
				if (parts.length>1) from = parts[0];
				
				if (Modules.ircReplyListenerMap.containsKey(command_str))
					for(String modName : Modules.ircReplyListenerMap.get(command_str))
						Modules.moduleMap.get(modName).runEventListener(args, postfix);
				
				switch(command_str) {
					case "MODE":
						if (Channel.isChannel(args[2]) && channelMap.containsKey(args[2])) {
							channelMap.get(args[2]).userMode(args[4], args[3]);
						} else {
							if (nick.equals(args[2])) mode = postfix;
						}
						break;
					case "NOTICE":
						Console.out("NOTICE", postfix);
						break;
					case "JOIN":
						channelMap.get(args[2]).userJoin(from);
						break;
					case "PART":
						channelMap.get(args[2]).userPart(from);
						break;
					case "PRIVMSG":
						if (postfix.startsWith("!")) {
							Module module = Modules.getByCommand(postfix.substring(1).split(" ")[0]);
							Command command = new Command(args, postfix);
							if (module != null) {
								Console.out("Using module "+module.name);
								module.runCommand(command);
							} else {
								say(args[2], "No module using command "+command.command);
							}
						}
						break;
					case "353":
						channelMap.get(args[4]).init(postfix);
						break;
				}
		}
	}
	
	private static void processLine(String line) {
		System.out.println("IrcBot <<< " + line);
		
		String[] parts = line.trim().split(":", 3);

		String prefix = "", postfix = "";
		String[] args = null;
		switch(parts.length) {
			case 0: break;
			case 1:
				args    = parts[0].trim().split(" ");
				break;
			case 3:
			default:
				postfix = parts[2].trim();
			case 2:
				prefix  = parts[0].trim().toUpperCase();
				args    = parts[1].trim().split(" ");
				break;
		}

		act(prefix, args, postfix);
	}
	
	public static void run() {
		if (nick == null || username == null || realname == null) return;
		
		try {
			outThread.start();
			
			String line;
			while ((line = in.readLine()) != null) {
				try {
					processLine(line);
				} catch (Exception e) {
					Console.err(e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			Console.err(e);
			e.printStackTrace();
		}
	}
	
	public static String getIDName() {
		return nick+"!"+username;
	}
	public String toString() {
		return IrcBot.getIDName();
	}
}
