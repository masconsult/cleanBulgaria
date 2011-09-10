package eu.masconsult.connection;

public class ConnectionException extends Exception{
	
	private static final long serialVersionUID = 8175727264840611921L;

	public ConnectionException(String message) {
		super(message);
	}
	
	public ConnectionException(Throwable cause) {
		super(cause);
	}

}
