package com.xie.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.auth.mapper.SysRoleMapper;
import com.xie.auth.service.SysRoleService;
import com.xie.auth.service.SysUserRoleService;
import com.xie.model.system.SysRole;
import com.xie.model.system.SysUserRole;
import com.xie.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> findRoleById(Long id) { //显示所有角色和当前所选的用户具有的角色

        //1.查询所有角色
        List<SysRole> allRolesList = baseMapper.selectList(null);

        //2.查询当前用户的角色并取出其中的id
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id);
        List<SysUserRole> existSysRole = sysUserRoleService.list(wrapper);

        List<Long> list = new ArrayList<>();
        for(SysUserRole sysUserRole:existSysRole){
            Long sysRoleId = sysUserRole.getRoleId();
            list.add(sysRoleId);
        }

        //3.根据角色id与所有id进行比较
        List<SysRole> assignRoleList = new ArrayList<>();
        for(SysRole sysRole:allRolesList){
            if(list.contains(sysRole.getId())){
                assignRoleList.add(sysRole);
            }
        }

        HashMap<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList",assignRoleList);
        roleMap.put("allRolesList",allRolesList);

        return roleMap;
    }

    @Override
    public void doAssign(AssginRoleVo assginRoleVo) { //重新分配給所选用户的角色
        //1.删除该用户原来的角色
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);

        //2.重新分配角色
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for(Long roleid:roleIdList){
            if(StringUtils.isEmpty(roleid)){
                continue;
            }else{
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(assginRoleVo.getUserId());
                sysUserRole.setRoleId(roleid);
                sysUserRoleService.save(sysUserRole);
            }
        }
    }
}






















