package org.apereo.cas.ticket.accesstoken;

import org.apereo.cas.AbstractOAuth20Tests;
import org.apereo.cas.authentication.CoreAuthenticationTestUtils;
import org.apereo.cas.token.JwtBuilder;

import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link OAuth20JwtBuilderTests}.
 *
 * @author Misagh Moayyed
 * @since 6.2.0
 */
@Tag("OAuthToken")
@SuppressWarnings("JavaUtilDate")
class OAuth20JwtBuilderTests extends AbstractOAuth20Tests {
    @Test
    void verifyJwt() {
        servicesManager.save(getRegisteredService("clientid-jwt", "secret-jwt"));
        val service = CoreAuthenticationTestUtils.getService("https://service.example.com");
        val request = JwtBuilder.JwtRequest.builder()
            .issueDate(new Date())
            .jwtId(service.getId())
            .serviceAudience(Set.of("clientid-jwt"))
            .subject("casuser")
            .issuer(casProperties.getServer().getPrefix())
            .build();
        val jwt = accessTokenJwtBuilder.build(request);
        assertNotNull(jwt);
    }

    @Test
    void verifyBadJwt() {
        assertThrows(IllegalArgumentException.class, () -> JwtBuilder.parse("badly-formatted-jwt"));
    }
}
