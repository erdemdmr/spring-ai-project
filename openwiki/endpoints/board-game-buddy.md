---
type: endpoints
title: Board Game Buddy Endpoints
description: The boardgamebuddy module — POST /summarize multipart flow with Tika rule extraction and POST /ask conversational flow with Question validation, X-Conversation-Id, and ChatMemory conversation memory.
tags: [endpoints, spring-ai, tika, chat-memory, multipart]
verified:
  - by: openwiki/0.6.0
    at: 2026-10-26T13:41:05.092Z
sources:
  - id: openwiki-source-001b71df6456b887fac163df
    resource: repo://src/main/java/com/example/springaidemo/boardgamebuddy/BoardGameService.java
  - id: openwiki-source-3904e0f0c54cab1be8a077b9
    resource: repo://src/main/java/com/example/springaidemo/boardgamebuddy/Question.java
  - id: openwiki-source-3e208709f1812e5f162fa094
    resource: repo://src/main/java/com/example/springaidemo/boardgamebuddy/SpringAiBoardGameService.java
  - id: openwiki-source-05f1bc303e78d2c296788f75
    resource: repo://src/main/java/com/example/springaidemo/boardgamebuddy/SummaryController.java
generated: { by: "opencode", at: "2026-10-26T13:41:05.092Z" }
---

# Board Game Buddy Endpoints

The `boardgamebuddy` package adds a conversational assistant for tabletop games. It is the
only module with a service layer and multi-turn memory.

## Structure

- `BoardGameService` interface declares two operations: `summarizeRules(String)` and
  `askQuestion(Question, String conversationId)`.
- `SpringAiBoardGameService` implements it as a `@Service`, building one `ChatClient` and
  using the two prompt templates in `resources/promptTemplates/`.
- `SummaryController` exposes both operations as REST endpoints and owns HTTP concerns
  (multipart handling, header parsing, validation).
- Records: `Question` (inbound) and `Answer` (outbound).

## POST /summarize

`POST /summarize` accepts a multipart part named `rulesDocument`
(`@RequestPart("rulesDocument") MultipartFile`). The controller creates a
`TikaDocumentReader(rulesDocument.getResource())`, extracts the first document's text
(`reader.get().getFirst().getText()`), and passes it to `summarizeRules`. The service sends
the `summarizeSystemPrompt.st` system template and a user prompt
`"Summarize these rules: {gameRules}"`, returning an `Answer` via
`.call().entity(Answer.class)`. A matching curl example is `api-examples/summarize.request.sh`
using `sample-rules.txt` as the upload.

## POST /ask

`POST /ask` accepts a JSON `Question` body and an optional `X-Conversation-Id` header.

- The controller rejects blank `gameTitle` or `question` with HTTP 400
  (`ResponseStatusException(BAD_REQUEST, "gameTitle and question are required")`).
- When the header is absent or blank, the controller generates a fresh
  `UUID.randomUUID()` so each anonymous caller starts a new conversation.
- `Question` normalizes its own `language` in the compact constructor: blank/null defaults
  to `"English"` (`DEFAULT_LANGUAGE`), and a two-argument constructor exists for callers
  that omit language.
- The service renders the `systemPromptTemplate.st` system prompt with `{gameTitle}` and
  `{targetLanguage}`, sends the raw question text as the user message, and attaches the
  `ChatMemory.CONVERSATION_ID` advisor param to scope message history by conversation.

## Conversation lifecycle

Because history is keyed by `X-Conversation-Id`, repeating the same conversation ID in later
`/ask` calls lets the model answer follow-ups that rely on earlier context. Omitting the
header yields a stateless one-off answer under a fresh UUID. `ChatMemory` persistence is
provided by the Spring AI chat client; no manual history store is managed by this module.

## Failure behavior

- Blank `gameTitle`/`question` → 400 before any model call.
- No explicit exception handling exists around `TikaDocumentReader`; a malformed upload
  surfaces as a standard Spring error response.

## Representative artifacts

`api-examples/ask.request.sh`/`ask.response.txt` show a typical single-turn chess question;
`api-examples/sample-rules.txt` is the Tic-Tac-Toe rules fixture used by
`summarize.request.sh` and yields the summarized `Answer` in `summarize.response.txt`.
