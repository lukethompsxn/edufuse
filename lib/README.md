# eduFUSE C

### Installation
todo

### Important Files
- `fuse.h:` This header file is necessary when creating any fuse file system in C as it contains all the required definitions. This includes
all the file system functions, helper functions and any data structs fuse requires.
- `edufuse.h:` This header file defines the function "edufuse_register" which is required to link your created file system to the edufuse 
interface.

### Running and Example

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
