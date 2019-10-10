package com.github.lukethompsxn.edufuse.examples.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Luke Thompson
 * @since 18/8/19
 */
public class FSVisualiser {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8081;

    private Socket clientSocket;
    private PrintWriter out;

    public FSVisualiser() {
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

    public void sendINodeTable(MockINodeTable mockINodeTable) {
        Map<String, String> table = new HashMap<>();

        table.put("type", "INODE_TABLE");
        table.put("nextBlock", String.valueOf(mockINodeTable.peekNextFreeBlock()));
        table.put("block-size", String.valueOf(mockINodeTable.getBlockSize()));

        Map<String, String> inodes = new HashMap<>();
        for (String entry : mockINodeTable.entires()) {
            Map<String, String> node = new HashMap<>();
            MockINode iNode = mockINodeTable.getINode(entry);
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
    }

    public void stopConnection() throws IOException {
        out.close();
        clientSocket.close();
    }
}