---
type: operations
title: Configuration and API Consumption
description: Operational surface of the demo — the application.properties env-var contract (AI_BASE_URL, AI_API_KEY, AI_MODEL), OpenAI-compatible model wiring, and ready-made consumption artifacts (curl scripts, sample responses, Postman collection and environment).
tags: [operations, configuration, environment, postman, curl]
verified:
  - by: openwiki/0.6.0
    at: 2026-10-26T13:41:05.092Z
sources:
  - id: openwiki-source-2b51e1d99864aae88c3d83cb
    resource: repo://api-examples/ask.request.sh
  - id: openwiki-source-52b747bfeddd5a55307b7eee
    resource: repo://api-examples/ask.response.txt
  - id: openwiki-source-746e7e74a4c7385953f3aeb6
    resource: repo://api-examples/sentiment.request.sh
  - id: openwiki-source-c0b31f403af6c5fee3bb83e1
    resource: repo://api-examples/sentiment.response.txt
  - id: openwiki-source-d02ccd513833e4399fc58b11
    resource: repo://api-examples/summarize.request.sh
  - id: openwiki-source-31bb4615e43aa5e321c63845
    resource: repo://api-examples/summarize.response.txt
  - id: openwiki-source-cc3786fe775b43c718e6b35f
    resource: repo://api-examples/translate.request.sh
  - id: openwiki-source-913a73be4229e0620d822328
    resource: repo://api-examples/translate.response.txt
  - id: openwiki-source-99f668f71344109adfdab724
    resource: repo://postman/spring-ai-demo.local.postman_environment.json
  - id: openwiki-source-266a0a12b8359b0e038becfa
    resource: repo://postman/spring-ai-demo.postman_collection.json
  - id: openwiki-source-ece093ad10b91f3da0b7e34f
    resource: repo://src/main/resources/application.properties
generated: { by: "opencode", at: "2026-10-26T13:41:05.092Z" }
---

# Configuration and API Consumption

## Runtime configuration

All model connectivity is configured in `src/main/resources/application.properties` through
three environment variables:

- `AI_BASE_URL` → `spring.ai.openai.base-url` — the OpenAI-compatible API endpoint.
- `AI_API_KEY` → `spring.ai.openai.api-key` — the credential used for chat calls.
- `AI_MODEL` (optional, defaults to `TokenHub/deepseek-v4-flash`) →
  `spring.ai.openai.chat.options.model` — the chat model name.

Because each property has no hardcoded fallback except the model default, the application
requires `AI_BASE_URL` and `AI_API_KEY` to be present at startup for chat calls to succeed.
The model name can be overridden per environment without code changes.

## Consuming the endpoints

### curl examples

The `api-examples/` directory contains one request/response pair per endpoint, intended to be
run against `http://localhost:8080`:

- `translate.request.sh` / `translate.response.txt` — POST /translate.
- `sentiment.request.sh` / `sentiment.response.txt` — POST /sentiment.
- `summarize.request.sh` / `summarize.response.txt` — POST /summarize (multipart upload of
  `sample-rules.txt`).
- `ask.request.sh` / `ask.response.txt` — POST /ask with an `X-Conversation-Id` header.

Each `*response.txt` file records the HTTP status line and JSON body observed from a real
run; `sample-rules.txt` is the Tic-Tac-Toe fixture uploaded by the summarize example.

### Postman

`postman/spring-ai-demo.postman_collection.json` defines the four requests (Translate,
Sentiment Analysis, Ask Question, Summarize) with the same payloads as the curl scripts and
basic test scripts (e.g. status 200, score within -1..1). It references a
`{{baseUrl}}` variable provided by `postman/spring-ai-demo.local.postman_environment.json`,
which sets `baseUrl` to `http://localhost:8080` for the local environment.
