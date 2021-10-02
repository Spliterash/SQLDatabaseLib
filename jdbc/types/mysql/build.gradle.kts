import ru.spliterash.database.gradle.DependenciesSet.JDBC_MYSQL

val current = project

subprojects {
    dependencies {
        runtimeOnly(JDBC_MYSQL)
        implementation(current)
    }
}