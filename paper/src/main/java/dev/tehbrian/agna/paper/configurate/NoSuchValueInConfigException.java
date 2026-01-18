package dev.tehbrian.agna.paper.configurate;

import java.io.Serial;

/**
 * Thrown to indicate that no value was found at the given path.
 */
public class NoSuchValueInConfigException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -7199781355817557125L;

	/**
	 * @param message the detail message
	 */
	public NoSuchValueInConfigException(final String message) {
		super(message);
	}

}
