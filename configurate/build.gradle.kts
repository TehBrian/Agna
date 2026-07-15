plugins {
	id("agna.java-conventions")
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly("org.spongepowered:configurate-core:4.2.0")
	testRuntimeOnly("org.spongepowered:configurate-core:4.2.0")
}
