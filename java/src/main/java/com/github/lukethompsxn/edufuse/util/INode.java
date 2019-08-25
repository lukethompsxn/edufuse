package com.github.lukethompsxn.edufuse.util;

import com.github.lukethompsxn.edufuse.struct.FileStat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for representing a mock INode in a ManualBlockFS or similar.
 *
 * @author Luke Thompson
 * @since 16/8/19
 */
public class INode implements Serializable {
    private transient FileStat stat;
    long mode;
    long size;
    private List<Integer> blocks = new ArrayList<>();

    /**
     * Gets the FileStat for the given INode.
     *
     * @return file stat object
     */
    public FileStat getStat() {
        return stat;
    }

    /**
     * Updates the FileStat for the given INode.
     *
     * @param stat new stat object to update INode with
     */
    public void setStat(FileStat stat) {
        this.stat = stat;
    }

    /**
     * Adds block offsets to the blocks for the given INode. This is to be called when the file is extended and so
     * requires more blocks in order to store the file.
     *
     * @param offsets the offsets of offset * BLOCK_SIZE from the beginning of the file for the new newly allocated block.
     */
    public void addBlocks(List<Integer> offsets) {
        blocks.addAll(offsets);
    }

    /**
     * Gets the block offsets for the contents of the file for the given INode
     *
     * @return list of block offsets which are allocated to the given file.
     */
    public List<Integer> getBlocks() {
        return blocks;
    }
}
