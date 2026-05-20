package dev.tehbrian.agna.paper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

/**
 * Returns an empty list upon tab completion.
 */
public final class EmptyTabCompleter implements TabCompleter {
	@Override
	public List<String> onTabComplete(
			final CommandSender sender,
			final Command command,
			final String alias,
			final String[] args
	) {
		return Collections.emptyList();
	}
}
