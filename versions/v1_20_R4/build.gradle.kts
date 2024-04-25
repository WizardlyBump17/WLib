val lombok = "1.18.32"
val jetbrainsAnnotations = "24.1.0"
val spigot = "1.20.5-R0.1-SNAPSHOT"

dependencies {
    compileOnly("org.spigotmc:spigot:${spigot}")
    compileOnly("org.projectlombok:lombok:${lombok}")
    compileOnly("org.jetbrains:annotations:${jetbrainsAnnotations}")
    compileOnly(project(":versions:adapter"))
    compileOnly(project(":utils"))
    compileOnly(project(":bukkit-utils"))
    annotationProcessor("org.projectlombok:lombok:${lombok}")
}