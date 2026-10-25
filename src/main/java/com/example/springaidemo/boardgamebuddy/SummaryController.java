package com.example.springaidemo.boardgamebuddy;

import org.springframework.ai.reader.tika.TikaDocumentReader;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SummaryController {

  private final BoardGameService boardGameService;
  
  public SummaryController(BoardGameService boardGameService) {
    this.boardGameService = boardGameService;
  }
  
  @PostMapping("/summarize")
  public Answer summarize(@RequestPart("rulesDocument") MultipartFile rulesDocument) {
    var reader = new TikaDocumentReader(rulesDocument.getResource());
    var rulesText = reader.get().getFirst().getText();
    return boardGameService.summarizeRules(rulesText);
  }

  @PostMapping("/ask")
  public Answer ask(
      @RequestBody Question question,
      @RequestHeader(value = "X-Conversation-Id", required = false) String conversationId) {
    if (question.gameTitle() == null || question.gameTitle().isBlank()
        || question.question() == null || question.question().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "gameTitle and question are required");
    }
    var id = conversationId == null || conversationId.isBlank()
        ? UUID.randomUUID().toString()
        : conversationId;
    return boardGameService.askQuestion(question, id);
  }
}
