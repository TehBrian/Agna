package dev.tehbrian.agna.paper;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

final class VersionComparison {

	private VersionComparison() {
	}

	static @Nullable Boolean isUpdateAvailable(final String currentVersion, final String latestVersion) {
		final @Nullable ComparableVersion current = ComparableVersion.parse(currentVersion);
		final @Nullable ComparableVersion latest = ComparableVersion.parse(latestVersion);

		if (current == null || latest == null) {
			return null;
		}

		return current.compareTo(latest) < 0;
	}

	static @Nullable Integer compare(final String leftVersion, final String rightVersion) {
		final @Nullable ComparableVersion left = ComparableVersion.parse(leftVersion);
		final @Nullable ComparableVersion right = ComparableVersion.parse(rightVersion);

		if (left == null || right == null) {
			return null;
		}

		return left.compareTo(right);
	}

	static boolean isComparable(final String version) {
		return ComparableVersion.parse(version) != null;
	}

	private record ComparableVersion(List<Integer> parts) implements Comparable<ComparableVersion> {

		private static @Nullable ComparableVersion parse(final String rawVersion) {
			final String coreVersion = rawVersion
					.strip()
					.replaceFirst("^[vV]", "")
					.split("-", 2)[0]
					.split("\\+", 2)[0];

			if (coreVersion.isBlank()) {
				return null;
			}

			final String[] rawParts = coreVersion.split("\\.");
			final List<Integer> parts = new ArrayList<>(rawParts.length);
			for (final String rawPart : rawParts) {
				try {
					parts.add(Integer.parseInt(rawPart));
				} catch (final NumberFormatException e) {
					return null;
				}
			}

			return new ComparableVersion(List.copyOf(parts));
		}

		@Override
		public int compareTo(final ComparableVersion other) {
			final int partCount = Math.max(this.parts().size(), other.parts().size());
			for (int i = 0; i < partCount; i++) {
				final int currentPart = this.part(i);
				final int otherPart = other.part(i);
				final int comparison = Integer.compare(currentPart, otherPart);
				if (comparison != 0) {
					return comparison;
				}
			}

			return 0;
		}

		private int part(final int index) {
			if (index >= this.parts().size()) {
				return 0;
			}

			return this.parts().get(index);
		}

	}

}
