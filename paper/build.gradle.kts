plugins {
	id("agna.java-conventions")
}

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":agna-configurate"))
	compileOnly("io.papermc.paper:paper-api:26.1.2.build.64-stable")
}
