package IrcBot;

public enum Usermode {
	o('@', 'o'),
	v('+', 'v');
	
	public char symbol;
	public char value;

	private Usermode(char symbol, char value) {
		this.symbol = symbol;
		this.value = value;
	}
	
	public String toString() {
		return ""+symbol;
	}
}
