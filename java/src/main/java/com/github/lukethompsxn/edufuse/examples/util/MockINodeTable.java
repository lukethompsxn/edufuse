package com.github.lukethompsxn.edufuse.examples.util;

import com.github.lukethompsxn.edufuse.struct.FileStat;
import jnr.ffi.Runtime;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

/**
 * Object for representing a mock MockINode Table for a ManualBlockFS or similar
 *
 * @author Luke Thompson
 * @since 16/8/19
 */
public class MockINodeTable implements Serializable {
    private int blockSize;

    /**
     * Serialises the INodeTable object to the iNode table on file (/tmp/.inodetable) to store between sessions.
     *
     * @param file       the java.io.File object for the inode table.
     * @param mockINodeTable the object representing the current MockINode table.
     */
    public static void serialise(File file, MockINodeTable mockINodeTable) {
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (String entry : mockINodeTable.entires()) {
                MockINode iNode = mockINodeTable.getINode(entry);
                iNode.size = iNode.getStat().st_size.longValue();
                iNode.mode = iNode.getStat().st_mode.longValue();
            }
            oos.writeObject(mockINodeTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialises the INodeTable file into the MockINode table object.
     *
     * @param file the java.io.File object for the inode table.
     * @return INodeTable object for the parsed INodeTable.
     */
    public static MockINodeTable deserialise(File file) {
        MockINodeTable iNodeTable = null;
        try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {
            iNodeTable = (MockINodeTable) ois.readObject();

            if (iNodeTable != null) {
                for (String entry : iNodeTable.entires()) {
                    MockINode iNode = iNodeTable.getINode(entry);
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

    private HashMap<String, MockINode> table = new HashMap<>();
    private int blockIndex = 0;

    /**
     * Most common constructor.
     */
    public MockINodeTable(int blockSize) {
        this.blockSize = blockSize;
    }

    /**
     * Advanced constructor for custom implementations.
     *
     * @param table      HashMap representing the path to MockINode mappings.
     * @param blockIndex integer representing next available block.
     */
    public MockINodeTable(HashMap<String, MockINode> table, int blockIndex, int blockSize) {
        this.table = table;
        this.blockIndex = blockIndex;
        this.blockSize = blockSize;
    }

    /**
     * Returns the MockINode object for a given path.
     *
     * @param path the path to retrieve the MockINode for.
     * @return MockINode object for the given path.
     */
    public MockINode getINode(String path) {
        return table.get(path);
    }

    /**
     * Adds or Updates an MockINode with the new object.
     *
     * @param path  string representing given path for INode.
     * @param iNode MockINode object representing the INode.
     */
    public void updateINode(String path, MockINode iNode) {
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
     * Determines whether a given file path has an entry in the MockINode table.
     *
     * @param path string representing the path to be tested for.
     * @return boolean representing whether file path has entry in the MockINode table.
     */
    public boolean containsINode(String path) {
        return table.containsKey(path);
    }

    /**
     * Retrieves all the entries in the MockINode table. Use this method to get the file paths in the MockINode table,
     * then retrieve the MockINode using getINode(String path) method.
     *
     * @return set of string representing the file paths in the MockINode table.
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
