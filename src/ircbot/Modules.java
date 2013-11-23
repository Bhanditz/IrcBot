package ircbot;

import ircbot.modules.Module;

import java.util.ArrayList;
import java.util.HashMap;

public class Modules {

	public static HashMap<String, Module> moduleMap = new HashMap<String, Module>();
	public static HashMap<String, String> commandMap = new HashMap<String, String>();
	public static HashMap<String, ArrayList<String>> ircReplyListenerMap = new HashMap<String, ArrayList<String>>();

	public static void init() {
		ircReplyListenerMap.put("MODE", new ArrayList<String>());
		ircReplyListenerMap.put("JOIN", new ArrayList<String>());
		ircReplyListenerMap.put("PRIVMSG", new ArrayList<String>());
	}
	public static void install(Module module) {
		moduleMap.put(module.name, module);
		for(String command : module.commands)
			commandMap.put(command, module.name);

		for(String replyCommand : module.ircReplyListeners)
			if (ircReplyListenerMap.containsKey(replyCommand))
				ircReplyListenerMap.get(replyCommand).add(module.name);
	}
	
	public static Module get(String moduleName) {
		if (moduleMap.containsKey(moduleName))
			return moduleMap.get(moduleName);
		return null;
	}
	
	public static Module getByCommand(String command_str) {
		if (commandMap.containsKey(command_str)) {
			return moduleMap.get(commandMap.get(command_str));
		} else return null;
	}
}
