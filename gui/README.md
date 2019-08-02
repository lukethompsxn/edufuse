# eduFUSE GUI
eduFUSE GUI is a front end interface for your backend file system. The main purpose is to help expose some useful information and to improve visibility when developing your file system. In addition to this, due to the information which is exposed, it can help you to develop your understanding of how underlying file systems work with the various system calls which are made for a single front end operation. 

### Installation
The eduFUSE GUI comes pre-packaged with eduFUSE. All you need to do is specify the parameter when registering your file system, more detailed usage information provided in subsequent sections.

### Getting Started
All of the underlying communication and data collection is completely within eduFUSE. All you need to do is specify whether or not you wish to visualise your file system when calling `edufuse_register()` (in the C version) or `mount()` in the Java version. 

Please note that some paths have been ignored for the visualisation. By ignoring these paths it helps to reduce the amount of information being displayed, thus making it easier to see the useful information. Currently, the ignored paths include `/`, `./`, `.`, `.hidden`, `/.hidden`, `/.Trash`, `/.Trash-100`, `/.Trash-1000`, `/.localized`, `/.DS_STORE`, and `/.DS_Store`.

**Java**

The second parameter of the `mount()` method determines whether the GUI is launched. Simply supply `true` to mount and launch the GUI, or `false` to mount without launching the GUI.

```java
// Main method of HelloFUSE.java

// Visualised (launches GUI)
public static void main(String[] args) {
	HelloFUSE fs = new HelloFUSE();
	fs.mount(args, true);
}

// Not Visualised (does NOT launch GUI)
public static void main(String[] args) {
	HelloFUSE fs = new HelloFUSE();
	fs.mount(args, false);
}
```

**C**

The fourth parameter of the `edufuse_register()` function takes an `int` to specify whether or not the GUI is launched. Simply supply `1` to mount and launch the GUI, or `0` to mount without launching the GUI.
	
```c
// Main method of examplefs.c

// Visualised (launches GUI)
int main(int argc, char *argv[])
{
    return edufuse_register(argc, argv, &efs_operations, 1);
}

// Not Visualised (does NOT launch GUI)
int main(int argc, char *argv[])
{
    return edufuse_register(argc, argv, &efs_operations, 0);
}
```

### Tabs

**Charts**
//todo screenshot

//todo @joel

**Directory**
//todo screenshot

The directory tab has two main components. On the left, we have a live representation of the mounted directory. This automatically updates when files change. You can click on a file or directory (excluding the mount point) and the information about the file/directory will be displayed on the right. 

*File/Directory Properties.*

| Property           	| `stat` variable 	| Description                                                                 
|--------------------	|-----------------	|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	|
| Filename           	| N/A             	| The given pathname of the file                                                                                                                                                                                                                                                                                                                                                     	|
| Created            	| `st_ctime`      	| The time of creation                                                                                                                                                                                                                                                                                                                                                               	|
| Access             	| `st_atime`      	| The time of last access                                                                                                                                                                                                                                                                                                                                                            	|
| Modified           	| `st_mtime`      	| The time of last modification                                                                                                                                                                                                                                                                                                                                                      	|
| File Mode          	| `st_mode`       	| Specifies the mode of the file. *See table below*                                                                                                                                                                                                                                                                                                                                  	|
| Size               	| `st_size`       	| The size of the file in bytes                                                                                                                                                                                                                                                                                                                                                      	|
| Optimal Block Size 	| `st_blksize`    	| A filesystem-specific preferred I/O block size for this object. In some filesystem types, this may vary from file to file                                                                                                                                                                                                                                                          	|
| # Blocks Allocated 	| `st_blocks`     	| This is the amount of disk space that the file occupies, measured in units of 512-byte blocks. The number of disk blocks is not strictly proportional to the size of the file, for two reasons: the file system may use some blocks for internal record keeping; and the file may be sparse—it may have “holes” which contain zeros but do not actually take up space on the disk. 	|
| File Serial Number 	| `st_ino`        	| The file serial number (inode number), which distinguishes this file from all other files on the same device.                                                                                                                                                                                                                                                                      	|
| Device             	| `st_dev`        	| Identifies the device containing the file                                                                                                                                                                                                                                                                                                                                          
| Owner ID           	| `st_uid`        	| The user ID of the file’s owner                                                                                                                                                                                                                                                                                                                                                    	|
| Group ID           	| `st_gid`        	| The group ID of the file                                                                                                                                                                                                                                                                                                                                                           	|
| Link Count         	| `st_nlink`      	| The number of hard links to the file. This count keeps track of how many directories have entries for this file. If the count is ever decremented to zero, then the file itself is discarded as soon as no process still holds it open. Symbolic links are not counted in the total.                                                                                               	|

*File Modes.*

|Name|Numeric Value|Description|
|--- |--- |--- |
|S_IRWXU|0700|Read, write, execute/search by owner.|
|S_IRUSR|0400|Read permission, owner.|
|S_IWUSR|0200|Write permission, owner.|
|S_IXUSR|0100|Execute/search permission, owner.|
|S_IRWXG|070|Read, write, execute/search by group.|
|S_IRGRP|040|Read permission, group.|
|S_IWGRP|020|Write permission, group.|
|S_IXGRP|010|Execute/search permission, group.|
|S_IRWXO|07|Read, write, execute/search by others.|
|S_IROTH|04|Read permission, others.|
|S_IWOTH|02|Write permission, others.|
|S_IXOTH|01|Execute/search permission, others.|
|S_ISUID|04000|Set-user-ID on execution.|
|S_ISGID|02000|Set-group-ID on execution.|
|[XSI]  S_ISVTX|01000|On directories, restricted deletion flag.|


**Logger** 
//todo screenshot

The logger provides customisable output based on your specified selection. When each of your file system methods is called, data is sent through to the visualiser. This information is displayed in the logger. Three different information selections are available, when enabled the related information will be printed to the logger with each invocation of your file system function.

| Option | Description |
| --- | --- |
| System Calls | Enables printing of the file system function which was invoked.|
| File/Directory | Enables printing of the File/Directory path which the function is invoked for/on. |
| File/Directory Information | Enables printing of the various struct elements accesible in the file system function. |
