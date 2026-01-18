package dev.tehbrian.agna.configurate;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;

public abstract class AbstractRawConfig<W extends ConfigurateWrapper<?>> extends AbstractConfig<W> implements RawConfig<W> {

	/**
	 * @param configurateWrapper the wrapper
	 */
	public AbstractRawConfig(final W configurateWrapper) {
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
	}

	/**
	 * Gets the root node.
	 *
	 * @return the root node
	 */
	public CommentedConfigurationNode rootNode() {
		return this.wrapper.rootNode();
	}

}
