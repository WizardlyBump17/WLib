# WLib
[![wakatime](https://wakatime.com/badge/user/6e86b908-7bfd-4cb9-9128-8fa18c67e54a/project/6cd04190-9757-4fc7-be89-b5dfd1ae912f.svg)](https://wakatime.com/badge/user/6e86b908-7bfd-4cb9-9128-8fa18c67e54a/project/6cd04190-9757-4fc7-be89-b5dfd1ae912f)

<br>

### Build yourself

### Linux
### Pre-requisites
1. Git installed;
2. JDK 16 installed (JDK 21 for branches related to 1.20.6+);
3. BuildTools ran for all Minecraft versions of the branch you are on;
    1. If you are on the "1.20.6+" branch, you don't need to do that;
    2. This can be automated by running the `scripts/buildtools/build-spigot.sh` script;
        1. For this approach, you will need to have installed:
           1. [SDKMAN!](https://sdkman.io/);
           2. curl.
### Building
1. Run `./gradlew build`;
2. The .jar to use on the Spigot (or Paper) server will be located at `core/build/libs/WLib-<version>.jar`;
3. The .jar to use on BungeeCord will be located at `bungee/build/libs/WLib-BungeeCord-<version>.jar`.

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