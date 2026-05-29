plugins {
	id("agna.java-conventions")
}

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":agna-configurate"))
	compileOnly("com.google.code.gson:gson:2.14.0")
	compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
	testImplementation(platform("org.junit:junit-bom:6.1.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("com.google.code.gson:gson:2.14.0")
	testRuntimeOnly("io.papermc.paper:paper-api:26.1.2.build.65-stable")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
