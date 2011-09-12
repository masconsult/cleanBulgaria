package eu.masconsult;

public class PositionException extends Exception {

	private static final long serialVersionUID = 1578799408518040214L;

	public PositionException(String message) {
		super(message);
	}
	
	public PositionException(Throwable cause) {
		super(cause);
	}
	
}
