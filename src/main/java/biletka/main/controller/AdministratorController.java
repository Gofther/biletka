package biletka.main.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dGlja2V0QWRtaW4=")
@RequiredArgsConstructor
@Tag(name = "Контроллер администрации", description = "Всё, что связано с администрированием")
public class AdministratorController {
    @GetMapping
    public ResponseEntity<?> getTest() {
        return ResponseEntity.ok("ok");
    }
}
