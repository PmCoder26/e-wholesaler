package com.parimal.e_wholesaler.shop_service.security.authorization;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OwnerSecurity {

    public boolean isAuthorizedOwner(Long ownerId) {
        if (ownerId == null) return false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Safe check for Principal type to avoid ClassCastException
        if (auth == null || !(auth.getPrincipal() instanceof Claims principleClaims)) {
            return false;
        }

        Object rawOwnerId = principleClaims.get("owner_id");

        // Check if the claim is any type of Number
        if (rawOwnerId instanceof Number principleOwnerId) {
            // Compare long values directly
            return principleOwnerId.longValue() == ownerId;
        }

        return false;
    }
}
