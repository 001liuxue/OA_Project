package com.xie.auth.service.impl;

import com.xie.auth.service.SysMenuService;
import com.xie.common.config.exception.XieException;
import com.xie.common.result.ResultEnum;
import com.xie.model.system.SysUser;
import com.xie.security.custom.CustomUser;
import com.xie.auth.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//loadUserByUsername方法自定义
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }

//        根据用户id查询权限数据
        List<String> btnListById = sysMenuService.findBtnListById(sysUser.getId());
//        将得到的权限数据封装成最终形式
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        for (String s : btnListById){
            authList.add(new SimpleGrantedAuthority(s.trim()));
        }

        return new CustomUser(sysUser, authList);
    }
}