# eduFUSE Java
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com/edufuse/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com/edufuse)

### Installation
todo

### Important Classes
- `FileSystemStub` is the stub class which your file system implementations must extend. This stub class has method signatures for all of the supported FUSE functions. 
- `AbstractFS` is the abstract class which `FileSystemStub` extends. This class provides the logic for communicating with the underlying FUSE implementation, determining the implemented methods, mounting of the file system and unmounting. 
- `FileStat`, `Flock`, `FuseBug`, `FuseBufvec`, `FuseContext`, `FuseFileInfo`, `FuseOperations`, `FusePollhandle`, `Statvfs`, and `Timespec` are Java classes representing the c `structs`. These classses are passed into the various FUSE methods. In order to utilise them, simply get or set their data as you would in a c implementation of a FUSE file system.
- `ErrorCodes` is a class which stores all of the error codes which you may wish to return in file system implementation for various reasons. Each of the methods in the `ErrorCodes` class returns a `int` corresponding the specific error code.
- `FuseException` is an exception class which is used for raising various exceptions you may wish to throw in your file system implementation.
- `AccessConstants`, `FuseBufFlags`, `IoctlFlags`, and `XAttrConstants` are flag classes. Please refer to JavaDoc documentation in the classes for more information.

### Running an Example
Example file system implementations can be found [here](https://github.com/lukethompsxn/edufuse/tree/master/java/examples/). You can run these examples either from and IDE or command line, but first you need to install eduFUSE.

**From an IDE** *(recommended)*

- Simply open the example which you wish to run. Then since the examples have a `public static void main(String[])` method, you should be able to click the run arrow next to the method in the class of the example file system you wish to run. 
- **NOTE** the first time you run the file system like this you will recieve an error, this is because you need to pass the mount point as a parameter to the main method. In order to do this, simply modify the Run Configuration settings to add your mount point as a `Program Argument`. Guides for adding program parameters can be found here for [IntelliJ](https://stackoverflow.com/questions/2066307/how-do-you-input-commandline-argument-in-intellij-idea) and [Eclipse](https://www.cs.colostate.edu/helpdocs/eclipseCommLineArgs.html). For example, if I was using Linux, I could pass `/tmp/example` as a program argument for my mount point (assuming this directory exists). 

### Getting Started
After installation, all you need to do is create a class extending `FileSystemStub`. Then, simply implement override the methods you wish to install. It is important to note that although there is no requirement for any methods to be implemented, a basic set of methods is required for any functioning file system. More information can be found about this [here](https://github.com/libfuse/libfuse). Full example implementations can be found [here](https://github.com/lukethompsxn/edufuse/tree/master/java/src/main/java/com/edufuse/examples).

First you need to create a class extending `FileSystemStub`.

```java
import FileSystemStub;

public class HelloFUSE extends FileSystemStub { 
}
```

Then create a main method (either inside your file system class or elsewhere), create an instance of your file system class, then call `mount()` to mount (start) your file system. As all FUSE file systems must be unmounted, it is good practice to do this in a `try, catch, finally` where you call `unmount()` in the finally block. The `mount()` method takes two parameters, `args` which are command line arguments (including the mount point), and a `boolean` specifying whether to launch the front end GUI.

```java
public static void main(String[] args) {
	HelloFUSE fs = new HelloFUSE();
 	try {
   		fs.mount(args, false);
	} finally {
   		fs.unmount();
   	}
}
```
In order to run your file system implementation, simply execute the main method you implemented. Please note that you **must** pass the mount point as a parameter to the program. This is done on command line simply by specifying them after calling to run the program, or alternatively in an IDE you can edit the `Configuration` for the run in order to specify program arguments.   

### Methods
##### FileSystemStub 
 **Method** | **Explanation**
 --- | ---
`mount(String[], boolean)` | `String[]` is arguments passed from command line. **This must include the mount path.** `boolean` is the parameter for selecting whether to launch the GUI. This GUI can help with visualising the underlying commands and help to debug your file system implementation. The `mount()` function is used to mount (start) your file system. This method registers your file systems functions with the underlying VFS. 
| `unmount()` | This function is used to unmount your specified mount point from the underlying VFS. By calling this in your file system, it will save you from having to run `fusermount -u mountPoint` where `mountPoint` is your specified mount point after each run of your file system. It is good practice to mount your file system in a `try, catch, finally` block in order to ensure unmounting occurs (assuming no segmentation fault).
| `getContext()` | This function returns an object of `FuseContext` which exposes the `fuse` pointer, `uid`, `gid`, `pid,` `private_data` of the file system, and `unmask`. 
