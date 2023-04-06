package com.xie.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xie.auth.service.SysMenuService;
import com.xie.auth.service.SysUserService;
import com.xie.common.config.exception.XieException;
import com.xie.common.jwt.JwtHelper;
import com.xie.common.result.Result;
import com.xie.common.utils.MD5;
import com.xie.model.system.SysMenu;
import com.xie.model.system.SysUser;
import com.xie.vo.system.LoginVo;
import com.xie.vo.system.RouterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "后台管理系统")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        //code:200 data:{token: "admin-token"}
//        Map<String, Object> map = new HashMap<>();
//        map.put("token","admin-token");

        //1.获取登录的用户名
        String username = loginVo.getUsername();
        //2.根据用户名查询数据库，判断是否存在
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        SysUser sysUser = sysUserService.getOne(wrapper);

        if(sysUser == null){
            throw new XieException(201,"用户不存在");
        }
        //3.判断密码是否正确
        String password_db = sysUser.getPassword();
        String password_vo = MD5.encrypt(loginVo.getPassword());
        if (!password_db.equals(password_vo)){
            throw new XieException(201,"密码不正确");
        }
        //4.判断是否被禁用
        if(sysUser.getStatus().intValue() == 0){
            throw new XieException(201,"用户被禁用");
        }
        //5.生成token
        String token = JwtHelper.createToken(sysUser.getId(), username);
        //6.返回token
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }

    @ApiOperation("信息")
    @GetMapping("/info")
    public Result info(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        //1.获取请求头中的token
        String token = request.getHeader("token");
        //2.根据token取出用户id
        Long userId = JwtHelper.getUserId(token);
        //3.根据id查询用户的信息
        SysUser sysUser = sysUserService.getById(userId);
        //4.根据id查询可以操作的用户列表，查询数据库动态构建路由结构
        List<RouterVo> sysMenuList = sysMenuService.findMenuListByUserId(userId);
        //5.根据id查询可以操作的按钮
        List<String> buttonList = sysMenuService.findBtnListById(userId);

        map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");

        //还需要返回用户列表和操作权限
        map.put("routers",sysMenuList);
        map.put("buttons",buttonList);
        return Result.ok(map);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }


}
