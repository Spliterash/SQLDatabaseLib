plugins {
    id("java")
    id("maven-publish")
    id("io.freefair.lombok") version "6.2.0"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")

/*    tasks.generateLombokConfig {
        enabled = false
    }*/


    group = "ru.spliterash.utils"
    version = "1.0.0-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenCentral()
        mavenLocal()

        maven {
            name = "spliterash-group"
            url = uri("https://nexus.spliterash.ru/repository/group/")
        }
    }

    dependencies {
        implementation("org.jetbrains", "annotations", "20.1.0")

        testImplementation(platform("org.junit:junit-bom:5.8.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        // https://mvnrepository.com/artifact/org.assertj/assertj-core
        testImplementation("org.assertj:assertj-core:3.21.0")
        testImplementation("commons-io:commons-io:2.11.0")
    }

    tasks.test {
        useJUnitPlatform()
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


val base: Project = project(":base")

// Все проекты имеют доступ к base
(subprojects.toMutableList() - base).forEach {
    it.dependencies {
        implementation(base)
    }
}


// Настройки деплоя

val projectRepoFolder: String = rootProject.projectDir.absolutePath + "\\repo"

//for (def repo in project.repositories) {
//    repo.
//}


subprojects {
    apply(plugin = "maven-publish")
    apply(uri("https://gradle.spliterash.ru/group-id-fix.gradle"))
    apply(uri("https://gradle.spliterash.ru/maven-publish.gradle"))

    java {
        withSourcesJar()
    }

    val mavenGroupId = rootProject.group.toString() + "." + rootProject.name;
    ext["mavenGroupId"] = mavenGroupId



    afterEvaluate {
        if (tasks.jar.get().enabled) {
            publishing {
                publications {
                    create<MavenPublication>("maven") {
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
                        url = uri("https://nexus.spliterash.ru/repository/libs")
                        credentials {
                            username = project.property("splitNexusUser") as String
                            password = project.property("splitNexusPassword") as String
                        }
                    }

                    /*    maven {
                            name = "local"
                            url = uri("file://$projectRepoFolder")
                        }*/

                }
            }
        }
    }
}