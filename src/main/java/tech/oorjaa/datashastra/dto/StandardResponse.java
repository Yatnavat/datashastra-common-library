package tech.oorjaa.datashastra.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Standardized response format for API responses.
 * Used to provide consistent response structure across the application.
 * 
 * @param <T> The type of data being returned in the response
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponse<T> {
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private boolean success;
    private String message;
    private T data;
    private String error;
    private String path;

    /**
     * Create a successful response with data
     */
    public static <T> StandardResponse<T> success(T data, String message) {
        return StandardResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Create a successful response without data
     */
    public static <T> StandardResponse<T> success(String message) {
        return StandardResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

    /**
     * Create an error response
     */
    public static <T> StandardResponse<T> error(String message, String error) {
        return StandardResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .build();
    }

    /**
     * Create an error response with path
     */
    public static <T> StandardResponse<T> error(String message, String error, String path) {
        return StandardResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .path(path)
                .build();
    }
}
