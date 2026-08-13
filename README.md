# Alchemy Redirected

Empty Paper plugin project, targeting **Paper API 26.2** (Minecraft, Java 25 toolchain).

## Structure

```
alchemy-redirected/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── src/main/
    ├── java/dev/alchemyredirected/alchemyredirected/AlchemyRedirected.java
    └── resources/plugin.yml
```

## Getting started

1. Open the project folder in IntelliJ IDEA (or your IDE of choice) as a Gradle project.
2. This project doesn't include the Gradle wrapper jar (it couldn't be fetched in the environment
   that generated it). Generate it yourself with a local Gradle install:
   ```
   gradle wrapper --gradle-version 8.11
   ```
   After that you can use `./gradlew build` as usual.
3. Build the plugin jar:
   ```
   ./gradlew build
   ```
   The output jar will be in `build/libs/alchemy-redirected-1.0-SNAPSHOT.jar`.
4. Drop the jar into your Paper server's `plugins/` folder and start the server.

## Notes

- Main class: `dev.alchemyredirected.AlchemyRedirected`
- `paper-api` is a `compileOnly` dependency — don't shade/relocate it, it's provided by the server at runtime.
- Update `group`/package name in `build.gradle.kts` and the Java source if you'd like a different namespace.
