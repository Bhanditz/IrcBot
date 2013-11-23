package IrcBot;

public enum Usermode {
	o('@'),
	v('+');
	
	private char value;
	private Usermode(char value) {
		this.value = value;
	}
}
