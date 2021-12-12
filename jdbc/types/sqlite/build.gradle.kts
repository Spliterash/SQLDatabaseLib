import ru.spliterash.database.gradle.DependenciesSet.JDBC_SQLITE

val current = project

subprojects {
    dependencies {
//        runtimeOnly(JDBC_SQLITE)
        implementation(current)
    }
}