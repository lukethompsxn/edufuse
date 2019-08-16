package com.edufuse.util;

import com.edufuse.struct.FileStat;

import java.util.ArrayList;
import java.util.List;

public class INode {
    private FileStat stat;
    private List<Integer> blocks = new ArrayList<>();

    public FileStat getStat() {
        return stat;
    }

    public void setStat(FileStat stat) {
        this.stat = stat;
    }

    public void addBlocks(List<Integer> offsets) {
        blocks.addAll(offsets);
    }

    public List<Integer> getBlocks() {
        return blocks;
    }
}
