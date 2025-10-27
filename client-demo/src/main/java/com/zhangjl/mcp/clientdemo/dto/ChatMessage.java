package com.zhangjl.mcp.clientdemo.dto;

public record ChatMessage(
    String content,  // 消息内容
    String role      // 角色：user/assistant
){}
