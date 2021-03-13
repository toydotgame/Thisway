# Thisway
<div align="center">
	<img alt="GitHub Downloads" src="https://img.shields.io/github/downloads/toydotgame/Thisway/total?label=GitHub%20Downloads">
	<img alt="SpigotMC Downloads" src="https://img.shields.io/spiget/downloads/87115?label=SpigotMC%20Downloads"><br>
	<img alt="GitHub Latest Release Tag" src="https://img.shields.io/github/v/tag/toydotgame/Thisway?label=release">
	<img alt="GitHub Last Commit" src="https://img.shields.io/github/last-commit/toydotgame/Thisway"><br> <!-- I put a `<br>` because I wanted the repo. social details a bit more seperate. -->
	<img alt="GitHub forks" src="https://img.shields.io/github/forks/toydotgame/Thisway">
	<img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/toydotgame/Thisway">
</div>

## [Usage](https://github.com/Toydotgame/Thisway/wiki/How-to-use-Thisway), [Features](https://github.com/Toydotgame/Thisway/wiki/Features-of-Thisway), [Building, Contributing](https://github.com/Toydotgame/Thisway/wiki/How-to-set-up-the-Source-Code-in-Your-Editor-and-Build), [Command Output](https://github.com/Toydotgame/Thisway/wiki/Outputs), and other Details
All of the above can be found on the [wiki](https://github.com/Toydotgame/Thisway/wiki).

## Version
Latest versions always come first to the [Releases](https://github.com/Toydotgame/Thisway/releases) tab.
This plugin was developed primarily for 1.6.4 Spigot. I doubt Thisway will be compatible with Bukkit.
I can only guarantee that Thisway will work for 1.6.4, but any other versions are an added bonus - yet do not expect that I will work on compatibility with these newer versions.

## Changelog
### Release 1.3
* Optimised Debug Mode's scripts for the player's facing direction.
* Plugin loading in the console is more truthful to the actual loading sequence.

### Release 1.2.4
* Repositioned teleport success message in chat when Debug Mode is active.
* (Backend) renamed main package from `io.github.Toydotgame` to `io.github.toydotgame`. Big change, I know.
* (Backend) Thisway is compiled with JRE v1.8, this should allow it to run on out-of-date servers and servers at the bleeding edge of Spigot.
* Thisway now teleports exactly to the same (relative to the block) positions at your destination! (Issue [#3](https://github.com/toydotgame/Thisway/issues/3))
    * Debug Mode shows the same coordinates as you'd see in the F3 screen. (As in coordinates with five decimal places)
    * Server log output use no decimal places.

### Release 1.2.3
* Removed update checker, it sucked
* Cleaned Debug Mode output
* Removed some comments from the code
* Stopped command usage from being printed into console when console uses `/thisway`.

### Release 1.2.2
* Un-crippled update checker and added it to debug output.

### Release 1.2.1
* Semi-crippled update checker to allow plugin to still operate if the plugin says it's out of date. A new update checker has been made and it will make it's way to a `rc` build.

### Release 1.2
#### Completely non-functional build, please use 1.2.1 if you want something close to 1.2.
* Added update checker
	* Console and Debug Mode outputs have been added.
* Added player pitch to Debug Mode output.
	* (I think this did exist before, but it's patched back in now)
* Optimised suffocation detector
* Players can teleport into grass too, it used to not be on the safe block list.
* Stopped crashes when console users use Thisway
* Optimised `DataStorage` class
* Fixed typos in client-sided output

### Release 1.1
* Added a [suffocation detector](https://github.com/Toydotgame/Thisway/wiki/Thisway-Teleporting-Mechanics#tracking-of-players-head-into-a-block).
* \[I\] made it so that a glass block replaces air if your new location is in midair.
* Fixed issue [#3](https://github.com/Toydotgame/Thisway/issues/3). (New location positioning error)
* Added permission system:
   * `thisway.use` - Allows use of `/thisway` \[and `/tw`\].
   * `thisway.debug` - Allows use of Debug Mode. (`/thisway <distance> true`)
   * Fixed bug where non-ops could use Thisway. Now `thisway.use` and `thisway.debug` are defaulted to op-only.

### Release 1.0.1
* Fixed versioning error in `plugin.yml`.
* Cleaned up code.
    * Seperate method for main command actions, leaving the main method in the class to be for syntax error checking.

### Relase 1.0
* Added main set of features:
    * Basic teleporting.
    * Both `/thisway` and `/tw` commands.
    * Debug Mode.
    * Multi world support.
    * Seperate data storage class.
