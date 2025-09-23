package tech.oorjaa.datashastra.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Error response structure for API errors
 *
 * @param status    HTTP status code
 * @param message   Error message
 * @param timestamp Timestamp when the error occurred
 * @param path      Request path
 * @param details   Additional error details (optional)
 */
public record ErrorResponse(int status, String message, LocalDateTime timestamp, String path,
                            Map<String, Object> details) {
}
