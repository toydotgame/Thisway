# Thisway
A simple teleportation plugin for Spigot servers on 1.6.4.

Downloads | ![GitHub all releases](https://img.shields.io/github/downloads/Toydotgame/Thisway/total?color=blue&label=GitHub%20-%20Total%20Downloads) ![GitHub release (latest by date)](https://img.shields.io/github/downloads/Toydotgame/Thisway/latest/total?color=blue&label=GitHub%20-%20Latest%20Version%20Downloads) ![Spiget Downloads](https://img.shields.io/spiget/downloads/87115?color=blue&label=Spigot%20-%20Total%20Downloads)
---- | ----
**Issues** | ![GitHub issues](https://img.shields.io/github/issues-raw/Toydotgame/Thisway?color=red&label=Open%20Issues) ![GitHub closed issues](https://img.shields.io/github/issues-closed-raw/Toydotgame/Thisway?color=green&label=Closed%20Issues)
**Pulls** | ![GitHub pull requests](https://img.shields.io/github/issues-pr-raw/Toydotgame/Thisway?color=blue&label=Open%20Pull%20Requests) ![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed-raw/Toydotgame/Thisway?color=blue&label=Closed%20Pull%20Requests)
**Versions** | ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Toydotgame/Thisway?color=blue&label=Github%20-%20Latest%20%28Stable%20Only%29) ![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/Toydotgame/Thisway?color=blue&include_prereleases&label=GitHub%20-%20Latest) ![Spiget Version](https://img.shields.io/spiget/version/87115?color=blue&label=Spigot%20-%20Latest)
**Repo. Activity** | ![GitHub last commit](https://img.shields.io/github/last-commit/Toydotgame/Thisway?color=blue&label=Latest%20Commit) ![GitHub commits since latest release (by date)](https://img.shields.io/github/commits-since/Toydotgame/Thisway/latest?color=blue&label=Commits%20Since%20Last%20Stable%20Release) ![GitHub commits since latest release (by date including pre-releases)](https://img.shields.io/github/commits-since/Toydotgame/Thisway/latest?color=blue&include_prereleases&label=Commits%20Since%20Latest%20Release%20%28Including%20Unstable%29)
**Spigot Support** | ![Spigot Version](https://img.shields.io/badge/Minecraft%20Spigot%20Server%20Version-r1.6.4-blue)
**Repo. Popularity** | ![GitHub forks](https://img.shields.io/github/forks/Toydotgame/Thisway?color=blue&label=Forks) ![GitHub Repo stars](https://img.shields.io/github/stars/Toydotgame/Thisway?color=blue&label=Stars&style=flat)

## [Usage](https://github.com/Toydotgame/Thisway/wiki/How-to-use-Thisway), [Features](https://github.com/Toydotgame/Thisway/wiki/Features-of-Thisway), [Building, Contributing](https://github.com/Toydotgame/Thisway/wiki/How-to-set-up-the-Source-Code-in-Your-Editor-and-Build), [Command Output](https://github.com/Toydotgame/Thisway/wiki/Outputs), and other Details
All of the above can be found on the [wiki](https://github.com/Toydotgame/Thisway/wiki).

## Version
Latest versions always come first to the [Releases](https://github.com/Toydotgame/Thisway/releases) tab.
Versions use [Semantic Versioning](https://semver.org/).
This plugin will _always_ be for Minecraft Release 1.6.4. I am not updating it to 1.12 or 1.16 or whatever you want. I like 1.6 for many reasons, and I want to help it by making more plugins for it; which is a big problem I've encoutered whilst playing this version.

## Looking for a faster and smaller version of Thisway?
### Try [Thisway Light](https://github.com/Toydotgame/Thisway-Light)!

## To-Do
* Check Bukkit support. If no support, consider making a fork of Thisway for Bukkit.

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
