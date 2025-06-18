package tech.oorjaa.demoservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Custom converter for extracting and converting Keycloak roles from JWT token to Spring Security authorities
 */
@Component
public class KeycloakRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    /**
     * Converts JWT token to AbstractAuthenticationToken by extracting roles from realm_access and resource_access claims
     *
     * @param jwt The JWT token to convert
     * @return JwtAuthenticationToken containing the original JWT and extracted authorities
     */
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Extract roles from realm_access.roles claim
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            authorities.addAll(roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .toList());
        }

        // Extract roles from resource_access.<client>.roles claims
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            resourceAccess.forEach((clientId, clientRoles) -> {
                if (clientRoles instanceof Map) {
                    Map<String, Object> clientRolesMap = (Map<String, Object>) clientRoles;
                    if (clientRolesMap.containsKey("roles")) {
                        List<String> roles = (List<String>) clientRolesMap.get("roles");
                        authorities.addAll(roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + clientId.toUpperCase() + "_" + role.toUpperCase()))
                                .toList());
                    }
                }
            });
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}