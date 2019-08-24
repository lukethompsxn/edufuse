# eduFUSE C

### Installation
1. Make sure you have edufuse downloaded
2. From command line run ```fusermount -V``` to check whether base libfuse is installed
3. If not installed run ```sudo apt-get update -y``` followed by ```sudo apt-get install -y libfuse-dev```

### Important Files
- `fuse.h:` This header file is necessary when creating any fuse file system in C as it contains all the required definitions. This includes
all the file system functions, helper functions and any data structs fuse requires.
- `edufuse.h:` This header file defines the function "edufuse_register" which is required to link your created file system to the edufuse 
interface.

### Running and Example
A simple example file system can be found [here](https://github.com/lukethompsxn/edufuse/tree/master/example). This can be ran by executing the run.sh script found in the base edufuse [folder](https://github.com/lukethompsxn/edufuse). This will mount the file
system to `/tmp/example/` where you will be able to use `ls` on the folder and `cat` on the file it contains.

When creating your file system from scratch, the easiest way to compile and run it follows the same process as above. However, you must either write your file system to the `example.c` file or you can create your own and modify the [`CMakeList`](https://github.com/lukethompsxn/edufuse/blob/master/CMakeLists.txt) by replacing `example/example.c` under the `add_executable` section for your own file. 

### Getting Started
To being writing your file system you first must include the two header files fuse.h and edufuse.h. Once you have these you can begin
writing your file system function calls. These functions must match those defined in the struct [fuse_operations](https://libfuse.github.io/doxygen/structfuse__operations.html).

Once you have written your file system calls you must map them to the [fuse_operations](https://libfuse.github.io/doxygen/structfuse__operations.html)
struct from `fuse.h`. This struct will be passed to the edufuse interface. An example of this can be seen below:
```C
static struct fuse_operations yourfs_operations = {
    .getattr = yourfs_getattr,
    .open = yourfs_open,
    .read = yourfs_read,
    .readdir = yourfs_readdir,
};
```

Finally, you write your main function which calls "edufuse_register", this will start your file system. The function takes the number of 
command line arguments, the location to mount your file system, the [fuse_operations](https://libfuse.github.io/doxygen/structfuse__operations.html)
struct mapped to your functions and whether or not you wish to run visualisations (1 or 0). 
```C
int main(int argc, char *argv[]) = {
    return edufuse_register(argc, argv, &yourfs_operations, 1)
}
```
