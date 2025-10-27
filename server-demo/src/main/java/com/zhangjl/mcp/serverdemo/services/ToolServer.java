package com.zhangjl.mcp.serverdemo.services;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ToolServer {
    @Tool(description = "批量生成标题")
    public List<String> BatchGenerateTitles(String title) {
        return List.of("测试成功");
    }
}