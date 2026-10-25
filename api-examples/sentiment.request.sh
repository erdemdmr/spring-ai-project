#!/usr/bin/env bash
curl -sS -i -X POST http://localhost:8080/sentiment \
  -H 'Content-Type: application/json' \
  -d '{"text":"I love this, it is wonderful"}'
