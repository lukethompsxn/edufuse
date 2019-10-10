package com.github.lukethompsxn.edufuse.examples.util;

import java.util.HashMap;
import java.util.Set;

/**
 * Object for representing a mock MockINode Table for a ManualBlockFS or similar
 *
 * @author Luke Thompson
 * @since 16/8/19
 */
public class MemoryINodeTable {
    private HashMap<String, MemoryINode> table = new HashMap<>();

    /**
     * Returns the MockINode object for a given path.
     *
     * @param path the path to retrieve the MockINode for.
     * @return MockINode object for the given path.
     */
    public MemoryINode getINode(String path) {
        return table.get(path);
    }

    /**
     * Adds or Updates an MockINode with the new object.
     *
     * @param path  string representing given path for INode.
     * @param iNode MockINode object representing the INode.
     */
    public void updateINode(String path, MemoryINode iNode) {
        table.put(path, iNode);
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

    public void removeINode(String path) {
        table.remove(path);
    }
}
