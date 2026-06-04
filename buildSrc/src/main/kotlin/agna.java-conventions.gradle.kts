plugins {
	`java-library`
	id("net.kyori.indra")
	id("net.kyori.indra.checkstyle")
	id("net.kyori.indra.publishing")
}

group = rootProject.group
version = rootProject.version
description = rootProject.description

dependencies {
	compileOnly("org.jspecify:jspecify:1.0.0")
}

indra {
	javaVersions {
		target(25)
	}

	github("TehBrian", "Agna")

	mitLicense()

	publishReleasesTo("tehbrian", "https://repo.tehbrian.dev/releases")
	publishSnapshotsTo("tehbrian", "https://repo.tehbrian.dev/snapshots")
	signWithKeyFromProperties("signingKey", "signingPassword")

	configurePublications {
		pom {
			url.set("github.com/TehBrian/Agna")

			developers {
				developer {
					id.set("TehBrian")
					url.set("https://tehbrian.dev")
					email.set("tehbrian@proton.me")
				}
			}

			scm {
				connection.set("scm:git:git://github.com/TehBrian/Agna.git")
				developerConnection.set("scm:git:ssh://github.com/TehBrian/Agna.git")
				url.set("github.com/TehBrian/Agna.git")
			}
		}
	}
}
