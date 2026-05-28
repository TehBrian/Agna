package dev.tehbrian.agna.paper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class UpdateChecker {

	private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

	private static final long MIN_DELAY_SECONDS = 5;
	private static final long MAX_DELAY_SECONDS = 60;

	private final JavaPlugin plugin;
	private final String modrinthProjectSlug;
	private final HttpClient httpClient;

	public UpdateChecker(
			final JavaPlugin plugin,
			final String modrinthProjectSlug
	) {
		this.plugin = plugin;
		this.modrinthProjectSlug = modrinthProjectSlug;
		this.httpClient = HttpClient.newBuilder()
				.connectTimeout(REQUEST_TIMEOUT)
				.build();
	}

	public void checkForUpdates() {
		final String currentVersion = this.plugin.getPluginMeta().getVersion();
		final String minecraftVersion = this.plugin.getServer().getMinecraftVersion();
		final String pluginName = this.plugin.getPluginMeta().getName();
		final Logger logger = this.plugin.getSLF4JLogger();

		final long delay = ThreadLocalRandom.current().nextLong(MIN_DELAY_SECONDS, MAX_DELAY_SECONDS + 1);

		this.plugin.getServer().getAsyncScheduler().runDelayed(
				this.plugin,
				(_) -> this.checkForUpdatesNow(currentVersion, minecraftVersion, pluginName, logger),
				delay,
				TimeUnit.SECONDS
		);
	}

	private void checkForUpdatesNow(
			final String currentVersion,
			final String minecraftVersion,
			final String pluginName,
			final Logger logger
	) {
		final HttpRequest request = HttpRequest.newBuilder(this.modrinthVersionsUri(minecraftVersion))
				.header("User-Agent", "tehbrian/" + this.modrinthProjectSlug + "/" + currentVersion)
				.timeout(REQUEST_TIMEOUT)
				.GET()
				.build();

		try {
			final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				logger.info(
						"Unable to check for updates: HTTP {}",
						response.statusCode()
				);
				return;
			}

			final @Nullable ModrinthVersion latestVersion = latestReleaseVersion(response.body());
			if (latestVersion == null) {
				logger.info(
						"Unable to check for updates: Modrinth did not return any Paper versions for Minecraft {}",
						minecraftVersion
				);
				return;
			}

			final @Nullable Boolean updateAvailable = VersionComparison.isUpdateAvailable(
					currentVersion,
					latestVersion.versionNumber()
			);
			if (updateAvailable == null) {
				logger.info(
						"Unable to check for updates: Cannot compare current version {} with latest version {}",
						currentVersion,
						latestVersion.versionNumber()
				);
				return;
			}

			if (updateAvailable) {
				logger.info(
						"An update is available: {} {} -> {}",
						pluginName,
						currentVersion,
						latestVersion.versionNumber()
				);
				logger.info(
						"Download it at {}/version/{}",
						this.modrinthProjectUri(),
						latestVersion.id()
				);
			}
		} catch (final IOException | InterruptedException | JsonParseException | IllegalStateException | UnsupportedOperationException e) {
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			logger.info("Unable to check for updates", e);
		}
	}

	private URI modrinthProjectUri() {
		return URI.create("https://modrinth.com/plugin/" + this.modrinthProjectSlug);
	}

	private URI modrinthVersionsUri(final String minecraftVersion) {
		return URI.create(
				"https://api.modrinth.com/v2/project/" + this.modrinthProjectSlug + "/version"
						+ "?loaders=[%22paper%22]"
						+ "&game_versions=[%22" + minecraftVersion + "%22]"
						+ "&include_changelog=false"
		);
	}

	static @Nullable ModrinthVersion latestReleaseVersion(final String responseBody) {
		final JsonArray versions = JsonParser.parseString(responseBody).getAsJsonArray();
		@Nullable ModrinthVersion latestVersion = null;

		for (final JsonElement versionElement : versions) {
			final JsonObject version = versionElement.getAsJsonObject();
			final @Nullable String id = stringMember(version, "id");
			final @Nullable String versionNumber = stringMember(version, "version_number");
			final @Nullable String datePublishedRaw = stringMember(version, "date_published");
			if (id == null || versionNumber == null || datePublishedRaw == null) {
				continue;
			}

			if (!"release".equals(stringMember(version, "version_type"))) {
				continue;
			}

			if (!VersionComparison.isComparable(versionNumber)) {
				continue;
			}

			final Instant datePublished;
			try {
				datePublished = Instant.parse(datePublishedRaw);
			} catch (final DateTimeParseException e) {
				continue;
			}

			final ModrinthVersion modrinthVersion = new ModrinthVersion(id, versionNumber, datePublished);
			if (latestVersion == null || isNewerVersion(modrinthVersion, latestVersion)) {
				latestVersion = modrinthVersion;
			}
		}

		return latestVersion;
	}

	private static boolean isNewerVersion(final ModrinthVersion candidate, final ModrinthVersion currentLatest) {
		final @Nullable Integer comparison = VersionComparison.compare(
				candidate.versionNumber(),
				currentLatest.versionNumber()
		);

		if (comparison == null) {
			return false;
		}

		if (comparison != 0) {
			return comparison > 0;
		}

		return candidate.datePublished().isAfter(currentLatest.datePublished());
	}

	private static @Nullable String stringMember(final JsonObject object, final String memberName) {
		if (!object.has(memberName) || object.get(memberName).isJsonNull()) {
			return null;
		}

		return object.get(memberName).getAsString();
	}

	record ModrinthVersion(String id, String versionNumber, Instant datePublished) {
	}

}
