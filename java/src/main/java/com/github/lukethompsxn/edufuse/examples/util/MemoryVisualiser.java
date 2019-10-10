package com.github.lukethompsxn.edufuse.examples.util;

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
public class MemoryVisualiser {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8081;

    private Socket clientSocket;
    private PrintWriter out;

    public MemoryVisualiser() {
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

    public void sendINodeTable(MemoryINodeTable mockINodeTable) {
        Map<String, String> table = new HashMap<>();

        table.put("type", "INODE_TABLE");

        Map<String, String> inodes = new HashMap<>();
        for (String entry : mockINodeTable.entires()) {
            Map<String, String> node = new HashMap<>();
            MemoryINode iNode = mockINodeTable.getINode(entry);

            String content = new String(iNode.getContent());
            if (content != null && !content.isBlank()) {
                content = content.replaceAll("\\n", "");
            }

            node.put("contents", content);
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