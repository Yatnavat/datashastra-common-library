package tech.oorjaa.reporting.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.oorjaa.reporting.models.MemoryStatus;

@RestController("/foo")
public class FooController {

    @GetMapping("/memory-status")
    public ResponseEntity<MemoryStatus> getMemoryStatus() {
        return ResponseEntity.ok(new MemoryStatus());
    }
}
