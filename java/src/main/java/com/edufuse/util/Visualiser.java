package com.edufuse.util;

import com.edufuse.examples.ManualBlocksFS;
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
        table.put("block-size", String.valueOf(ManualBlocksFS.BLOCK_SIZE));

        Map<String, String> inodes = new HashMap<>();
        for (String entry : iNodeTable.entires()) {
            Map<String, String> node = new HashMap<>();
            INode iNode = iNodeTable.getINode(entry);
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