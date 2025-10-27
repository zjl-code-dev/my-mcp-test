package com.zhangjl.mcp.serverdemo.config;
import com.zhangjl.mcp.serverdemo.services.ToolServer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig{
    @Bean
    public ToolCallbackProvider openLibraryTools(ToolServer toolServer) {
        return MethodToolCallbackProvider.builder().toolObjects(toolServer).build();
    }
}