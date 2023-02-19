package com.rzico.core.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/6/17.
 */
@Component
public class Md5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return DigestUtils.md5Hex(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return StringUtils.equals(
                DigestUtils.md5Hex(rawPassword.toString()),
                encodedPassword);
    }
}
