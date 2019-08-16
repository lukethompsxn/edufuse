package com.edufuse.util;

import com.edufuse.struct.FileStat;
import jnr.ffi.Runtime;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class INodeTable implements Serializable {

    public static void serialise(File file, INodeTable iNodeTable) {
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            //todo remove this hack when FileStat is serializable
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

    public static INodeTable deserialise(File file) {
        INodeTable iNodeTable = null;
        try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {
            iNodeTable = (INodeTable) ois.readObject();
            //todo remove this hack when FileStat is serializable
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
