# Files

- [Architecture Overview](overview.md) - System structure of the Spring AI demo — Spring Boot 3.4/Java 21 build, application entrypoint, package layout, and how a single ChatClient is configured against an OpenAI-compatible model.
- [ChatClient and Prompt Mechanism](prompt-and-chatclient.md) - The shared Spring AI mechanism behind all endpoints — ChatClient.Builder construction, ST prompt templates with parameters, defaultSystem prompts, structured output via .entity(Record.class), and message-history advisors keyed by conversation ID.
