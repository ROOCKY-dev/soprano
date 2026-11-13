# Installation

This port installs as a NeoForge mod for Minecraft 1.21.1 (NeoForge 21.1.265, Java 21). The other loader projects are retained from upstream but are disabled by default and are not validated on this branch. Download the jar for your Minecraft version and loader from the [releases page](https://github.com/AverWasTaken/soprano/releases) and put it in your `mods` folder. If you had Baritone installed, take it out first. Soprano replaces it, and the two will fight over the same classes.

Each branch of the repo is one Minecraft version, and each Minecraft version has its own release line (Soprano 1.1.x is Minecraft 26.3, 1.0.x is Minecraft 1.21.4, this port uses 1.3.x for Minecraft 1.21.1), tagged like `soprano-v1.1.0`. `26.3` is the newest Minecraft version, and this `1.21.1-latest` branch backports the latest `26.3` source to 1.21.1. The 1.21.1 port must currently be built from source.

Once it's installed, see [the usage page](USAGE.md).

## Which jar

Every release has three flavors:

- **api**: only the code outside `baritone.api` is obfuscated. This is the one most people want, and the one to use if another mod has a Baritone integration.
- **standalone**: everything is obfuscated. Other mods can't use the API, but you get a bit of extra performance.
- **unoptimized**: nothing is obfuscated. Use it when you're reporting a bug, so the stack traces are readable.

And each flavor comes for:

- **Fabric / Forge / NeoForge**: loads as a normal mod with that loader. The Fabric build may or may not work on Quilt.
- **No loader** (`tweaker`): loads as a launchwrapper tweaker against vanilla Minecraft using a custom `version.json`. You probably don't want this one.

The jars are named `soprano-<flavor>-<loader>-<version>.jar`. Mod ids and the `baritone` settings folder keep their old names on purpose, so mods and configs that expect Baritone don't notice the swap.

# Building it yourself

Clone the repo, and check out the branch for the Minecraft version you want (one branch per version).

## Java

You need the right Java version for the Minecraft version you're building. [Download Java here](https://adoptium.net/) and check what you're using with `java -version`. 26.3 needs Java 25; this 1.21.1 backport and 1.21.4 need Java 21. `java_version` in `gradle.properties` on a branch says which Java that branch wants.

## Command line

On Windows use `gradlew`, on Mac and Linux use `./gradlew`.

`./gradlew build` builds everything, and the finished jars end up in the `dist` directory at the root of the repo, along with `checksums.txt`. There are also mapping files in `dist` if you need to read an obfuscated stack trace.

To start a dev client with Soprano loaded, use `./gradlew :neoforge:runClient`. Tests are `./gradlew test`.

## IntelliJ

- Open the project in IntelliJ as a Gradle project
- Refresh the Gradle project (or, to be safe, just restart IntelliJ)

## GitHub Actions

Pushes and pull requests run [the CI workflow](.github/workflows/gradle_build.yml) and upload the jars as a build artifact. If you fork the repo and enable actions, you can push a commit to your fork and have GitHub build it for you.
