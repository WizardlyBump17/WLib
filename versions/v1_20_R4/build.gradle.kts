plugins {
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

apply(plugin = "io.papermc.paperweight.userdev")

val paper = "1.20.6-R0.1-20240702.153951-123"
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
