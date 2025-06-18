package tech.oorjaa.demoservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Response containing authentication tokens")
public class LoginResponse {
    private Long userId;
    private String username;
    private String phoneNumber;
    private String email;
    private boolean existingUser;
    private String sessionToken;
    private String firstName;
    private String lastName;
    private String userType;
    private boolean validUserDetails;
    private String refreshToken;
    private long sessionExpire;
    private long refreshExpire;
    private List<String> userRoles;
    private List<String> authorities;
    private List<String> scopes;
    private Long companyId;
    private String companyName;
}
