val annotations = "26.0.1"
val gson = "2.13.2"

dependencies {
    implementation("org.jetbrains:annotations:${annotations}")
    implementation(project(":objects"))
    implementation(project(":utils"))

    implementation("com.google.code.gson:gson:${gson}")

    testImplementation("org.jetbrains:annotations:${annotations}")
}
