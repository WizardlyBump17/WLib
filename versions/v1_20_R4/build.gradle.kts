val spigot = "1.20.6-R0.1-SNAPSHOT"
val lombok = "1.18.32"
val jetbrainsAnnotations = "24.1.0"

dependencies {
    compileOnly("org.spigotmc:spigot:${spigot}")

    compileOnly("org.projectlombok:lombok:${lombok}")
    annotationProcessor("org.projectlombok:lombok:${lombok}")
    compileOnly("org.jetbrains:annotations:${jetbrainsAnnotations}")

    implementation(project(":versions:adapter"))
    implementation(project(":utils"))
    implementation(project(":bukkit-utils"))
}
