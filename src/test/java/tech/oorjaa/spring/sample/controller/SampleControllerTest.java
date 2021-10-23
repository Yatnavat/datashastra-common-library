package tech.oorjaa.spring.sample.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import tech.oorjaa.spring.sample.domain.MemoryStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getMemoryStatus_success() {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/memory-status",
                        MemoryStatus.class))
                .isNotNull();
    }
}
