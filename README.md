# WLib

[![](https://jitpack.io/v/WizardlyBump17/WLib.svg)](https://jitpack.io/#WizardlyBump17/WLib)
<br>
<br>

### Build yourself

#### Pre-requisites

1. Git installed
2. Gradle installed
3. Java 8+ installed
4. [BuildTools](https://www.spigotmc.org/wiki/buildtools/) run for the versions: 1.8.8, 1.12.2, 1.13.2, 1.15.2, 1.16.5, 1.17.1

#### Building

1. Clone the repository
2. Enter the directory
3. Run `gradle shadowJar`

<br>

### Add as dependency
Add the JitPack repository.<br>
Maven:
```xml
<repositories>
    <repository>
        <id>jitpack</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Gradle:
```groovy
repositories {
    maven {
        url('https://jitpack.io')
    }
}
```
<br>

Then add the dependency. Replace MODULE with a module and VERSION with a version<br>
Maven:
```xml
<dependencies>
    <dependency>
        <groupId>com.github.WizardlyBump17.WLib</groupId>
        <artifactId>MODULE</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
Gradle:
```groovy
dependencies {
    implementation('com.github.WizardlyBump17.WLib:MODULE:VERSION')
}
```
<br>

To add it into your plugin, add the following at your `plugin.yml`
```yaml
softdepend: [WLib]
```