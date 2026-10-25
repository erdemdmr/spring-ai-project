#!/usr/bin/env bash
curl -sS -i -X POST http://localhost:8080/ask \
  -H 'Content-Type: application/json' \
  -H 'X-Conversation-Id: demo-conversation' \
  -d '{"gameTitle":"Chess","question":"How does a knight move?","language":"English"}'
