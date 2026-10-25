---
type: architecture
title: ChatClient and Prompt Mechanism
description: The shared Spring AI mechanism behind all endpoints — ChatClient.Builder construction, ST prompt templates with parameters, defaultSystem prompts, structured output via .entity(Record.class), and message-history advisors keyed by conversation ID.
tags: [spring-ai, chat-client, prompt-templates, structured-output, chat-memory]
verified:
  - by: openwiki/0.6.0
    at: 2026-10-26T13:41:05.092Z
sources:
  - id: openwiki-source-3e208709f1812e5f162fa094
    resource: repo://src/main/java/com/example/springaidemo/boardgamebuddy/SpringAiBoardGameService.java
  - id: openwiki-source-05f1bc303e78d2c296788f75
    resource: repo://src/main/java/com/example/springaidemo/boardgamebuddy/SummaryController.java
  - id: openwiki-source-97e7fd98c171269b1cb10364
    resource: repo://src/main/java/com/example/springaidemo/sentimentanalysis/SentimentController.java
  - id: openwiki-source-10f756c6a53d6e6e477e1fd4
    resource: repo://src/main/java/com/example/springaidemo/simpletranslator/TranslatorController.java
  - id: openwiki-source-cc7c4f7cb3efafec72d7f424
    resource: repo://src/main/resources/promptTemplates/summarizeSystemPrompt.st
  - id: openwiki-source-054ac5bdd567cd532f0edbe9
    resource: repo://src/main/resources/promptTemplates/systemPromptTemplate.st
  - id: openwiki-source-cc38d44d430780dd660c29b4
    resource: repo://src/main/resources/sentimentSystemPrompt.st
  - id: openwiki-source-4a1159849f8f4906019f8945
    resource: repo://src/main/resources/translationPromptTemplate.st
generated: { by: "opencode", at: "2026-10-26T13:41:05.092Z" }
---

# ChatClient and Prompt Mechanism

All three features rely on the same Spring AI building blocks: a `ChatClient` built from a
constructor-injected `ChatClient.Builder`, classpath `.st` prompt templates, parameterized
user/system text, and structured output bound to Java records.

## ChatClient construction

Each controller/service injects `ChatClient.Builder` and calls `chatClientBuilder.build()`
to obtain a `ChatClient`. Only the sentiment controller customizes the builder before
building, via `defaultSystem(...)` to pin its system prompt.

## Prompt templates

Prompt text is kept in `.st` (ST4) template files under `src/main/resources`, loaded with
`@Value("classpath:/...")` into a `Resource`:

- `translationPromptTemplate.st` — user prompt for translation, parameterized with
  `{sourceLanguage}`, `{targetLanguage}`, and `{sourceText}`.
- `sentimentSystemPrompt.st` — system prompt defining the sentiment scoring contract
  (score between -1 and 1 plus an explanation).
- `promptTemplates/summarizeSystemPrompt.st` — system prompt for board-game rule
  summarization.
- `promptTemplates/systemPromptTemplate.st` — parameterized system prompt for Q&A,
  using `{gameTitle}` and `{targetLanguage}`.

Templates are rendered by passing parameters: user text is composed with
`.user(spec -> spec.text(resource).param("key", value))` and system text with
`.system(spec -> spec.text(resource).param("key", value))`. Inline user strings such as
`"User text: {text}"` are parameterized the same way.

## Structured output

Every call terminates with `.call().entity(SomeRecord.class)`. Spring AI asks the model to
produce JSON matching the record shape and deserializes it, which is why each feature ships
its own small output record (`Translation`, `SentimentAnalysis`, `Answer`). The entity
returned is serialized back to JSON by the web layer.

## Conversation memory

`SpringAiBoardGameService` enables multi-turn Q&A by attaching an advisor:

```java
.advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
```

`ChatMemory.CONVERSATION_ID` is a built-in constant that keys message history per
conversation. `SummaryController` supplies a fresh `UUID.randomUUID()` when the caller does
not provide an `X-Conversation-Id` header, so each new caller gets a new conversation scope.

## Endpoint-specific wiring

- Translation: user prompt template + `entity(Translation.class)`.
- Sentiment: builder-level `defaultSystem` + user text param + `entity(SentimentAnalysis.class)`.
- Board game: service-layer prompts (one stateless summarize, one conversation-aware ask)
  + `entity(Answer.class)`.
