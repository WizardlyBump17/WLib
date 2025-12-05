val annotations = "26.0.1"

dependencies {
    compileOnly("org.jetbrains:annotations:${annotations}")
    implementation(project(":objects"))
    implementation(project(":utils"))

    testCompileOnly("org.jetbrains:annotations:${annotations}")
}
