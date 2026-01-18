package dev.tehbrian.agna.configurate;

import org.spongepowered.configurate.ConfigurateException;

/**
 * Loads and holds values from a {@link ConfigurateWrapper}.
 */
public interface Config<W extends ConfigurateWrapper<?>> {

	/**
	 * Loads values from the wrapper into memory.
	 *
	 * @throws ConfigurateException if something goes wrong
	 */
	void load() throws ConfigurateException;

	/**
	 * Gets the underlying configurate wrapper.
	 *
	 * @return the configurate wrapper
	 */
	W wrapper();

}
