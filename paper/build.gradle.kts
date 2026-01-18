plugins {
	id("agna.java-conventions")
}

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":agna-configurate"))
	compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
}
