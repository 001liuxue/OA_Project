package com.xie.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.model.system.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author xie
 * @since 2023-03-29
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findMenuListByUserId(Long userId);
}
