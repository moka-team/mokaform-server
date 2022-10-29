package com.mokaform.mokaformserver.common.jwt;

import com.mokaform.mokaformserver.common.exception.AuthException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.util.RedisService;
import com.mokaform.mokaformserver.common.util.constant.RedisConstants;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {

    private final Jwt jwt;

    private final RedisService redisService;

    public JwtService(Jwt jwt, RedisService redisService) {
        this.jwt = jwt;
        this.redisService = redisService;
    }

    public Jwt.Claims verifyAccessToken(String accessToken) {
        String values = redisService.getValues(RedisConstants.LOGOUT.getPrefix() + accessToken);
        if (values != null) {
            throw new AuthException(CommonErrorCode.LOGGED_OUT_ACCESS_TOKEN);
        }

        return verify(accessToken);
    }

    public void logout(String token) {
        Jwt.Claims verifiedClaims = jwt.verify(token);
        long remainingExpirySeconds = verifiedClaims.exp.getTime() - new Date().getTime();
        redisService.setValues(RedisConstants.LOGOUT.getPrefix() + token, token, Duration.ofMillis(remainingExpirySeconds));
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }
}
