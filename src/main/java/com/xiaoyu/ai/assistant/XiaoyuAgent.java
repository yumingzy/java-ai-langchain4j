package com.xiaoyu.ai.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        // wiringMode = EXPLICIT 的作用是启用“显式装配模式”。
        // 在此模式下，必须通过注解属性（如 chatModel、chatMemoryProvider）明确指定 Spring Bean 的名称，
        // 而不是根据类型自动推断。这可以避免在 Spring 容器中存在多个同类型 Bean 时产生歧义。
        wiringMode = EXPLICIT,
        streamingChatModel = "qwenStreamingChatModel",
        chatMemoryProvider = "chatMemoryProviderXiaoyu",
        tools ="appointmentTools",
        contentRetriever = "contentRetrieverXiaoyuPinecone"//配置向量存储
)
public interface XiaoyuAgent {
    @SystemMessage(fromResource = "xiaoyu-prompt-template.txt")
    Flux<String> chat(@MemoryId Long memoryId, @UserMessage String userMessage);
}
