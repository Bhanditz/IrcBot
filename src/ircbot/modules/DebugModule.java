package ircbot.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ircbot.Command;
import ircbot.IrcBot;
import ircbot.Modules;
import ircbot.Channel;

public class DebugModule extends Module {
	public DebugModule() {
		this.name = "DebugModule";
		this.commands.add("debug");
	}

	@Override
	public void runCommand(Command command) {
		ArrayList<String> args = new ArrayList<String>();
		for(String argv : command.args.split(" ")) args.add(argv);
		
		if (args.size() == 0) args.add("");
		switch(args.get(0)) {
			case "list":
				if (args.size() == 1) args.add("");
				switch(args.get(1)) {
					case "commands":
						HashMap<String, String> commands = new HashMap<String, String>();
						for(Map.Entry<String, String> entry : Modules.commandMap.entrySet()) {
							if (!commands.containsKey(entry.getValue())) commands.put(entry.getValue(), "");
							commands.put(entry.getValue(), commands.get(entry.getValue())+" "+entry.getKey());
						}
						for(Map.Entry<String, String> entry : commands.entrySet())
							IrcBot.say(command.channel, "Module "+entry.getKey()+" -> "+entry.getValue());

						break;
					case "listeners":
						for(Map.Entry<String, ArrayList<String>> listEntry : Modules.ircReplyListenerMap.entrySet()) 
							for(int i = 0; i < listEntry.getValue().size(); i++)
								IrcBot.say(command.channel, "Listener "+listEntry.getKey()+" -> "+listEntry.getValue().get(i));

						break;
					case "modules":
						for(Map.Entry<String, Module> entry : Modules.moduleMap.entrySet())
							IrcBot.say(command.channel, "Modules "+entry.getKey());

						break;
					case "channels":
						for(Map.Entry<String, Channel> entry : IrcBot.channelMap.entrySet())
							IrcBot.say(command.channel, "Channel "+entry.getKey());

						break;
					default:
						IrcBot.say(command.channel, "!debug list (commands|listeners|channels|modules)");
				}
				break;
			default:
				IrcBot.say(command.channel, "!debug (list)");
		}
	}
	
	@Override
	public void runEventListener(String[] args, String postfix) { }
}
