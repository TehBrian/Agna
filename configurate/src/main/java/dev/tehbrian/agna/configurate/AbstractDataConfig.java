package dev.tehbrian.agna.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurateException;

import java.util.Objects;

public abstract class AbstractDataConfig<W extends ConfigurateWrapper<?>, D> extends AbstractConfig<W> implements DataConfig<W, D> {

	protected @Nullable D data;

	/**
	 * @param configurateWrapper the wrapper
	 */
	public AbstractDataConfig(final W configurateWrapper) {
		super(configurateWrapper);
	}

	/**
	 * Loads values from the wrapper into memory.
	 *
	 * @throws ConfigurateException if something goes wrong
	 */
	@Override
	public void load() throws ConfigurateException {
		this.wrapper.load();
		this.data = Objects.requireNonNull(this.wrapper.rootNode().get(this.dataClass()), "Deserialized data is null.");
	}

	protected abstract Class<D> dataClass();

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public D data() {
		return Objects.requireNonNull(this.data, "Data is null. Did #load() fail?");
	}

}
