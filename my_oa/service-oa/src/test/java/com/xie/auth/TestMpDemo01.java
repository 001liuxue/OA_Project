package com.xie.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xie.auth.mapper.SysRoleMapper;
import com.xie.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMpDemo01 {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    //查询所有信息
    @Test
    public void selectList(){
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        System.out.println(sysRoles);
    }

    //更改信息
    @Test
    public void updateById(){
        //先查询数据
        SysRole sysRole = sysRoleMapper.selectById(2);
        //修改数据
        sysRole.setRoleName("部门经理");
        //返回更改条数
        int row = sysRoleMapper.updateById(sysRole);
        System.out.println(row);
    }

    //条件查询
    @Test
    public void testQuery(){
        //创建Querywrapper对象，加入条件
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", "部门经理");
        //查询
        List<SysRole> sysRoles = sysRoleMapper.selectList(wrapper);
        System.out.println(sysRoles);
    }

}
