# eduFUSE
[![Build Status](https://travis-ci.com/lukethompsxn/edufuse.svg?token=gDeffs2syumfbfyPNXfM&branch=master)](https://travis-ci.com/lukethompsxn/edufuse) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com/edufuse/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com/edufuse)
 [![MIT Licence](https://badges.frapsoft.com/os/mit/mit.png?v=103)](https://opensource.org/licenses/mit-license.php)


A multi-language, educational, user space file system tool. This tool is designed to help ease some of the difficulties associated with developing a userspace filesystem. Many of the common setup difficulties have been alleviated, allowing the user to simply implement the file system methods without needing to worry about environment configuration. In addition to this a GUI is provided to help improve visibility and further improve the experience of developing a userspace file system. Whether you are developing your first userspace filesystem, or a seasoned proffesional, we hope eduFUSE can help speedup the development process. eduFUSE currently supports operating systems Linux and macOS, with languages C and Java. 

Part IV Honours Project by Luke Thompson and Joel Clarke. Supervised by Robert Sheehan.

## Getting Started
Firstly, you will need to install fuse, this can be done on linux using `sudo apt-get install libfuse-dev`. Then, below you will find the general eduFUSE user guide. Guides for [Java](https://github.com/lukethompsxn/edufuse/tree/master/java/) and [GUI](https://github.com/lukethompsxn/edufuse/tree/master/gui/) can be found at the respective links.

When running your file system, make sure you pass `-d -f <mount-point>` where <mount-point> is your mount point as program arguments. 

### Linux
In order to use eduFUSE on a linux system, all you need to do is download the latest release [here](https://github.com/lukethompsxn/edufuse/releases).

### macOS
In order to use eduFUSE on a macOS system, you first need to install [osxfuse](https://osxfuse.github.io/). Then download the latest eduFUSE release [here](https://github.com/lukethompsxn/edufuse/releases).

If you get the error `error On Darwin API version 25 or greater must be used` (or similar), then you will need to modify the `fuse.h` header file located at `/usr/local/include/osxfuse/fuse/fuse.h`. On line 28, modify to `FUSE_USE_VERSION` to be same as the `FUSE_USE_VERSION` defined on line 1 of `edufuse.c` (currently this is 31).

### Windows
Currently not supported :'(

## C User Guide
@joel todo

## Java User Guide
Find the Java User Guide [here](https://github.com/lukethompsxn/edufuse/tree/master/java/).

## GUI User Guide
Find the GUI User Guide [here](https://github.com/lukethompsxn/edufuse/tree/master/gui/).

## Troubleshooting
todo

## Acknowledgements
- [mkjson](https://github.com/Jacajack/mkjson)
- [ptty](https://gitlab.com/pachanka/ptty)
- [vue-terminal-ui](https://github.com/shershen08/vue-terminal-ui)
- [vue-file-tree](https://github.com/robogeek/vue-file-tree)
- [jnr-fuse](https://github.com/SerCeMan/jnr-fuse)
- [jnr-ffi](https://github.com/jnr/jnr-ffi)
