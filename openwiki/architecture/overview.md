---
type: architecture
title: Architecture Overview
description: System structure of the Spring AI demo — Spring Boot 3.4/Java 21 build, application entrypoint, package layout, and how a single ChatClient is configured against an OpenAI-compatible model.
tags: [architecture, spring-boot, spring-ai, openai, chat-client]
verified:
  - by: openwiki/0.6.0
    at: 2026-10-26T13:41:05.092Z
sources:
  - id: openwiki-source-2355f81d7cf522f8dbdaabd4
    resource: repo://pom.xml
  - id: openwiki-source-3e208709f1812e5f162fa094
    resource: repo://src/main/java/com/example/springaidemo/boardgamebuddy/SpringAiBoardGameService.java
  - id: openwiki-source-05f1bc303e78d2c296788f75
    resource: repo://src/main/java/com/example/springaidemo/boardgamebuddy/SummaryController.java
  - id: openwiki-source-97e7fd98c171269b1cb10364
    resource: repo://src/main/java/com/example/springaidemo/sentimentanalysis/SentimentController.java
  - id: openwiki-source-10f756c6a53d6e6e477e1fd4
    resource: repo://src/main/java/com/example/springaidemo/simpletranslator/TranslatorController.java
  - id: openwiki-source-0a2feee9354cdbefdd69d5ed
    resource: repo://src/main/java/com/example/springaidemo/SpringAiDemoApplication.java
  - id: openwiki-source-ece093ad10b91f3da0b7e34f
    resource: repo://src/main/resources/application.properties
generated: { by: "opencode", at: "2026-10-26T13:41:05.092Z" }
---

# Architecture Overview

The repository is `spring-ai-demo`, a minimal Spring AI demonstration project built with
Spring Boot and Java 21. It exposes three HTTP features — translation, sentiment analysis,
and a conversational "board game buddy" — all backed by a single Spring AI `ChatClient`
that talks to an OpenAI-compatible chat model.

## Build and runtime structure

- `pom.xml` uses `spring-boot-starter-parent` 3.4.2 and declares `java.version` 21.
  Spring AI is imported through `spring-ai-bom` 1.0.0 under `dependencyManagement`, so no
  Spring AI version is pinned on individual dependencies.
- Runtime dependencies: `spring-boot-starter-web` for the web layer,
  `spring-ai-starter-model-openai` for the OpenAI chat integration, and
  `spring-ai-tika-document-reader` for document parsing used by the summarize endpoint.
- The application entrypoint is `SpringAiDemoApplication` in
  `com.example.springaidemo`, a plain `@SpringBootApplication` that boots via
  `SpringApplication.run`. No additional component scanning or configuration classes exist.

## Package layout

Three feature packages sit under `com.example.springaidemo`, each with its own controllers
and records:

- `simpletranslator` — `TranslatorController` (`POST /translate`) plus the `Translation` record.
- `sentimentanalysis` — `SentimentController` (`POST /sentiment`) plus `TextInput` and
  `SentimentAnalysis` records.
- `boardgamebuddy` — `SummaryController` (`POST /summarize`, `POST /ask`), the
  `BoardGameService` interface with its `SpringAiBoardGameService` implementation, and the
  `Question`/`Answer` records.

Prompt templates live under `src/main/resources` as `.st` files: a single
`translationPromptTemplate.st`, a `sentimentSystemPrompt.st`, and a `promptTemplates/`
directory holding `systemPromptTemplate.st` and `summarizeSystemPrompt.st`.

## ChatClient configuration

Controllers and services never configure the model directly. They receive a
`ChatClient.Builder` via constructor injection and build a `ChatClient` from it
(`chatClientBuilder.build()`). `application.properties` supplies the model wiring:

- `spring.ai.openai.base-url=${AI_BASE_URL}` — the OpenAI-compatible endpoint.
- `spring.ai.openai.api-key=${AI_API_KEY}` — the credential.
- `spring.ai.openai.chat.options.model=${AI_MODEL:TokenHub/deepseek-v4-flash}` — the model
  name, defaulting to `TokenHub/deepseek-v4-flash` when the environment variable is unset.

Each feature builds its own `ChatClient` from the same builder and composes prompts from
the classpath `.st` templates; the shared prompt-building and structured-output mechanism is
described in the [ChatClient and Prompt Mechanism](../architecture/prompt-and-chatclient.md)
page.
