package IrcBot;

import java.util.Map;

public class NamesModule extends Module {
	NamesModule() {
		this.name = "NamesModule";
		this.commands.add("names");
	}

	@Override
	void run(Command command) {
		Channel channel = IrcBot.channelMap.get(command.channel);
		if (channel != null) {
			String userList = "";
			for(Map.Entry<String, Usermode> entry : channel.userMap.entrySet())
				if (entry.getValue() != null)
					 userList += " "+entry.getValue().symbol+""+entry.getKey();
				else userList += " "+entry.getKey();
			IrcBot.say(command.channel, userList);
			IrcBot.say(command.channel, "End of /NAMES -list");
		} else {
			IrcBot.say(command.channel, "Invalid channel "+command.channel);
		}
	}
}
