package com.mokaform.mokaformserver.common.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.AuthException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.util.RedisService;
import com.mokaform.mokaformserver.common.util.constant.RedisConstants;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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

    public String reissueAccessToken(String accessToken, String refreshToken) {
        if (!jwt.isExpiredToken(accessToken)) {
            throw new ApiException(CommonErrorCode.NOT_EXPIRED_ACCESS_TOKEN);
        }

        Jwt.Claims claims = getClaims(accessToken);
        checkRefreshToken(claims.email, refreshToken);
        return getAccessToken(claims.email, claims.roles);
    }

    private void checkRefreshToken(String email, String refreshToken) {
        String redisToken = redisService.getValues(MessageFormat.format("{0}{1}",
                RedisConstants.LOGIN.getPrefix(), email));
        if (redisToken == null || !refreshToken.equals(redisToken)) {
            throw new ApiException(CommonErrorCode.ILLEGAL_REFRESH_TOKEN);
        }
    }

    private String getAccessToken(String email, String[] roles) {
        return jwt.signAccessToken(Jwt.Claims.from(email, roles));
    }

    private Jwt.Claims getClaims(String accessToken) {
        DecodedJWT decodedJWT = com.auth0.jwt.JWT.decode(accessToken);
        return new Jwt.Claims(decodedJWT);
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }
}
