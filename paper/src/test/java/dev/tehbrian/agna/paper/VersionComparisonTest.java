package dev.tehbrian.agna.paper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VersionComparisonTest {

	@Test
	void olderCurrentVersionHasUpdateAvailable() {
		assertEquals(true, VersionComparison.isUpdateAvailable("0.9.9", "0.10.0"));
	}

	@Test
	void sameVersionDoesNotHaveUpdateAvailable() {
		assertEquals(false, VersionComparison.isUpdateAvailable("0.10.0", "0.10.0"));
	}

	@Test
	void newerCurrentVersionDoesNotHaveUpdateAvailable() {
		assertEquals(false, VersionComparison.isUpdateAvailable("0.11.0", "0.10.0"));
	}

	@Test
	void missingPatchVersionsCompareAsZero() {
		assertEquals(false, VersionComparison.isUpdateAvailable("0.10", "0.10.0"));
		assertEquals(true, VersionComparison.isUpdateAvailable("0.10", "0.10.1"));
	}

	@Test
	void versionPrefixIsIgnored() {
		assertEquals(false, VersionComparison.isUpdateAvailable("v0.10.0", "0.10.0"));
		assertEquals(true, VersionComparison.isUpdateAvailable("v0.9.9", "0.10.0"));
	}

	@Test
	void prereleaseAndBuildMetadataAreIgnored() {
		assertEquals(false, VersionComparison.isUpdateAvailable("0.10.0-SNAPSHOT", "0.10.0"));
		assertEquals(false, VersionComparison.isUpdateAvailable("0.10.0+build.1", "0.10.0"));
	}

	@Test
	void unparsableVersionsReturnNull() {
		assertNull(VersionComparison.isUpdateAvailable("0.10.x", "0.10.0"));
		assertNull(VersionComparison.isUpdateAvailable("0.10.0", "latest"));
		assertNull(VersionComparison.isUpdateAvailable("", "0.10.0"));
	}

	@Test
	void comparableVersionsCanBeOrderedDirectly() {
		assertEquals(1, VersionComparison.compare("0.10.0", "0.9.9"));
		assertEquals(0, VersionComparison.compare("0.10", "0.10.0"));
		assertEquals(-1, VersionComparison.compare("0.10.0", "0.10.1"));
	}

}
