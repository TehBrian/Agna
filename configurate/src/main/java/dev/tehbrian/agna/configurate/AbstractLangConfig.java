package dev.tehbrian.agna.configurate;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.spongepowered.configurate.NodePath;

/**
 * Retrieves {@code String}s from a {@link ConfigurateWrapper}
 * and parses them using {@link MiniMessage}.
 *
 * @param <W> the wrapper type
 */
public abstract class AbstractLangConfig<W extends ConfigurateWrapper<?>> extends AbstractRawConfig<W> {

	/**
	 * @param configurateWrapper the wrapper
	 */
	public AbstractLangConfig(final W configurateWrapper) {
		super(configurateWrapper);
	}

	/**
	 * Gets the value for {@code path} from {@link #wrapper}
	 * and parses it using {@link MiniMessage}.
	 *
	 * @param path        the config path
	 * @param tagResolver the tag resolver
	 * @return the component
	 * @throws NoSuchValueInConfigException if there is no value found at the specified path
	 */
	public Component c(final NodePath path, final TagResolver tagResolver) throws NoSuchValueInConfigException {
		return MiniMessage.miniMessage().deserialize(this.getAndVerifyString(path), tagResolver);
	}

	/**
	 * Gets the value for {@code path} from {@link #wrapper}
	 * and parses it using {@link MiniMessage}.
	 *
	 * @param path the config path
	 * @return the component
	 * @throws NoSuchValueInConfigException if there is no value found at the specified path
	 */
	public Component c(final NodePath path) throws NoSuchValueInConfigException {
		return MiniMessage.miniMessage().deserialize(this.getAndVerifyString(path));
	}

	/**
	 * Gets the value for {@code path} from {@link #wrapper}
	 * and verifies that it is not null.
	 *
	 * @param path the path
	 * @return the verified string
	 * @throws NoSuchValueInConfigException if there is no value found at the specified path
	 */
	protected String getAndVerifyString(final NodePath path) throws NoSuchValueInConfigException {
		final String rawValue = this.rootNode().node(path).getString();

		if (rawValue == null) {
			throw new NoSuchValueInConfigException("Attempted to get value at path " + path + " in config "
					+ wrapper.path().getFileName() + " but found nothing");
		}

		return rawValue;
	}

}
