package com.edufuse.util;

import com.edufuse.struct.FileStat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class INode implements Serializable {
    private transient FileStat stat; //todo make this non-transient - cant be serialised as Struct is not serializable at the moment
    long mode; //todo remove this hack when FileStat is serializable
    long size; //todo remove this hack when FileStat is serializable
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
