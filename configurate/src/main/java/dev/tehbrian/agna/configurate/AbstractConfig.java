package dev.tehbrian.agna.configurate;

import org.spongepowered.configurate.ConfigurateException;

/**
 * Loads and holds values from a {@link ConfigurateWrapper}.
 *
 * @param <W> the wrapper type
 */
public abstract class AbstractConfig<W extends ConfigurateWrapper<?>> implements Config<W> {

	protected final W wrapper;

	/**
	 * @param wrapper the wrapper
	 */
	public AbstractConfig(final W wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * Loads values from the wrapper into memory.
	 *
	 * @throws ConfigurateException if something goes wrong
	 */
	public abstract void load() throws ConfigurateException;

	/**
	 * Gets the underlying configurate wrapper.
	 *
	 * @return the configurate wrapper
	 */
	public W wrapper() {
		return this.wrapper;
	}

}
