package com.mokaform.mokaformserver.user.dto.request;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

@Getter
public class LocalLoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    protected LocalLoginRequest() {
    }

    public LocalLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("email", email)
                .append("password", password)
                .toString();
    }
}
