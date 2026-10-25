#!/usr/bin/env bash
curl -sS -i -X POST http://localhost:8080/translate \
  -H 'Content-Type: application/json' \
  -d '{"text":"Hello, how are you?","sourceLanguage":"English","targetLanguage":"French"}'
