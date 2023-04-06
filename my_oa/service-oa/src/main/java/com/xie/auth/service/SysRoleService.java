package com.xie.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.model.system.SysRole;
import com.xie.vo.system.AssginRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    Map<String,Object> findRoleById(Long id);

    void doAssign(AssginRoleVo assginRoleVo);
}
