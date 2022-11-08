package com.mokaform.mokaformserver.common.util.constant;

public enum EmailType {
    SIGN_IN(":sign-in", "모카폼 회원가입 인증번호입니다.");

    private final String redisPrefix;
    private final String subject;

    EmailType(String redisPrefix, String subject) {
        this.redisPrefix = redisPrefix;
        this.subject = subject;
    }

    public String getRedisPrefix() {
        return redisPrefix;
    }

    public String getSubject() {
        return subject;
    }
}
