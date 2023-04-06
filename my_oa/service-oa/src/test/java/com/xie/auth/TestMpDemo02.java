package com.xie.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xie.auth.mapper.SysRoleMapper;
import com.xie.auth.service.SysRoleService;
import com.xie.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMpDemo02 {

    @Autowired
    private SysRoleService sysRoles;

    //查询所有信息
    @Test
    public void selectList(){
        List<SysRole> list = sysRoles.list();
        System.out.println(list);
    }


}
