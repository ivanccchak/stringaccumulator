package stringaccumulator;

public class NegativeNumberException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6519068316417650050L;

	public NegativeNumberException() {
	}

	public NegativeNumberException(String s) {
		super(s);
	}

	public NegativeNumberException(Throwable cause) {
		super(cause);
	}

	public NegativeNumberException(String message, Throwable cause) {
		super(message, cause);
	}

}
