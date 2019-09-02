# Troubleshooting

### Incorrect JDK Version
``` Java
Error:java: invalid source release: 11
```
#### Solution
Download and install JDK 11 found [here](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html).
Once you've done this you'll need to set the correct JDK within your IDE (example shown is IntelliJ).
1. File > Project Structure > Project Settings > Project. Set your project JDK to point to JDK 11 by selecting new > /usr/lib/jvm/jdk-11.x.x
![image](https://user-images.githubusercontent.com/31237267/63901609-67918180-ca59-11e9-81c1-b5e1c1d4059b.png)
2. File > Settings > Build, Execution, Deployment > Compiler > Java Compiler. Check Target bytecode version is 1.8.
![image](https://user-images.githubusercontent.com/31237267/63901655-90197b80-ca59-11e9-8a3c-ba18b6b40191.png)

### Failed to Connect to Socket
``` Bash
Socket was successfully created.
Socket not ready, sleeping for 2 seconds then retrying... (attempt 1/2)
Socket not ready, sleeping for 2 seconds then retrying... (attempt 2/2)
Failed to connect to the socket.... exiting....
```
#### Solution
This occurs when you run eduFUSE with visualisations enabled, but you don't have the GUI open. The GUI should launch automatically, however you can disable visualisations by passing false in the mount command (java) or edufuse_register (c).

### Corrupt iNode Table
```
Filesystem running...
java.io.EOFException
    at java.io.ObjectInputStream$PeekInputStream.readFully(ObjectInputStream.java:2681)
    at java.io.ObjectInputStream$BlockDataInputStream.readShort(ObjectInputStream.java:3156)
    at java.io.ObjectInputStream.readStreamHeader(ObjectInputStream.java:862)
    at java.io.ObjectInputStream.<init>(ObjectInputStream.java:358)
    at com.edufuse.util.INodeTable.deserialise(INodeTable.java:28)
    at com.edufuse.examples.ManualBlocksFS.init(ManualBlocksFS.java:69)
    at com.edufuse.filesystem.AbstractFS.lambda$init$13(AbstractFS.java:158)
    at jnr.ffi.provider.jffi.NativeClosureProxy$$impl$$20.invoke(Unknown Source)
    at com.kenai.jffi.Foreign.invokeN4O1(Native Method)
    at com.kenai.jffi.Invoker.invokeN4(Invoker.java:1194)
    at com.edufuse.util.FUSELink$jnr$ffi$0.edufuse_register(Unknown Source)
    at com.edufuse.filesystem.AbstractFS.mount(AbstractFS.java:232)
    at com.edufuse.examples.ManualBlocksFS.main(ManualBlocksFS.java:327)
```
#### Solution
To fix this you'll need to delete your iNode Table file and block file that you've stored locally. These 2 files should be re-created on the start up of your file system. 

### Missing Mount Point
``` Bash
fuse: missing mountpoint parameter
```
#### Solution
This error occurs when you have not given your file system a mount point at execution time. To fix this make sure your last command line argument when executing your file system is the directory you wish mount your file system (e.g. /tmp/file).

### Bad Mount Point
``` Bash
fuse: bad mount point `/tmp/test': No such file or directory
```
#### Solution
Occurs when trying to mount your file system to a directory that does not exist. Simply make sure your mount point exists before
running your file system. 

### Transport Endpoint is not Connected
```
fuse: bad mount point `/mnt': Transport endpoint is not connected
```
#### Solution
This commonly happens when a mounted file system crashes and does not successfully unmount. To unmount the file system run
```
fusermount -u <path_to_mounted_fs>
```
