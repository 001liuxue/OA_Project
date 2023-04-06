package com.xie.wechat.service;

import com.xie.model.wechat.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author xie
 * @since 2023-04-04
 */
public interface MenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
