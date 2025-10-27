package com.zhangjl.mcp.clientdemo.services;

import com.zhangjl.mcp.clientdemo.dto.ChatMessage;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final SyncMcpToolCallbackProvider toolCallbackProvider;

    public ChatService(ChatClient.Builder clientBuilder, SyncMcpToolCallbackProvider toolCallbackProvider) {
        this.toolCallbackProvider = toolCallbackProvider;
        this.chatClient = clientBuilder.build();
    }

    /**
     * 流式生成AI回复（支持中断）
     * @param userMessage 用户消息
     * @param cancelSignal 取消信号（用于中断生成）
     */
    public Flux<ChatMessage> streamChat(ChatMessage userMessage, Flux<Void> cancelSignal) {
        // 构建用户消息
        UserMessage message = new UserMessage(userMessage.content());
        
        // 生成流式响应并映射为ChatMessage
        Flux<ChatResponse> chatFlux = chatClient.prompt().toolCallbacks(this.toolCallbackProvider).messages(message).stream().chatResponse();
        
        // 转换响应流并支持通过cancelSignal中断
        return chatFlux
                .takeUntilOther(cancelSignal)  // 当取消信号触发时终止流
                .map(response -> new ChatMessage(
                        response.getResult().getOutput().getText(),
                        "assistant"
                ));
    }
}
