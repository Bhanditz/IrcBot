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
		String userList = "";
		for(Map.Entry<String, Usermode> entry : channel.userMap.entrySet())
			userList += entry.getValue().symbol+""+entry.getKey();
		IrcBot.say(command.channel, userList);
		IrcBot.say(command.channel, "End of /NAMES -list");
	}
}
