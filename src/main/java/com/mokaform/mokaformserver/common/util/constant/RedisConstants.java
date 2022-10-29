package com.mokaform.mokaformserver.common.util.constant;

public enum RedisConstants {
    LOGOUT("auth:logout:access-token:");

    private final String prefix;

    RedisConstants(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
