package dev.tehbrian.agna.paper;

import dev.tehbrian.tehlib.paper.EmptyTabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility methods for the main plugin class.
 */
public abstract class PluginUtils {

	private PluginUtils() {
	}

	/**
	 * Registers listeners to a plugin.
	 */
	public void registerListeners(final JavaPlugin plugin, final Listener... listeners) {
		final PluginManager manager = plugin.getServer().getPluginManager();
		for (final Listener listener : listeners) {
			manager.registerEvents(listener, plugin);
		}
	}

	/**
	 * Checks whether the resource already exists before trying to save it.
	 *
	 * @param filename the resource filename
	 */
	public void saveResourceSilently(final JavaPlugin plugin, final String filename) {
		final Path outPath = plugin.getDataPath().resolve(filename);
		if (!Files.exists(outPath)) {
			plugin.saveResource(filename, false);
		}
	}

	/**
	 * Disables this plugin.
	 */
	public void disableSelf(final JavaPlugin plugin) {
		plugin.getServer().getPluginManager().disablePlugin(plugin);
	}

	/**
	 * Registers a command to this plugin.
	 *
	 * @param name      the command name
	 * @param executor  the command executor
	 * @param completer the tab completer
	 */
	public void registerCommand(
			final JavaPlugin plugin,
			final String name,
			final CommandExecutor executor,
			final TabCompleter completer
	) {
		final PluginCommand command = Objects.requireNonNull(plugin.getCommand(name));
		command.setExecutor(executor);
		command.setTabCompleter(completer);
	}

	/**
	 * Registers a command to this plugin with {@link EmptyTabCompleter}
	 * as the tab completer.
	 *
	 * @param name     the command name
	 * @param executor the command executor
	 */
	public void registerCommand(
			final JavaPlugin plugin,
			final String name,
			final CommandExecutor executor
	) {
		this.registerCommand(plugin, name, executor, new EmptyTabCompleter());
	}

}
