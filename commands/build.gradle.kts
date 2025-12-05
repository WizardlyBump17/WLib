val annotations = "26.0.1"

dependencies {
    implementation("org.jetbrains:annotations:${annotations}")
    implementation(project(":objects"))
    implementation(project(":utils"))

    testImplementation("org.jetbrains:annotations:${annotations}")
}
