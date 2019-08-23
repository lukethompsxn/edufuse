package com.github.lukethompsxn.edufuse.examples;

import com.github.lukethompsxn.edufuse.filesystem.FileSystemStub;
import com.github.lukethompsxn.edufuse.struct.*;
import com.github.lukethompsxn.edufuse.util.FuseFillDir;
import com.github.lukethompsxn.edufuse.util.INodeTable;
import com.github.lukethompsxn.edufuse.util.Visualiser;
import jnr.ffi.Pointer;
import jnr.ffi.types.dev_t;
import jnr.ffi.types.mode_t;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;

import java.io.File;
import java.io.IOException;

/**
 * @author Luke Thompson
 * @since 14.08.19
 */
public class ManualBlocksFSStudent extends FileSystemStub {

    public static final int BLOCK_SIZE = 32; //bytes
    private static final String BLOCK_FILE_LOCATION = "/Users/lukethompson/Downloads/testfuse3/.blockfile";
    private static final String INODE_LOCATION = "/Users/lukethompson/Downloads/testfuse3/.inodetable";

    private static File blockFile = null;
    private static File iNodeTableFile = null;

    private INodeTable iNodeTable = null;
    private Visualiser visualiser;

    @Override
    public Pointer init(Pointer conn) {
        
        /*
        
        Implement the file system initialisation here. 
        
        You will need to check/create your blockFile and iNodeTableFile, and 
        read/create your INodeTable.
        
         */

        if (isVisualised()) {
            visualiser = new Visualiser();
        }

        return conn;
    }

    @Override
    public int getattr(String path, FileStat stat) {

        /*

        Implement the file system get attributes here.

        You will need to handle three cases
        - if the file exists
        - if the file doesn't exist
        - if the path is "/"

        In the last case, you need to set st_mode to be 'FileStat.S_IFDIR | 0755'
        and st_nlink to be '2'.

         */

        return 0;
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filler, @off_t long offset, FuseFileInfo fi) {

        /*

        Implement the logic for reading the contents of a directory here.

        You do not need to go through your block file to achieve this, simply
        looking through your INode table will be sufficient to implement this
        functionality.

         */

        return 0;
    }

    @Override
    public int open(String path, FuseFileInfo fi) {

        /*

        Implement the logic for opening a file here.

        You do not actually need to open the file in this implementation due
        to how the files are stored, but you should still verify it exists.

         */

        return 0;
    }


    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {

        /*

        Implement the functionality for reading a file here.

        You should wrap everything in a Java synchronised block.
        Take a look at the RandomAccessFile class and remember that the blocks
        of the file may not be contiguous.

        The return value should be the number of bytes read.

         */

        return 0;
    }

    @Override
    public int write(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {

        /*

        Implement the functionality for writing to a file here.

        You should wrap everything in a Java synchronised block.
        Take a look at the RandomAccessFile class and remember that the blocks
        of the file may not be contiguous. Also make sure you thinking of the
        three cases which you may be writing in (in regard to the block).
        Don't forget to update the size in INode.

        The return value should be the number of bytes written.

         */

        if (isVisualised()) {
            visualiser.sendINodeTable(iNodeTable);
        }

        return 0;
    }

    @Override
    public int mknod(String path, @mode_t long mode, @dev_t long rdev) {

        /*

        Implement the functionality for creating a file here.

         */

        return 0;
    }


    @Override
    public int utimens(String path, Timespec[] timespec) {

        /*

        Implement the functionality for adding timestamps here.

         */

        return 0;
    }

    public static void main(String[] args) {
        ManualBlocksFSStudent fs = new ManualBlocksFSStudent();
        try {
            fs.mount(args, false);
        } finally {
            fs.unmount();
        }
    }

    /*

    ############################################

    THE METHODS BELOW DO NOT NEED TO BE MODIFIED

    ############################################

     */

    @Override
    public int flush(String path, FuseFileInfo fi) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int statfs(String path, Statvfs stbuf) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int mkdir(String path, long mode) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int rmdir(String path) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int rename(String oldpath, String newpath) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int truncate(String path, @size_t long size) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int release(String path, FuseFileInfo fi) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int fsync(String path, int isdatasync, FuseFileInfo fi) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int setxattr(String path, String name, Pointer value, @size_t long size, int flags) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int getxattr(String path, String name, Pointer value, @size_t long size) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int listxattr(String path, Pointer list, @size_t long size) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int removexattr(String path, String name) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int opendir(String path, FuseFileInfo fi) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int releasedir(String path, FuseFileInfo fi) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public void destroy(Pointer initResult) {

        /*  NO FURTHER FUNCTIONALITY REQUIRED  */

        if (isVisualised()) {
            try {
                visualiser.stopConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int access(String path, int mask) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }

    @Override
    public int lock(String path, FuseFileInfo fi, int cmd, Flock flock) {

        /*  NO FUNCTIONALITY REQUIRED  */

        return 0;
    }
}
