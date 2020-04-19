package acs.logic;

public class ElementNotFoundExcetpion extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ElementNotFoundExcetpion() {
		super();
	}

	public ElementNotFoundExcetpion(String message, Throwable cause) {
		super(message, cause);
	}

	public ElementNotFoundExcetpion(String message) {
		super(message);
	}

	public ElementNotFoundExcetpion(Throwable cause) {
		super(cause);
	}
}
