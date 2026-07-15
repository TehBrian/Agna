package dev.tehbrian.agna.configurate;

import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ConfigLoader {
	private final JavaPlugin plugin;

	public ConfigLoader(final JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Loads the given list of configuration.
	 *
	 * <p>If there is an error while loading a config file, the exception is logged
	 * and the file is skipped.</p>
	 *
	 * @return whether all config files were successfully loaded
	 */
	public boolean load(final List<Loadable> toLoad) {
		boolean successful = true;
		for (final Loadable data : toLoad) {
			final Path dataPath = this.plugin.getDataPath();
			final Path savePath = dataPath.resolve(data.filename());

			if (!Files.exists(savePath)) {
				this.plugin.saveResource(data.filename(), false);
			}

			final Config<?> config = data.config();
			if (!this.loadConfig(config)) {
				successful = false;
				continue;
			}

			if (!data.versioned()) {
				// skip version check.
				continue;
			}

			final int loadedVersion = config.wrapper().rootNode().node("version").getInt();
			if (loadedVersion != data.version()) {
				this.plugin.getSLF4JLogger().warn(
						"Config {} is versioned {}, but the current version is {}",
						data.filename(), loadedVersion, data.version()
				);

				int tag = -1;
				String archiveFilename;
				Path archivePath;
				do {
					tag += 1;
					archiveFilename = data.filenameBase() + ".old-" + tag + "." + data.filenameExt();
					archivePath = dataPath.resolve(archiveFilename);
				} while (Files.exists(archivePath));

				this.plugin.getSLF4JLogger().warn(
						"Regenerating {} and archiving the old config as {}",
						data.filename(), archiveFilename
				);

				try {
					Files.move(savePath, archivePath);
				} catch (final IOException e) {
					this.plugin.getSLF4JLogger().error("Failed to move the old config");
					this.plugin.getSLF4JLogger().error("Printing stack trace", e);
					successful = false;
					continue;
				}

				this.plugin.saveResource(data.filename(), false);

				if (!this.loadConfig(config)) {
					successful = false;
					continue;
				}
			}
		}

		if (successful) {
			this.plugin.getSLF4JLogger().info("Successfully loaded configuration");
		}
		return successful;
	}

	private boolean loadConfig(final Config<?> config) {
		try {
			config.load();
			return true;
		} catch (final ConfigurateException e) {
			this.plugin.getSLF4JLogger().error(
					"Failed to load config file {}",
					config.wrapper().path().getFileName()
			);
			this.plugin.getSLF4JLogger().error("Please ensure that the config is valid");
			this.plugin.getSLF4JLogger().error("Printing stack trace", e);
			return false;
		}
	}

	public static final class Loadable {
		private final String filenameBase;
		private final String filenameExt;
		private final Config<?> config;
		private final int version;

		private Loadable(
				final String filenameBase, final String filenameExt,
				final Config<?> config, final int version
		) {
			this.filenameBase = filenameBase;
			this.filenameExt = filenameExt;
			this.config = config;
			this.version = version;
		}

		public static Loadable of(final String filename, final Config<?> config) {
			return ofVersioned(filename, config, -1);
		}

		public static Loadable ofVersioned(final String filename, final Config<?> config, final int version) {
			final int lastDot = filename.lastIndexOf('.');
			if (lastDot == -1) {
				throw new IllegalArgumentException("Filename must contain a dot");
			}
			return new Loadable(filename.substring(0, lastDot), filename.substring(lastDot + 1), config, version);
		}

		public String filenameBase() {
			return this.filenameBase;
		}

		public String filenameExt() {
			return this.filenameExt;
		}

		public Config<?> config() {
			return this.config;
		}

		public int version() {
			return this.version;
		}

		public String filename() {
			return this.filenameBase() + "." + this.filenameExt();
		}

		public boolean versioned() {
			return this.version() >= 0; // versions *may* start at 0.
		}
	}
}
