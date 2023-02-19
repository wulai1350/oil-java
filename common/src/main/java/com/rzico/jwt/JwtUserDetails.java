package com.rzico.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 存放账户信息
 *
 * @author gstripe@gmail.com
 */
public class JwtUserDetails implements UserDetails {

    private String userId;
    private String username;
    private String password;
    private String nickname;
    private String mchId;

    public JwtUserDetails(String userId, String username, String password, String nickname, String mchId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.mchId = mchId;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname(){
        return nickname;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    public String getMchId() {
        return mchId;
    }

    /**
     * 账号是否未过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否未锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭据是否未过期，例如密码，Token之类的
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 指示是否启用或禁用用户。禁用用户无法通过认证。
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }



}
