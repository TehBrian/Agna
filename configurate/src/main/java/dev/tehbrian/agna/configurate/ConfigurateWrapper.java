package dev.tehbrian.agna.configurate;

import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Wraps around a {@link ConfigurationLoader} and provides methods for loading,
 * saving, and getting the root node.
 *
 * @param <L> the loader type
 */
public class ConfigurateWrapper<L extends ConfigurationLoader<CommentedConfigurationNode>> {

	private final Path path;
	private final L loader;

	private @Nullable CommentedConfigurationNode rootNode;

	/**
	 * @param path   the path for the config
	 * @param loader the loader
	 */
	public ConfigurateWrapper(final Path path, final L loader) {
		this.path = path;
		this.loader = loader;
	}

	/**
	 * Gets the root node.
	 *
	 * @return the root node
	 * @throws NullPointerException if {@link #load()} has not been called
	 */
	public CommentedConfigurationNode rootNode() {
		return Objects.requireNonNull(this.rootNode, "Root node is null. Did #load() fail?");
	}

	/**
	 * Loads the root node from the file system.
	 */
	public void load() throws ConfigurateException {
		this.rootNode = this.loader.load();
	}

	/**
	 * Saves the root node to the file system.
	 */
	public void save() throws ConfigurateException {
		this.loader.save(this.rootNode());
	}

	/**
	 * Gets the path that the loader will load from and save to.
	 *
	 * @return the path
	 */
	public Path path() {
		return this.path;
	}

}
