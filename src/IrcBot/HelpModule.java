package IrcBot;

import java.util.Map;

public class HelpModule extends Module {
	HelpModule() {
		this.name = "HelpModule";
		this.commands.add("help");
	}

	@Override
	void run(Command command) {
		if(command.args == "") {
			IrcBot.say(command.channel, "Usage: !help (<module>|list)");
		} else if(command.args.equals("list")) {
			String moduleList = "";
			for(Map.Entry<String, Module> entry : Modules.moduleMap.entrySet()) moduleList += " "+entry.getKey();
			IrcBot.say(command.channel, "Asennetut moduulit:"+moduleList);
		} else {
			Module module = Modules.moduleMap.get(command.args);
			if (module== null) {
				IrcBot.say(command.channel, "Ei moduulia "+command.args);
			} else { // On moduuli
				String commandList = "";
				for(String str : module.commands) commandList += " "+str;
				IrcBot.say(command.channel, module.name +" - Komennot:"+commandList);
			}
		}
	}
}
