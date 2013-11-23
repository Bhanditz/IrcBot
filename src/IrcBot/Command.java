package IrcBot;

public class Command {
	public String command = null;
	public String args = "";
	public String channel = null;
	public String user = null;
	
	public Command(String[] _args, String msg) {
		if (_args[1].equals("PRIVMSG")) {
			String[] parts = msg.substring(1).split(" ", 2);

			this.command = parts[0];
			if (parts.length > 1)
				this.args    = parts[1];
			this.channel = _args[2];
			this.user    = _args[0];
		}
	}
}
