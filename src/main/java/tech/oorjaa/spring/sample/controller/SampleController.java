package tech.oorjaa.spring.sample.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.oorjaa.spring.sample.domain.MemoryStatus;

@RestController("/")
public class SampleController {

    @GetMapping("memory-status")
    public ResponseEntity<MemoryStatus> getMemoryStatus() {
        return ResponseEntity.ok(new MemoryStatus());
    }
}
