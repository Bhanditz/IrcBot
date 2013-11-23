package ircbot.modules;

import ircbot.Command;
import ircbot.IrcBot;

import java.util.ArrayList;

public abstract class Module {
	public ArrayList<String> commands = new ArrayList<String>();
	public String name = null;
	public IrcBot bot = null;
	
	abstract public void run(Command command);
}
