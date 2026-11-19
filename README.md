# Evoked – NeoForge 1.21.1

Port of Evoked 2.2.1 (Forge 1.19.2) to NeoForge 1.21.1 by Team Taiga. Blueprint is no longer required.

## Building

Needs JDK 21.

    ./gradlew build

The jar is written to `build/libs/`. Pushing to GitHub also builds it automatically:
open the **Actions** tab, pick the latest run, and download the `evoked-neoforge-1.21.1` artifact.

Run a dev client with `./gradlew runClient`.
