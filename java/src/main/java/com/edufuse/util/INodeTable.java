package com.edufuse.util;

import java.util.HashMap;
import java.util.Set;

public class INodeTable {
    private HashMap<String, INode> table = new HashMap<>();
    private int blockIndex = 0;

    public INodeTable() { }

    public INodeTable(HashMap<String, INode> table, int blockIndex) {
        this.table = table;
        this.blockIndex = blockIndex;
    }

    public INode getINode(String path) {
        return table.get(path);
    }

    public void updateINode(String path, INode iNode) {
        table.put(path, iNode);
    }

    public int nextFreeBlock() {
        int ret = blockIndex;
        blockIndex++;
        return ret;
    }

    public boolean containsINode(String path) {
        return table.containsKey(path);
    }

    public Set<String> entires() {
        return table.keySet();
    }
}
