/**
 * 版权声明：厦门中软海晟信息技术有限公司 版权所有 违者必究
 * 日    期：2019-02-21
 */
package com.rzico.core.security;

import com.alibaba.fastjson.JSON;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysUserService;
import com.rzico.jwt.JwtUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * security查询用户
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2019-02-21
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserService sysUserService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (s == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", s));
        } else {
             SysUser sysUser = sysUserService.login(s);
            if(sysUser == null) {
                throw new UsernameNotFoundException(String.format("No user found with username '%s'.", s));
            }
            return new JwtUserDetails(
                    sysUser.getId(),
                    sysUser.getUsername(),
                    passwordEncoder.encode(sysUser.getPassword()),
                    sysUser.getNickname(),
                    sysUser.getMchId()
            );
        }
    }

    public String json2Str(Object object){
        return JSON.toJSONString(object);
    }
}
