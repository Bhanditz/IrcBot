package ircbot.modules;

import ircbot.Channel;
import ircbot.Command;
import ircbot.IrcBot;
import ircbot.Usermode;

import java.util.Map;

public class NamesModule extends Module {
	public NamesModule() {
		this.name = "NamesModule";
		this.commands.add("names");
	}

	@Override
	public void run(Command command) {
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
