plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
}

apply(plugin = "io.papermc.paperweight.userdev")

val paper = "1.21.11-R0.1-SNAPSHOT"
val jetbrainsAnnotations = "26.0.2"

dependencies {
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:${paper}")

    compileOnly("org.jetbrains:annotations:${jetbrainsAnnotations}")

    implementation(project(":versions:adapter"))
    implementation(project(":utils"))
    implementation(project(":bukkit-utils"))
}
