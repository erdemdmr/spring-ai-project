---
type: guide
title: Spring AI Demo Quickstart
description: Orientation and task-routing map for the spring-ai-demo repository — what the app is, its three feature areas, how to build and run it, and which wiki page covers each concern.
tags: [quickstart, orientation, spring-ai, demo]
verified:
  - by: openwiki/0.6.0
    at: 2026-10-26T13:41:05.092Z
sources:
  - id: openwiki-source-2b51e1d99864aae88c3d83cb
    resource: repo://api-examples/ask.request.sh
  - id: openwiki-source-746e7e74a4c7385953f3aeb6
    resource: repo://api-examples/sentiment.request.sh
  - id: openwiki-source-d02ccd513833e4399fc58b11
    resource: repo://api-examples/summarize.request.sh
  - id: openwiki-source-cc3786fe775b43c718e6b35f
    resource: repo://api-examples/translate.request.sh
  - id: openwiki-source-2355f81d7cf522f8dbdaabd4
    resource: repo://pom.xml
  - id: openwiki-source-05f1bc303e78d2c296788f75
    resource: repo://src/main/java/com/example/springaidemo/boardgamebuddy/SummaryController.java
  - id: openwiki-source-97e7fd98c171269b1cb10364
    resource: repo://src/main/java/com/example/springaidemo/sentimentanalysis/SentimentController.java
  - id: openwiki-source-10f756c6a53d6e6e477e1fd4
    resource: repo://src/main/java/com/example/springaidemo/simpletranslator/TranslatorController.java
  - id: openwiki-source-ece093ad10b91f3da0b7e34f
    resource: repo://src/main/resources/application.properties
generated: { by: "opencode", at: "2026-10-26T13:41:05.092Z" }
---

# Spring AI Demo Quickstart

`spring-ai-demo` is a small Spring AI demonstration application. It exposes three HTTP
features, all backed by one OpenAI-compatible chat model through Spring AI `ChatClient`.

## Feature areas

- **Translation** (`simpletranslator` package) — `POST /translate`; translates text between
  languages using a parameterized user prompt template. See
<!-- openwiki: broken internal link [../endpoints/translation.md] file "../endpoints/translation.md" does not exist. Fix the href or restore the target, then delete this comment. -->
  [Translation Endpoint](../endpoints/translation.md).
- **Sentiment analysis** (`sentimentanalysis` package) — `POST /sentiment`; scores text
  between -1 and 1 with an explanation, driven by a `defaultSystem` prompt. See
<!-- openwiki: broken internal link [../endpoints/sentiment-analysis.md] file "../endpoints/sentiment-analysis.md" does not exist. Fix the href or restore the target, then delete this comment. -->
  [Sentiment Analysis Endpoint](../endpoints/sentiment-analysis.md).
- **Board game buddy** (`boardgamebuddy` package) — `POST /summarize` (multipart rules
  document parsed by Tika) and `POST /ask` (conversational Q&A keyed by conversation ID).
  The only module with a service layer and multi-turn memory. See
<!-- openwiki: broken internal link [../endpoints/board-game-buddy.md] file "../endpoints/board-game-buddy.md" does not exist. Fix the href or restore the target, then delete this comment. -->
  [Board Game Buddy Endpoints](../endpoints/board-game-buddy.md).

The shared `ChatClient` prompt, template, and structured-output mechanism is documented in
<!-- openwiki: broken internal link [../architecture/prompt-and-chatclient.md] file "../architecture/prompt-and-chatclient.md" does not exist. Fix the href or restore the target, then delete this comment. -->
[ChatClient and Prompt Mechanism](../architecture/prompt-and-chatclient.md); build and
<!-- openwiki: broken internal link [../architecture/overview.md] file "../architecture/overview.md" does not exist. Fix the href or restore the target, then delete this comment. -->
package structure in [Architecture Overview](../architecture/overview.md).

## Building and running

- Java 21 and Maven are required; the project uses Spring Boot 3.4.2 with the Spring AI 1.0.0
  BOM (see `pom.xml`).
- The app is a standard Spring Boot application booted from `SpringAiDemoApplication`
  (`com.example.springaidemo`), e.g. `mvn spring-boot:run`.
- Before startup, export `AI_BASE_URL` and `AI_API_KEY`; optionally set `AI_MODEL` (defaults
  to `TokenHub/deepseek-v4-flash`). Details in
<!-- openwiki: broken internal link [../operations/configuration-and-api-consumption.md] file "../operations/configuration-and-api-consumption.md" does not exist. Fix the href or restore the target, then delete this comment. -->
  [Configuration and API Consumption](../operations/configuration-and-api-consumption.md).
- Ready-made curl scripts and a Postman collection for all four endpoints live under
  `api-examples/` and `postman/`, with captured sample responses.

## Task routing

- Adding a new LLM-powered endpoint: follow the existing pattern — inject `ChatClient.Builder`,
  build a `ChatClient`, load a `.st` prompt template under `resources/`, and terminate with
  `.call().entity(YourRecord.class)`.
- Changing prompts: edit the `.st` templates under `src/main/resources/`
  (`translationPromptTemplate.st`, `sentimentSystemPrompt.st`, `promptTemplates/`).
- Changing model/provider wiring: edit `src/main/resources/application.properties` env mapping
  or the `AI_MODEL`/`AI_BASE_URL`/`AI_API_KEY` environment values.
