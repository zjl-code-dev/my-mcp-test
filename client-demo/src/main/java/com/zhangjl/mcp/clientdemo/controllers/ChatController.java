package com.zhangjl.mcp.clientdemo.controllers;

import com.zhangjl.mcp.clientdemo.dto.ChatMessage;
import com.zhangjl.mcp.clientdemo.services.ChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {

    private final ChatService chatService;
    // 存储每个会话的取消信号（使用UUID作为会话标识）
    private final Map<String, Sinks.Empty<Void>> cancelSignals = new ConcurrentHashMap<>();

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 流式聊天接口（SSE）
     * @param sessionId 会话ID（用于标识当前对话，实现停止功能）
     * @param userMessage 用户消息
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(
            @RequestParam String sessionId,
            @RequestBody ChatMessage userMessage) {

        // 创建当前会话的取消信号
        Sinks.Empty<Void> cancelSignal = Sinks.empty();
        cancelSignals.put(sessionId, cancelSignal);

        // 生成AI响应流，并包装为SSE格式
        return chatService.streamChat(userMessage, cancelSignal.asMono().flux())
                .map(assistantMessage -> "data: " + assistantMessage.content() + "\n\n")
                .doFinally(signalType -> {
                    // 流结束后清理取消信号
                    cancelSignals.remove(sessionId);
                });
    }

    /**
     * 停止生成接口
     * @param sessionId 会话ID（指定要停止的对话）
     */
    @PostMapping("/stop")
    public void stopGeneration(@RequestParam String sessionId) {
        // 触发对应会话的取消信号
        Sinks.Empty<Void> cancelSignal = cancelSignals.get(sessionId);
        if (cancelSignal != null) {
            cancelSignal.tryEmitEmpty();  // 发送取消信号
            cancelSignals.remove(sessionId);  // 清理信号
        }
    }
}
