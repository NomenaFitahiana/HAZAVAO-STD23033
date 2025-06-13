package com.example.demo.endpoint.rest.controller.health;

import com.example.demo.PojaGenerated;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PojaGenerated
@RestController
@AllArgsConstructor
public class HazavaoController {

  public static final ResponseEntity<String> OK = new ResponseEntity<>("OK", HttpStatus.OK);
  public static final ResponseEntity<String> KO =
      new ResponseEntity<>("KO", HttpStatus.INTERNAL_SERVER_ERROR);

  private final RestTemplate restTemplate;

  @Value("${OPENAI_API_KEY}")
  private final String OPENAI_API_KEY;

  @GetMapping("/hazavao")
  public ResponseEntity<String> hazavao(@RequestParam String teny) {

    Map<String, Object> body =
        Map.of(
            "model",
            "gpt-3.5-turbo",
            "messages",
            List.of(Map.of("role", "user", "content", "Inona no dikan'ity teny ity : " + teny)));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(OPENAI_API_KEY);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    String url = "https://api.openai.com/v1/chat/completions";

    try {
      ResponseEntity<String> response =
          restTemplate.postForEntity(url, requestEntity, String.class);
      if (response.getStatusCode() == HttpStatus.OK) {

        return ResponseEntity.ok(response.getBody());
      } else {
        return ResponseEntity.status(response.getStatusCode()).body("Erreur API OpenAI");
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
    }
  }
}
