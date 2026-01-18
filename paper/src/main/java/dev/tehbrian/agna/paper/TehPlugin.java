package dev.tehbrian.agna.paper;

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
 * Utility methods for the main plugin class. Extend as you would
 * normally would {@code JavaPlugin}.
 */
public abstract class TehPlugin extends JavaPlugin {

	/**
	 * Registers listeners to this plugin.
	 *
	 * @param listeners the listeners
	 */
	public void registerListeners(final Listener... listeners) {
		final PluginManager manager = this.getServer().getPluginManager();
		for (final Listener listener : listeners) {
			manager.registerEvents(listener, this);
		}
	}

	/**
	 * Checks whether the resource already exists before calling
	 * {@link #saveResource(String, boolean)}.
	 *
	 * @param filename the resource filename
	 */
	public void saveResourceSilently(final String filename) {
		final Path outPath = this.getDataPath().resolve(filename);
		if (!Files.exists(outPath)) {
			this.saveResource(filename, false);
		}
	}

	/**
	 * Disables this plugin.
	 */
	public void disableSelf() {
		this.getServer().getPluginManager().disablePlugin(this);
	}

	/**
	 * Registers a command to this plugin.
	 *
	 * @param name      the command name
	 * @param executor  the command executor
	 * @param completer the tab completer
	 */
	public void registerCommand(
			final String name,
			final CommandExecutor executor,
			final TabCompleter completer
	) {
		final PluginCommand command = Objects.requireNonNull(this.getCommand(name));
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
			final String name,
			final CommandExecutor executor
	) {
		this.registerCommand(name, executor, new EmptyTabCompleter());
	}

}
