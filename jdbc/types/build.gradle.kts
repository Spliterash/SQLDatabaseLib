val jdbc = project

subprojects {
    dependencies {
        api(project(":jdbc"))
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

    publishing.publications.withType(MavenPublication::class) {
        artifactId = rootProject.name + "-" + artifactName
    }
}