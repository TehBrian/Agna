plugins {
	id("agna.java-conventions")
}

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":agna-configurate"))
	compileOnly("com.google.code.gson:gson:2.13.2")
	compileOnly("io.papermc.paper:paper-api:26.1.2.build.64-stable")
	testImplementation(platform("org.junit:junit-bom:5.13.4"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("com.google.code.gson:gson:2.13.2")
	testRuntimeOnly("io.papermc.paper:paper-api:26.1.2.build.64-stable")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
