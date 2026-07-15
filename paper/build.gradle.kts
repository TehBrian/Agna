plugins {
	id("agna.java-conventions")
}

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	compileOnly("com.google.code.gson:gson:2.14.0")
	testRuntimeOnly("com.google.code.gson:gson:2.14.0")

	testImplementation(platform("org.junit:junit-bom:6.1.2"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
