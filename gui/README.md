# eduFUSE GUI
eduFUSE GUI is a front end interface for your backend file system. The main purpose is to help expose some useful information and to improve visibility when developing your file system. In addition to this, due to the information which is exposed, it can help you to develop your understanding of how underlying file systems work with the various system calls which are made for a single front end operation. 

### Installation
The eduFUSE GUI comes pre-packaged with eduFUSE. All you need to do is specify the parameter when registering your file system, more detailed usage information provided in subsequent sections.

### Getting Started
All of the underlying communication and data collection is completely within eduFUSE. All you need to do is specify whether or not you wish to visualise your file system when calling `edufuse_register()` (in the C version) or `mount()` (in the Java version). 

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
##### Charts
//todo @joel

##### Directory


##### Logger 
