package com.xie.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.model.system.SysMenu;
import com.xie.vo.system.AssginMenuVo;
import com.xie.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author xie
 * @since 2023-03-29
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> findMuneById(Long id);

    void doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> findMenuListByUserId(Long userId);

    List<String> findBtnListById(Long userId);
}
