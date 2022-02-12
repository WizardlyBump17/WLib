# WLib
[![wakatime](https://wakatime.com/badge/user/6e86b908-7bfd-4cb9-9128-8fa18c67e54a/project/6cd04190-9757-4fc7-be89-b5dfd1ae912f.svg)](https://wakatime.com/badge/user/6e86b908-7bfd-4cb9-9128-8fa18c67e54a/project/6cd04190-9757-4fc7-be89-b5dfd1ae912f)

<br>

### Build yourself

#### Pre-requisites

1. Git installed
2. Gradle installed
3. Java 8, for the 1.8.8 - 1.16.5 Minecraft versions, Java 16 for 1.17 and Java 17 for 1.18
4. [BuildTools](https://www.spigotmc.org/wiki/buildtools/) run for the versions: 1.8.8, 1.12.2, 1.13.2, 1.15.2, 1.16.5, 1.17.1 and 1.18

#### Building

1. Clone the repository
2. Enter the directory
3. Run `gradle shadowJar`

<br>

### Add as dependency
Follow [the Gradle instructions](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry) or [the Maven instructions](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry) <br>
(Soon I will show how to do it)
<br>

Then add the dependency. Replace MODULE with a module and VERSION with a version<br>
Maven:
```xml
<dependencies>
    <dependency>
        <groupId>com.wizardlybump17.wlib</groupId>
        <artifactId>MODULE</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
Gradle:
```groovy
dependencies {
    implementation('com.wizardlybump17.wlib:MODULE:VERSION')
}
```
<br>

To add it into your plugin, add the following at your `plugin.yml`
```yaml
softdepend: [WLib]
```