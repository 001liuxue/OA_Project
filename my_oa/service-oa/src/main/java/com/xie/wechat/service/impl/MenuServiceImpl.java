package com.xie.wechat.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xie.model.wechat.Menu;
import com.xie.vo.wechat.MenuVo;
import com.xie.wechat.mapper.MenuMapper;
import com.xie.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author xie
 * @since 2023-04-04
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private WxMpService wxMpService;

    @Override
    public List<MenuVo> findMenuInfo() {
        //最终菜单
        List<MenuVo> menuVos = new ArrayList<>();
        //1.查询所有菜单
        List<Menu> menusList = baseMapper.selectList(null);
        //2.遍历每个一级菜单
        List<Menu> oneMenu = menusList.stream().filter(menu -> menu.getParentId().longValue() == 0).collect(Collectors.toList());
        for(Menu menu : oneMenu){
            //将menu类型转换成menuVo类型
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(menu,menuVo);

            List<MenuVo> twoMenuVos = new ArrayList<>();
            //3.查询每个一级菜单下面的二级菜单
            List<Menu> twoMenu = menusList.stream().filter(item -> item.getParentId().longValue() == menu.getId()).collect(Collectors.toList());
            for(Menu m : twoMenu) {
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(m, twoMenuVo);
                //4.将二级菜单放入一级菜单中
                twoMenuVos.add(twoMenuVo);
            }
            menuVo.setChildren(twoMenuVos);
            //5.将整理好的一级菜单放入最终菜单中
            menuVos.add(menuVo);
        }




        return menuVos;
    }

    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if(CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                one.put("url", "http://da.gz2vip.91tunnel.com/#"+oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if(twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //H5页面地址
                        view.put("url", "http://da.gz2vip.91tunnel.com/#"+twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        try {
            wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
