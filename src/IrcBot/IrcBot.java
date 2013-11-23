package IrcBot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import sun.org.mozilla.javascript.commonjs.module.ModuleScope;

public class IrcBot implements Runnable {
	private String nick = null;
	private String realname = null;
	private String username = null;
	private String serverHost = null;
	private int serverPort;
	private Socket socket = null;
	private String mode = null;
	
	private HashMap<String, Channel> channelMap = new HashMap<String, Channel>();
	private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<String>(1000);
	private PrintWriter out;
	private BufferedReader in;
	
	private Thread outThread = new Thread() {
		public void run() {
			try {
				OutputStream out = socket.getOutputStream();
				while (true) {
					String s = outQueue.take();
					System.out.println(getIDName() + " >>> " + s);
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
	
	public IrcBot(IrcBotConfig config) {
		Console.out(toString(), "Creating IrcBot", config);
		this.nick = config.nick;
		this.realname = config.realname;
		this.username = config.username;
	}
	
	public boolean connect(String host, int port) throws IOException {
		Console.out(this, "Connecting to "+host+":"+port);
		this.serverHost = host;
		this.serverPort = port;

		this.socket = new Socket(host, port);

		this.out = new PrintWriter(this.socket.getOutputStream(), true);
		this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
		
		return false;
	}
	public boolean connect(String host) throws IOException {
		return connect(host, 6667);
	}

	public void sendRawString(String s) {
		outQueue.add(s);
	}
	public void say(String channelName, String msg) {
		sendRawString("PRIVMSG "+channelName+" :"+msg);
	}
	public void quit(String msg) {
		synchronized(socket) {
			try {
				this.sendRawString("QUIT :"+msg);
				socket.close();
			} catch(IOException e) {
				Console.err("ircBot::quit", e);
			}
		} 
	}

	public boolean join(String channel_str) {
		Console.out(this, "Joinig to "+channel_str);
		
		if (this.channelMap.containsKey(channel_str)) return false;

		Channel channel = new Channel(channel_str);
		this.channelMap.put(channel_str, channel);
		
		return false;
	}

	private void act(String prefix, String[] args, String postfix) {
/*		System.out.println("IrcBot:act prefix\t"+prefix);
		System.out.print("IrcBot:act args");
		for(String str : args) System.out.print("\t"+str);
		System.out.println("\nIrcBot:act postfix\t"+postfix);*/
		
		String from = args[0];
		String command_str = prefix;

		if (args.length > 1) command_str = args[1];
		
		switch(prefix) {
			case "PING":
				Console.out("Answering to PING");
				sendRawString("PONG :"+args[0]);
				break;
			default:
				//TODO modules
				switch(command_str) {
					case "MODE":
						this.mode = postfix;
						break;
					case "NOTICE":
						Console.out("NOTICE", postfix);
						break;
					case "JOIN":
						this.channelMap.get(args[2]).userJoin(args[0]);
						break;
					case "PART":
						this.channelMap.get(args[2]).userPart(args[0]);
						break;
					case "PRIVMSG":
						if (postfix.startsWith("!")) {
							Module module = Modules.getByCommand(postfix.substring(1).split(" ")[0]);
							Command command = new Command(args, postfix);
							if (module != null) {
								Console.out("Using module "+module.name);
								module.run(command);
							} else {
								this.say(args[2], "No module using command "+command.command);
							}
						}
						break;
					case "353":
						this.channelMap.get(args[4]).init(postfix);
						break;
				}
		}
	}
	
	private void processLine(String line) {
		System.out.println(this.toString() + " <<< " + line);
		
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
	
	@Override
	public void run() {
		if (this.nick == null || this.username == null || this.realname == null) return;
		
		try {
			outThread.start();
	
			sendRawString("NICK " + this.nick);
			sendRawString("USER " + this.username + " 0 * :" + this.realname);

			for( Map.Entry<String, Channel> channel : this.channelMap.entrySet() ) {
				sendRawString("JOIN " + channel.getValue().channelName);
			}
			
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
	
	public String getIDName() {
		return this.nick+"!"+this.username;
	}
	public String toString() {
		return getIDName();
	}
}
