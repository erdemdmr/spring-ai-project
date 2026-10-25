---
type: endpoints
title: Sentiment Analysis Endpoint
description: The POST /sentiment endpoint — TextInput in, a builder-level defaultSystem prompt defining the scoring contract, user text param, and structured SentimentAnalysis (explanation + score between -1 and 1) entity response.
tags: [endpoints, spring-ai, sentiment, structured-output]
verified:
  - by: openwiki/0.6.0
    at: 2026-10-26T13:41:05.092Z
sources:
  - id: openwiki-source-e27f7b35113aa1499694cb21
    resource: repo://src/main/java/com/example/springaidemo/sentimentanalysis/SentimentAnalysis.java
  - id: openwiki-source-97e7fd98c171269b1cb10364
    resource: repo://src/main/java/com/example/springaidemo/sentimentanalysis/SentimentController.java
  - id: openwiki-source-31c03ad5c6ecbab39377676c
    resource: repo://src/main/java/com/example/springaidemo/sentimentanalysis/TextInput.java
  - id: openwiki-source-cc38d44d430780dd660c29b4
    resource: repo://src/main/resources/sentimentSystemPrompt.st
generated: { by: "opencode", at: "2026-10-26T13:41:05.092Z" }
---

# Sentiment Analysis Endpoint

The `sentimentanalysis` package exposes a single `POST /sentiment` endpoint that scores the
sentiment of a text with an OpenAI-compatible chat model.

## Types

- `TextInput` (inbound): a record holding the user text (`String text`).
- `SentimentAnalysis` (outbound): a record holding `String explanation` and `double score`.
- `SentimentController`: a `@RestController` owning the endpoint.

## Endpoint flow

`POST /sentiment` accepts a JSON `TextInput` body. The controller builds its `ChatClient`
once, at construction time, by injecting `ChatClient.Builder` and the classpath
`sentimentSystemPrompt.st` resource and calling:

```java
chatClientBuilder.defaultSystem(sentimentSystemPrompt).build()
```

`defaultSystem` makes the sentiment instructions the model's system message on every call.
The request handler then sends the user text as a parameterized user message
(`"User text: {text}"`) and returns `.call().entity(SentimentAnalysis.class)`.

## Scoring contract

The system prompt (`sentimentSystemPrompt.st`) instructs the model to behave as a sentiment
analysis tool: analyze the given text and provide a score between -1 (very negative) and 1
(very positive), plus a brief explanation of the reasoning. The `entity(SentimentAnalysis.class)`
binding asks the model for JSON matching `{explanation, score}`, so the endpoint response is
a JSON object with those fields.

## Representative request

`api-examples/sentiment.request.sh` posts `{"text":"I love this, it is wonderful"}`; the
sample response in `api-examples/sentiment.response.txt` returns a positive explanation with
`"score":1.0`.
