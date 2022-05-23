val jdbc = project

tasks.jar {
    enabled = false
}

subprojects {
    dependencies {
        implementation(project(":jdbc"))
    }

    var artifactName: String? = null;
    var lastProject: Project = project;

    while (lastProject != jdbc) {
        if (project.parent == jdbc)
            artifactName = project.name + "-abstract"
        else if (artifactName != null)
            artifactName = lastProject.name + "-" + artifactName;
        else
            artifactName = lastProject.name

        lastProject = lastProject.parent!!
    }
    ext["mavenArtifactId"] = rootProject.name + "-" + artifactName
}