#!/usr/bin/env bash
# Run from the api-examples directory
curl -sS -i -X POST http://localhost:8080/summarize \
  -F 'rulesDocument=@sample-rules.txt'
