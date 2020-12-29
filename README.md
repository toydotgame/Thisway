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

## Usage
```
/thisway <distance> [true/false]
```
* You can use `/tw` in place of `/thisway` for convenience and speed.
* `<distance>` is how far you'd like to go \[in blocks\].
* The optional `[true/false]` is for Debug Mode. (More below)

### Examples
To teleport 100 blocks in front of you, use:
```
/thisway 100
```

To teleport 24 blocks ahead and know your location data:
```
/thisway 24 true
```

50-block-TP: (No debug)
```
/tw 50 false
```
Alternatively,
```
/thisway 50
```

You get it? It's that simple!

### Debug Mode
Debug mode will show your current and future positioning stats in chat for you; signalled with headers and footers:
```
=== THISWAY DEBUG START ===
```
```
=== THISWAY DEBUG END ===
```
Like the plugin itself, Debug Mode is very simple. That's all it does.
It _is_ useful when you find a bug or such, and you can repeat your actions with Debug Mode turned on to see where the plugin thinks you are.

### Output
Thisway is, by default, an operator-only plugin. If you think you have an op who shouldn't be one; this may help:
Thisway will always print a very basic rundown of the teleport each time the command is run:
```log
[INFO] [Thisway] <playername> teleported <distance> blocks, from <x>, <y>, <z> to <newX>, <newY>, <newZ>.
```
This output mirrors `<distance>` from the original command sent:
```mccmd
/thisway <distance>
```

## Version
Latest versions always come first to the [Releases](https://github.com/Toydotgame/Thisway/releases) tab.
Versions use [Semantic Versioning](https://semver.org/).
This plugin will _always_ be for Minecraft Release 1.6.4. I am not updating it to 1.12 or 1.16 or whatever you want. I like 1.6 for many reasons, and I want to help it by making more plugins for it; which is a big problem I've encoutered whilst playing this version.

## Features
* Basic teleportation using Bukkit's teleportation system.
* Simple command format.
* Basic positioning data using Debug Mode.
* Multi dimension and world support.
	* I use a special bit of code which gets the name of the command sender's current world, and puts _that_ into the Bukkit teleport script.
	* This should allow for use in multiple dimensions, and
	* \[hypothetical\] Multiverse support - or any other multi-world plugin you may use.

## Building
You will need the Spigot 1.6.4 server JARfile, as that's what's used to create Spigot things; BuildTools and whatever else you use today didn't exist for those versions. Everything was integrated in `server.jar`.
You'll also need the repo. set up in your IDE. I'm going to use Eclipse for my examples, if you use other IDEs (IntelliJ or whatever): suffer. (Or just try to adapt these instructions to suit your IDE, your choice.
1. Right-click the project and select _Export_.
2. It will ask you what you want to export as; under the _Java_ folder in the list, choose _JAR file_. Do _not_ choose _Runnable JAR_! That will not work with Minecraft!
3. Tick the box next to the _Thisway_ project on the left file list.
4. On the right, deselect all of the project files except `plugin.yml`. Spigot needs this to know what commands to listen to and give help for it.
5. Set the export location for the JAR to you server's directory (I'm using `/` in this case), then the plugin directory; i.e: `/plugins/Thisway.jar`.
6. Click _Finish_.

## Looking for a faster and smaller version of Thisway?
### Try [Thisway Light](https://github.com/Toydotgame/Thisway-Light)!

## To-Do
* Test if spamming the command makes the player's Y go down like you would get using vanilla commands. (e.g: `/tp @p ~100 ~ ~`)
	* More info in issue [#1](https://github.com/Toydotgame/Thisway/issues/1).
* Check Bukkit support. If no support, consider making a fork of Thisway for Bukkit.
* Make a glass block get placed over air if the new location ends with the player standing on air.


## Changelog
### Update 1.0.1
* Fixed versioning error in `plugin.yml`.
* Cleaned up code.
    * Seperate method for main command actions, leaving the main method in the class to be for syntax error checking.

### Update 1.0
* Added main set of features:
    * Basic teleporting.
    * Both `/thisway` and `/tw` commands.
    * Debug Mode.
    * Multi world support.
    * Seperate data storage class.
