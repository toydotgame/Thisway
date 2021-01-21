# Thisway
A simple teleportation plugin for Spigot servers on 1.6.4.

<div align="center">
	<img alt="GitHub all releases" src="https://img.shields.io/github/downloads/toydotgame/Thisway/total">
	<img alt="GitHub release (latest by date including pre-releases)" src="https://img.shields.io/github/v/release/toydotgame/Thisway?include_prereleases">
	<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/toydotgame/Thisway"><br> <!-- I put a `<br>` because I wanted the repo. social details a bit more seperate. -->
	<img alt="GitHub forks" src="https://img.shields.io/github/forks/toydotgame/Thisway">
	<img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/toydotgame/Thisway">
</div>

## [Usage](https://github.com/Toydotgame/Thisway/wiki/How-to-use-Thisway), [Features](https://github.com/Toydotgame/Thisway/wiki/Features-of-Thisway), [Building, Contributing](https://github.com/Toydotgame/Thisway/wiki/How-to-set-up-the-Source-Code-in-Your-Editor-and-Build), [Command Output](https://github.com/Toydotgame/Thisway/wiki/Outputs), and other Details
All of the above can be found on the [wiki](https://github.com/Toydotgame/Thisway/wiki).

## Version
Latest versions always come first to the [Releases](https://github.com/Toydotgame/Thisway/releases) tab.
Versions use [Semantic Versioning](https://semver.org/).
This plugin will _always_ be for Minecraft Release 1.6.4. I am not updating it to 1.12 or 1.16 or whatever you want. I like 1.6 for many reasons, and I want to help it by making more plugins for it; which is a big problem I've encoutered whilst playing this version.

## Changelog
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
