package dev.tehbrian.agna.paper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateCheckerTest {

	@Test
	void latestReleaseUsesHighestVersionNumberBeforeDatePublished() {
		final UpdateChecker.ModrinthVersion latestVersion = UpdateChecker.latestReleaseVersion("""
				[
					{
						"id": "older-version-uploaded-later",
						"version_number": "0.9.0",
						"version_type": "release",
						"date_published": "2026-05-22T00:00:00Z"
					},
					{
						"id": "newer-version-uploaded-earlier",
						"version_number": "0.10.0",
						"version_type": "release",
						"date_published": "2026-05-21T00:00:00Z"
					}
				]
				""");

		assertEquals("newer-version-uploaded-earlier", latestVersion.id());
		assertEquals("0.10.0", latestVersion.versionNumber());
	}

	@Test
	void latestReleaseSkipsUnparsableVersionNumbers() {
		final UpdateChecker.ModrinthVersion latestVersion = UpdateChecker.latestReleaseVersion("""
				[
					{
						"id": "unparsable-version-uploaded-later",
						"version_number": "latest",
						"version_type": "release",
						"date_published": "2026-05-22T00:00:00Z"
					},
					{
						"id": "parseable-version-uploaded-earlier",
						"version_number": "0.10.0",
						"version_type": "release",
						"date_published": "2026-05-21T00:00:00Z"
					}
				]
				""");

		assertEquals("parseable-version-uploaded-earlier", latestVersion.id());
		assertEquals("0.10.0", latestVersion.versionNumber());
	}

}
