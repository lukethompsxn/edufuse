package com.github.lukethompsxn.edufuse.examples.util;

import com.github.lukethompsxn.edufuse.struct.FileStat;

/**
 * Class for representing a mock INode in a ManualBlockFS or similar.
 *
 * @author Luke Thompson
 * @since 16/8/19
 */
public class MemoryINode {
    private transient FileStat stat;
    private byte[] content = new byte[0];

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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }


}
