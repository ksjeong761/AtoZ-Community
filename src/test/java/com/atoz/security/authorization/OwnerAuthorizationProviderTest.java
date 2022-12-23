package com.atoz.security.authorization;

import com.atoz.security.authentication.helper.CustomWithMockUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class OwnerAuthorizationProviderTest {

    private final OwnerAuthorizationProvider sut = new OwnerAuthorizationProvider();

    private final String REQUESTER_USER_ID = "requesterUserId";

    @Test
    @CustomWithMockUser(username = REQUESTER_USER_ID)
    void isOwner_SecurityContextHolder에_저장된_사용자이면_true를_반환한다() {
        String resourceOwnerUserId = REQUESTER_USER_ID;


        boolean result = sut.isOwner(resourceOwnerUserId);


        assertTrue(result);
    }

    @Test
    @CustomWithMockUser(username = REQUESTER_USER_ID)
    void isOwner_SecurityContextHolder에_저장된_사용자가_아니면_false를_반환한다() {
        String resourceOwnerUserId = "otherUserId";


        boolean result = sut.isOwner(resourceOwnerUserId);


        assertFalse(result);
    }
}
