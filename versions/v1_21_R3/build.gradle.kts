plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-SNAPSHOT"
}

apply(plugin = "io.papermc.paperweight.userdev")

val paper = "1.21.4-R0.1-20250317.101324-208"
val lombok = "1.18.32"
val jetbrainsAnnotations = "24.1.0"

dependencies {
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:${paper}")

    compileOnly("org.projectlombok:lombok:${lombok}")
    annotationProcessor("org.projectlombok:lombok:${lombok}")
    compileOnly("org.jetbrains:annotations:${jetbrainsAnnotations}")

    implementation(project(":versions:adapter"))
    implementation(project(":utils"))
    implementation(project(":bukkit-utils"))
}
