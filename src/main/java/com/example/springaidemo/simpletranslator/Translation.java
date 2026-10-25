package com.example.springaidemo.simpletranslator;

public record Translation(
    String text,
    String sourceLanguage,
    String targetLanguage) {
}
