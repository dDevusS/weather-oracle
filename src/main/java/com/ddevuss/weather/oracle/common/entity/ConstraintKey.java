package com.ddevuss.weather.oracle.common.entity;

import lombok.Getter;

@Getter
public enum ConstraintKey {
    USER_LOGIN_UNQ("users_login_key"),
    LOCATION_COORDINATE_UNIQUE("idx_base_target");

    private final String key;

    ConstraintKey(String key) {
        this.key = key;
    }
}
