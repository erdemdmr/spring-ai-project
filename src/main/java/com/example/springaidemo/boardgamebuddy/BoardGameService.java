package com.example.springaidemo.boardgamebuddy;

public interface BoardGameService {
    Answer summarizeRules(String text);
    Answer askQuestion(Question question, String conversationId);
}
