# spring-ai-demo

A minimal Spring AI demonstration application. It exposes three HTTP features — translation,
sentiment analysis, and a conversational "board game buddy" — all backed by a single Spring AI
`ChatClient` talking to an OpenAI-compatible chat model.

## Requirements

- Java 21
- Maven
- `AI_BASE_URL` and `AI_API_KEY` environment variables (an OpenAI-compatible endpoint and
  credential)

## Build and run

```bash
export AI_BASE_URL=<your-openai-compatible-base-url>
export AI_API_KEY=<your-api-key>
# optional, defaults to TokenHub/deepseek-v4-flash
export AI_MODEL=<model-name>

mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## Endpoints

| Endpoint | Method | Body | Description |
|---|---|---|---|
| `/translate` | POST | `{"text": "...", "sourceLanguage": "English", "targetLanguage": "French"}` | Translate text between languages |
| `/sentiment` | POST | `{"text": "..."}` | Score sentiment between -1 (negative) and 1 (positive) with an explanation |
| `/summarize` | POST | multipart part `rulesDocument` | Summarize a board-game rules document (parsed with Tika) |
| `/ask` | POST | `{"gameTitle": "Chess", "question": "..."}` + optional `X-Conversation-Id` header | Conversational Q&A about a board game, with per-conversation memory |

## Configuration

Model wiring is environment-driven in `src/main/resources/application.properties`:

- `AI_BASE_URL` → `spring.ai.openai.base-url`
- `AI_API_KEY` → `spring.ai.openai.api-key`
- `AI_MODEL` → `spring.ai.openai.chat.options.model` (default `TokenHub/deepseek-v4-flash`)

## Try it

Ready-made curl scripts and captured responses for every endpoint live in `api-examples/`
(e.g. `api-examples/translate.request.sh`). A Postman collection is under `postman/`.

```bash
bash api-examples/translate.request.sh
```

## Project structure

- `src/main/java/com/example/springaidemo/simpletranslator/` — `/translate`
- `src/main/java/com/example/springaidemo/sentimentanalysis/` — `/sentiment`
- `src/main/java/com/example/springaidemo/boardgamebuddy/` — `/summarize`, `/ask`
- `src/main/resources/` — ST4 prompt templates (`.st`) and `application.properties`

## Documentation

Generated documentation lives in the `openwiki/` directory; start at
[`openwiki/quickstart.md`](openwiki/quickstart.md).
