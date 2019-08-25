package com.github.lukethompsxn.edufuse.util;

import com.github.lukethompsxn.edufuse.struct.FileStat;
import jnr.ffi.Runtime;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

/**
 * Object for representing a mock INode Table for a ManualBlockFS or similar
 *
 * @author Luke Thompson
 * @since 16/8/19
 */
public class INodeTable implements Serializable {
    private int blockSize;

    /**
     * Serialises the INodeTable object to the iNode table on file (/tmp/.inodetable) to store between sessions.
     *
     * @param file       the java.io.File object for the inode table.
     * @param iNodeTable the object representing the current INode table.
     */
    public static void serialise(File file, INodeTable iNodeTable) {
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (String entry : iNodeTable.entires()) {
                INode iNode = iNodeTable.getINode(entry);
                iNode.size = iNode.getStat().st_size.longValue();
                iNode.mode = iNode.getStat().st_mode.longValue();
            }
            oos.writeObject(iNodeTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialises the INodeTable file into the INode table object.
     *
     * @param file the java.io.File object for the inode table.
     * @return INodeTable object for the parsed INodeTable.
     */
    public static INodeTable deserialise(File file) {
        INodeTable iNodeTable = null;
        try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {
            iNodeTable = (INodeTable) ois.readObject();

            if (iNodeTable != null) {
                for (String entry : iNodeTable.entires()) {
                    INode iNode = iNodeTable.getINode(entry);
                    FileStat stat = new FileStat(Runtime.getSystemRuntime());
                    stat.st_size.set(iNode.size);
                    stat.st_mode.set(iNode.mode);
                    iNode.setStat(stat);
                    iNodeTable.updateINode(entry, iNode);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return iNodeTable;
    }

    private HashMap<String, INode> table = new HashMap<>();
    private int blockIndex = 0;

    /**
     * Most common constructor.
     */
    public INodeTable(int blockSize) {
        this.blockSize = blockSize;
    }

    /**
     * Advanced constructor for custom implementations.
     *
     * @param table      HashMap representing the path to INode mappings.
     * @param blockIndex integer representing next available block.
     */
    public INodeTable(HashMap<String, INode> table, int blockIndex, int blockSize) {
        this.table = table;
        this.blockIndex = blockIndex;
        this.blockSize = blockSize;
    }

    /**
     * Returns the INode object for a given path.
     *
     * @param path the path to retrieve the INode for.
     * @return INode object for the given path.
     */
    public INode getINode(String path) {
        return table.get(path);
    }

    /**
     * Adds or Updates an INode with the new object.
     *
     * @param path  string representing given path for INode.
     * @param iNode INode object representing the INode.
     */
    public void updateINode(String path, INode iNode) {
        table.put(path, iNode);
    }

    /**
     * Retrieves the next free block index (offset)
     *
     * @return integer representing the next free block in the block file.
     */
    public int nextFreeBlock() {
        int ret = blockIndex;
        blockIndex++;
        return ret;
    }

    int peekNextFreeBlock() {
        return blockIndex;
    }

    /**
     * Determines whether a given file path has an entry in the INode table.
     *
     * @param path string representing the path to be tested for.
     * @return boolean representing whether file path has entry in the INode table.
     */
    public boolean containsINode(String path) {
        return table.containsKey(path);
    }

    /**
     * Retrieves all the entries in the INode table. Use this method to get the file paths in the INode table,
     * then retrieve the INode using getINode(String path) method.
     *
     * @return set of string representing the file paths in the INode table.
     */
    public Set<String> entires() {
        return table.keySet();
    }

    /**
     * Returns the block size of the file system
     *
     * @return integer representing block size
     */
    public int getBlockSize() {
        return blockSize;
    }
}
