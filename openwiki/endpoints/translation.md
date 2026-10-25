---
type: endpoints
title: Translation Endpoint
description: The POST /translate endpoint — Translation record in/out, classpath user prompt template, parameterized source/target language and text, and a structured Translation entity response.
tags: [endpoints, spring-ai, translation, prompt-template]
verified:
  - by: openwiki/0.6.0
    at: 2026-10-26T13:41:05.092Z
sources:
  - id: openwiki-source-6f6d2cee5d7615e4d8cf6b9a
    resource: repo://src/main/java/com/example/springaidemo/simpletranslator/Translation.java
  - id: openwiki-source-10f756c6a53d6e6e477e1fd4
    resource: repo://src/main/java/com/example/springaidemo/simpletranslator/TranslatorController.java
  - id: openwiki-source-4a1159849f8f4906019f8945
    resource: repo://src/main/resources/translationPromptTemplate.st
generated: { by: "opencode", at: "2026-10-26T13:41:05.092Z" }
---

# Translation Endpoint

The `simpletranslator` package exposes `POST /translate`, which translates a text between
two languages using an OpenAI-compatible chat model.

## Types

- `Translation` (inbound and outbound): a record with `String text`, `String sourceLanguage`,
  and `String targetLanguage`.
- `TranslatorController`: a `@RestController` owning the endpoint.

## Endpoint flow

`POST /translate` accepts a JSON `Translation` body. The controller injects `ChatClient.Builder`
and the classpath user prompt `translationPromptTemplate.st` via `@Value`, building a
`ChatClient` at construction. Each request renders the user prompt template with the request
fields as parameters:

- `{sourceLanguage}` ← `request.sourceLanguage()`
- `{targetLanguage}` ← `request.targetLanguage()`
- `{sourceText}` ← `request.text()`

and returns `.call().entity(Translation.class)`.

## Prompt template

`translationPromptTemplate.st` instructs the model to act as an expert translator fluent in
`{targetLanguage}` and to translate the given text from `{sourceLanguage}` to `{targetLanguage}`.
Because the same `Translation` record is used for both request and response, the model's JSON
output carries the translated `text` together with the source/target languages, and the
endpoint echoes them back unchanged.

## Representative request

`api-examples/translate.request.sh` posts
`{"text":"Hello, how are you?","sourceLanguage":"English","targetLanguage":"French"}`; the
sample response in `api-examples/translate.response.txt` returns
`{"text":"Bonjour, comment allez-vous ?", ...}`.
