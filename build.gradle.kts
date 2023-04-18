plugins {
    id("java-library")
    id("maven-publish")
    id("io.freefair.lombok") version "6.5.1"
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.freefair.lombok")

    version = "1.0.1"

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        compileOnly("org.jetbrains", "annotations", "20.1.0")

        testImplementation(platform("org.junit:junit-bom:5.8.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        // https://mvnrepository.com/artifact/org.assertj/assertj-core
        testImplementation("org.assertj:assertj-core:3.21.0")
        testImplementation("commons-io:commons-io:2.11.0")
    }

    tasks.test {
        useJUnitPlatform()
        enabled = false
    }

    ext {
        set("hikari", {
            dependencies {
                // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
                implementation("com.zaxxer:HikariCP:5.0.0")
            }
        });
    }

}

tasks.jar {
    enabled = false
}


val baseProject = project(":base")

// Все проекты имеют доступ к base
configure(subprojects - baseProject) {
    dependencies {
        api(baseProject)
    }
}


// Настройки деплоя

val projectRepoFolder: String = rootProject.projectDir.absolutePath + "\\repo"

subprojects {
    apply(plugin = "maven-publish")


    if (!project.file("src").isDirectory) {
        tasks.jar {
            enabled = false
        }
        return@subprojects
    }

    java {
        withSourcesJar()
    }
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "ru.spliterash"
                artifactId = rootProject.name + "-" + project.name

                pom {
                    description.set("Simple library to work better with java JDBC database")
                    url.set("https://github.com/Spliterash/SQLDatabaseLib.git")
                }

                from(components["java"])
            }
        }

        repositories {
            maven {
                name = "nexus"
                url = uri("https://repo.spliterash.ru/sql-database")
                credentials {
                    username = project.property("SPLITERASH_NEXUS_USR") as String
                    password = project.property("SPLITERASH_NEXUS_PSW") as String
                }
            }

            maven {
                name = "local"
                url = uri("file://$projectRepoFolder")
            }
        }
    }
}