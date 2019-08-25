package com.github.lukethompsxn.edufuse.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Visualiser {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8081;

    private Socket clientSocket;
    private PrintWriter out;

    public Visualiser() {
        try {
            startConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startConnection() throws IOException {
        clientSocket = new Socket(HOST, PORT);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public void sendINodeTable(INodeTable iNodeTable) {
        Map<String, String> table = new HashMap<>();

        table.put("type", "INODE_TABLE");
        table.put("nextBlock", String.valueOf(iNodeTable.peekNextFreeBlock()));
        table.put("block-size", String.valueOf(iNodeTable.getBlockSize()));

        int totalBlocks = 0;

        Map<String, String> inodes = new HashMap<>();
        for (String entry : iNodeTable.entires()) {
            Map<String, String> node = new HashMap<>();
            INode iNode = iNodeTable.getINode(entry);
            totalBlocks += iNode.getBlocks().size();
            node.put("size", String.valueOf(iNode.size));
            node.put("mode", String.valueOf(iNode.mode));
            node.put("blocks", JSONArray.toJSONString(iNode.getBlocks()));
            inodes.put(entry, JSONObject.toJSONString(node));
        }

        table.put("iNodes", JSONObject.toJSONString(inodes));
        out.println(JSONObject.toJSONString(table)
                .replaceAll("\\\\", "")
                .replaceAll("\"\\{", "\\{")
                .replaceAll("\\}\"", "\\}") + "\\e");


        sendBlockFile(iNodeTable, totalBlocks);
    }

    private void sendBlockFile(INodeTable iNodeTable, int totalBlocks) {
        Map<String, String> blockFile = new HashMap<>();
        Map<String, String> blocks = new HashMap<>();

        blockFile.put("type", "BLOCK_FILE");

        String[] entries = new String[totalBlocks];
        Map<String, String> entryMap = new HashMap<>();
        for (int i = 0; i < totalBlocks; i++) {
            for (String entry : iNodeTable.entires()) {
                INode iNode = iNodeTable.getINode(entry);
                if (iNode.getBlocks().contains(i)) {
                    if (entries[i] == null) {
                        entries[i] = entry; //concatenate at end of string
                    } else {
                        entries[i] = entries[i] + " " + entry;
                    }
                }
            }
            entryMap.put("File", entries[i]);
            entryMap.put("Contents", "");
            blocks.put("Block " + i, JSONObject.toJSONString(entryMap));
        }

        blockFile.put("Blocks", JSONObject.toJSONString(blocks));
        blockFile.put("Total Blocks", Integer.toString(totalBlocks));

        out.println(JSONObject.toJSONString(blockFile)
                .replaceAll("\\\\", "")
                .replaceAll("\"\\{", "\\{")
                .replaceAll("\\}\"", "\\}") + "\\e");
    }

    public void stopConnection() throws IOException {
        out.close();
        clientSocket.close();
    }
}