package clueGame;

public class BadConfigFormatException extends Exception {
	private String message;
	public BadConfigFormatException (String message) {
		this.message = message;
	}
	
	// basic constructor that sets message to default phrase
	public BadConfigFormatException () {
		this.message = "An error occurred while handling a build file";
	}

	@Override
	public String toString() {
		if (!message.equals(null)) return message;
		else return "An error occurred while handling a build file";
	}
}