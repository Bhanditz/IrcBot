package ircbot;

import ircbot.modules.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Constructor;

public class Modules {

	public static HashMap<String, Module> moduleMap = new HashMap<String, Module>();
	public static HashMap<String, String> commandMap = new HashMap<String, String>();
	public static HashMap<String, ArrayList<String>> ircReplyListenerMap = new HashMap<String, ArrayList<String>>();

	public static void init() { }

	public static void install(Module module) {
		if (module == null) return;
		Console.out("Modules", "Installing module "+module.name);
		moduleMap.put(module.name, module);
	}

	public static void load(String moduleName) {
		if (!moduleMap.containsKey(moduleName)) return;
		Module module = moduleMap.get(moduleName);

		Console.out("Modules", "Loading module "+module.name);
		
		for(String command : module.commands)
			commandMap.put(command, module.name);

		for(String replyCommand : module.ircReplyListeners) {
			if (!ircReplyListenerMap.containsKey(replyCommand))
				ircReplyListenerMap.put(replyCommand, new ArrayList<String>());

			ircReplyListenerMap.get(replyCommand).add(module.name);
		}
	}
	public static void loadAll() {
		for(Map.Entry<String, Module> entry : moduleMap.entrySet())
			load(entry.getKey());
	}

	public static void install(String moduleName) {
		try {
			Constructor ctor = Class.forName("ircbot.modules."+moduleName).getDeclaredConstructor();
		    Module module = (Module)ctor.newInstance();
			install(module);
		} catch (Exception e) { }
	}
	
	public static void unload(String moduleName) {
		if (!moduleMap.containsKey(moduleName)) return;
		
		for(Map.Entry<String, String> entry : commandMap.entrySet())
			if (entry.getValue().equals(moduleName))
				commandMap.remove(entry.getKey());
			
		for(Map.Entry<String, ArrayList<String>> listEntry : ircReplyListenerMap.entrySet())
			for(int i = 0; i < listEntry.getValue().size(); i++)
				if (listEntry.getValue().get(i).equals(moduleName))
					ircReplyListenerMap.get(listEntry.getKey()).remove(i);
	}
	
	public static void uninstall(String moduleName) {
		if (!moduleMap.containsKey(moduleName)) return;
		
		unload(moduleName);

		moduleMap.remove(moduleName);
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
