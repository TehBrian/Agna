package dev.tehbrian.agna.configurate;

public interface DataConfig<W extends ConfigurateWrapper<?>, D> extends Config<W> {

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	D data();

}
