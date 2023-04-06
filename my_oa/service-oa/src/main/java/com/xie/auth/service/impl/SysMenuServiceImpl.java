package com.xie.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.auth.mapper.SysMenuMapper;
import com.xie.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.auth.service.SysRoleMenuService;
import com.xie.auth.utils.MenuHelper;
import com.xie.common.config.exception.XieException;
import com.xie.model.system.SysMenu;
import com.xie.model.system.SysRoleMenu;
import com.xie.vo.system.AssginMenuVo;
import com.xie.vo.system.MetaVo;
import com.xie.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author xie
 * @since 2023-03-29
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> findNodes() {
        //查询所有菜单列表
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        //构建树状结构
        List<SysMenu> list = MenuHelper.buildTree(sysMenuList);
        return list;
    }

    @Override
    public void removeMenuById(Long id) {
        //判断该菜单是否有子菜单
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);

        //如果有子菜单，那么这个菜单不能删除
        if (count > 0) {
            throw new XieException(201,"菜单不能删除");
        }else {
            baseMapper.deleteById(id);
        }

    }

    @Override
    public List<SysMenu> findMuneById(Long id) {
        //1.查找所有菜单且菜单的状态值为1
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("status",1);
        List<SysMenu> allMenuList = baseMapper.selectList(wrapper);

        //2.查找所选角色具有的菜单
        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        QueryWrapper<SysRoleMenu> roleMenuWrapper = new QueryWrapper<>();
        roleMenuWrapper.eq("role_id",id);
        List<SysRoleMenu> existlist = sysRoleMenuService.list(roleMenuWrapper);

        //3.取出具有的菜单的id
        List<Long> list = new ArrayList<>();
        for(SysRoleMenu sysRoleMenu:existlist){
            Long menuId = sysRoleMenu.getMenuId();
            list.add(menuId);
        }

        //4.将取到的id和所有的菜单做对比
        for(SysMenu sysMenu:allMenuList){
            if(list.contains(sysMenu.getId())){
                sysMenu.setSelect(true);
            }else{
                sysMenu.setSelect(false);
            }
        }

        //5.将所有的菜单转换成树状结构
        List<SysMenu> sysMenuList = MenuHelper.buildTree(allMenuList);
        return sysMenuList;

    }

    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //1.删除原来的菜单
        QueryWrapper<SysRoleMenu> sysRoleMenuWrapper = new QueryWrapper<>();
        sysRoleMenuWrapper.eq("role_id",assginMenuVo.getRoleId());
        sysRoleMenuService.remove(sysRoleMenuWrapper);
        //2.重新分配菜单
        List<Long> menuIdList = assginMenuVo.getMenuIdList();
        for(Long menuId:menuIdList){
            if(StringUtils.isEmpty(menuId)){
                continue;
            }else {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenuService.save(sysRoleMenu);
            }
        }
    }

    //根据userid构建菜单列表
    @Override
    public List<RouterVo> findMenuListByUserId(Long userId) {
        List<SysMenu> sysMenuList;
        //1.判断该id是否为管理员
        //如果是管理员取出所有菜单列表
        if(userId.longValue() == 1){

            QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("status",1);
            wrapper.orderByAsc("sort_value");
            sysMenuList = baseMapper.selectList(wrapper);
        }else {
            //如果不是管理员就根据多表关联来查询菜单列表
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        //2.将得到的菜单列表构建成树状结构
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        //3.将树形结构构建成要求的路由结构
        List<RouterVo> routerList = this.buildRouter(sysMenuTreeList);

        return routerList;
    }

    //树形结构构建成要求的路由结构
    private List<RouterVo> buildRouter(List<SysMenu> Menus) {

        //最终结果存放在列表里
        List<RouterVo> routerVoList = new ArrayList<>();
        //从sysMenuTreeList取出值赋给RouterVo
        for(SysMenu sysMenu:Menus){
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(sysMenu));
            router.setComponent(sysMenu.getComponent());
            router.setMeta(new MetaVo(sysMenu.getName(), sysMenu.getIcon()));
            // 下一层数据
            List<SysMenu> children = sysMenu.getChildren();
            if (sysMenu.getType().intValue() == 1) {
                // 加载隐藏路由
                List<SysMenu> hiddenMenuList = children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routerVoList.add(hiddenRouter);
                }
            }else {
                if (!CollectionUtils.isEmpty(children)) {
                    if(children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    // 递归
                    router.setChildren(buildRouter(children));
                }
            }
            routerVoList.add(router);
        }

        return routerVoList;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    //根据userid查询按钮列表
    @Override
    public List<String> findBtnListById(Long userId) {
        List<SysMenu> sysMenuList = new ArrayList<>();
        //判断是否为管理员
        if(userId.longValue() == 1){
            QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("status",1);
            sysMenuList = baseMapper.selectList(wrapper);
        }else {
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }

        //取出perms字段
        List<String> permList = sysMenuList.stream()
                .filter(item -> item.getType() == 2)
                .map(item -> item.getPerms())
                .collect(Collectors.toList());

        return permList;
    }
}













