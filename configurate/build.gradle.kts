plugins {
	id("agna.java-conventions")
}

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	compileOnly("org.spongepowered:configurate-core:4.2.0")
	testRuntimeOnly("org.spongepowered:configurate-core:4.2.0")
}
