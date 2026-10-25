package com.example.springaidemo.boardgamebuddy;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;

@Service
public class SpringAiBoardGameService implements BoardGameService {

    private final ChatClient chatClient;

    @Value("classpath:/promptTemplates/summarizeSystemPrompt.st")
    Resource summaryPromptTemplate;

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource promptTemplate;

    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Answer summarizeRules(String text) {
        return chatClient.prompt()
                .system(system -> system.text(summaryPromptTemplate))
                .user(userSpec -> userSpec
                        .text("Summarize these rules: {gameRules}")
                        .param("gameRules", text))
                .call()
                .entity(Answer.class);
    }

    @Override
    public Answer askQuestion(Question question, String conversationId) {
        return chatClient.prompt()
                .user(question.question())
                .system(systemSpec -> systemSpec
                        .text(promptTemplate)
                        .param("gameTitle", question.gameTitle())
                        .param("targetLanguage", question.language()))
                .advisors(advisorSpec -> advisorSpec
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .entity(Answer.class);
    }
}
