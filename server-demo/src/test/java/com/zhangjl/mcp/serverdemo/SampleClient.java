package com.zhangjl.mcp.serverdemo;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema.*;
import java.util.Map;
/**
 * @author zhengxujie
 * 2025/05/07/上午9:31
 */
public class SampleClient {
	private final McpClientTransport transport;
	public SampleClient(McpClientTransport transport) {
		this.transport = transport;
	}
	public void run() {
		var client = McpClient.sync(this.transport).build();
		client.initialize();
		client.ping();
		// List and demonstrate tools
		ListToolsResult toolsList = client.listTools();
		System.out.println("Available Tools = " + toolsList);
		CallToolResult books = client.callTool(new CallToolRequest("BatchGenerateTitles", Map.of("title", "人工智能")));
		System.out.println("Title Response = " + books);
		client.closeGracefully();
	}
}