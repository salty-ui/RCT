# RCT Right-Click Repeater

A Fabric mod for **Minecraft 1.21.11**. Hold the right-click (use item) button
and it keeps right-clicking for you, waiting a random delay between two times
you set (in milliseconds), like a repeater re-triggering itself.

## How it works

- Hold right-click (the vanilla "use item" key) → the mod fires a right-click
  action, waits a random time between `min` and `max` ms, then fires again,
  for as long as you keep holding the button.
- `/rct <min> <max>` sets that range. Example: `/rct 60 70` makes it wait a
  random 60–70ms between each simulated click.
- Default range if you never run the command: **60–70ms**.
- It pauses automatically if you open any screen (inventory, chat, pause
  menu, etc.) or if you release the button.

**Note:** some multiplayer servers ban autoclickers/macros in their rules —
check before using this on someone else's server. It's fine in singleplayer
or on servers that allow it.

## Project layout

```
rct-autoclicker/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── src/main/
    ├── resources/
    │   ├── fabric.mod.json
    │   └── rct.mixins.json
    └── java/net/rct/autoclicker/
        ├── RightClickRepeaterClient.java   (client entrypoint, /rct command)
        ├── RCTConfig.java                  (holds current min/max ms)
        ├── ClickerThread.java              (the repeater loop)
        └── mixin/MinecraftClientAccessor.java (exposes doItemUse())
```

## Building it — no installs needed (recommended)

This project includes a GitHub Actions workflow (`.github/workflows/build.yml`)
that builds the mod on GitHub's own servers. You only need a free GitHub
account and a web browser:

1. Go to https://github.com/new, create a new repository (any name, Public
   or Private, doesn't matter). Don't add a README/gitignore — leave it empty.
2. On the new repo's page, click **"uploading an existing file"**.
3. Drag the *entire contents* of this unzipped `rct-autoclicker` folder into
   the upload box (all the files and folders — `build.gradle`,
   `settings.gradle`, `gradle.properties`, `src/`, `.github/`, `README.md`).
   Your browser will preserve the folder structure.
4. Scroll down and click **"Commit changes"**.
5. Click the **"Actions"** tab at the top of the repo. You'll see a workflow
   run start automatically (it may take a minute to appear — refresh if
   needed).
6. Click into the running/finished job. Once it shows a green checkmark
   (takes a few minutes), scroll to the bottom **"Artifacts"** section and
   click **`rct-autoclicker-jar`** to download it as a zip.
7. Unzip that download — inside is `rct-autoclicker-1.0.0.jar`, your actual
   finished mod. Drop it into `.minecraft/mods` along with Fabric API.

If the Actions run shows a red X instead of green, click into it, open the
"Build mod" step, and copy the error text to me — it's almost always a
one-line version fix in `gradle.properties`.

## Building it locally

You'll need:
- **JDK 21**
- An internet connection (Gradle needs to download Minecraft, the Yarn
  mappings, Fabric Loader, and Fabric API the first time)

The easiest path is **IntelliJ IDEA** with the standard Fabric setup:

1. Open this folder in IntelliJ IDEA as a Gradle project (`File → Open`,
   select the `rct-autoclicker` folder). IntelliJ will generate the Gradle
   wrapper for you and import the project automatically.
2. Wait for Gradle to sync (first sync downloads Minecraft/mappings/Fabric
   API — can take a few minutes).
3. Run the Gradle task **`build`** (View → Tool Windows → Gradle →
   Tasks → build → `build`), or from a terminal in the project folder:
   ```
   ./gradlew build
   ```
4. The finished mod jar will be in `build/libs/rct-autoclicker-1.0.0.jar`.

If you'd rather use the command line without IntelliJ, you need Gradle
installed locally, then from inside the project folder run:
```
gradle wrapper --gradle-version 8.14
./gradlew build
```

## Installing

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.11.
2. Download **Fabric API** for 1.21.11 from Modrinth/CurseForge and drop it
   in your `.minecraft/mods` folder.
3. Drop `rct-autoclicker-1.0.0.jar` (from `build/libs/`) into the same
   `mods` folder.
4. Launch the game with the Fabric profile.

## Using it

- Just hold right-click anywhere in-game (holding a block, item, on an
  entity, etc. — anywhere a normal right-click would work).
- Run `/rct 60 70` (or whatever numbers you want) at any time to change the
  delay range. It applies immediately, even mid-hold.

## Versions used

| Component      | Version               |
|-----------------|------------------------|
| Minecraft       | 1.21.11                |
| Yarn mappings   | 1.21.11+build.4         |
| Fabric Loader   | 0.18.4                  |
| Fabric API      | 0.141.1+1.21.11         |
| Fabric Loom     | 1.14                    |

If any of these have moved on by the time you build this, check
https://fabricmc.net/develop/ for the current numbers and update
`gradle.properties` accordingly — everything else in the project will keep
working as-is.
