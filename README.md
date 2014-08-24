Downloads
=========

* Minecraft Forums: http://www.minecraftforum.net/topic/2637187-
* Jenkins (Always has latest build): http://server.techjargaming.com:8080/job/HexWool/

Compiling
=========

You need the dependencies, see build.gradle for those. Place them in the "dependencies" directory (create it) in your ForgeGradle directory. Once you've done that, you can either put the mod's source in the ForgeGradle directly and build it by running gradlew, or you can use the provided build script by creating a build.properties (example included) and setting up your directory structure as follows.

```
-root
  -forge (This is your ForgeGradle directory)
  -source
    -HexWool
  -build
    -HexWool
```