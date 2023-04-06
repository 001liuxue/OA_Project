package com.xie.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.auth.service.SysUserService;
import com.xie.common.result.Result;
import com.xie.common.utils.MD5;
import com.xie.model.system.SysRole;
import com.xie.model.system.SysUser;
import com.xie.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xie
 * @since 2023-03-29
 */
@Api(tags = "用户管理系统")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("修改状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,@PathVariable Integer status){
        sysUserService.updateStatus(id,status);
        return Result.ok();
    }


    @ApiOperation("分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageQuery(@PathVariable Long page,
                            @PathVariable Long limit,
                            SysUserQueryVo sysUserQueryVo){
        //1.创建page对象
        Page<SysUser> Page = new Page<>(page,limit);
        //2.封装wrapper条件,进行模糊查询
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        //3.判断条件
        String username = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        if(!StringUtils.isEmpty(username)){
            wrapper.like("username",username);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le("create_time",createTimeEnd);
        }

        IPage<SysUser> pageModel = sysUserService.page(Page,wrapper);
        return Result.ok(pageModel);
    }

    /**
     * 获取用户
     * @param id
     * @return
     */
    @ApiOperation("根据id获取用户")
    @GetMapping("/findById/{id}")
    public Result findById(@PathVariable Long id){
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    /**
     * 更新用户
     * @param sysUser
     * @return
     */
    @ApiOperation("更新用户")
    @PutMapping("/update")
    public Result update(@RequestBody SysUser sysUser){
        boolean is_success = sysUserService.updateById(sysUser);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 保存用户
     * @param sysUser
     * @return
     */
    @ApiOperation("添加用户")
    @PostMapping("/insert")
    public Result insert(@RequestBody SysUser sysUser){
        //用户的密码需要进行加密操作
        String password = MD5.encrypt(sysUser.getPassword());
        sysUser.setPassword(password);
        boolean is_success = sysUserService.save(sysUser);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean is_success = sysUserService.removeById(id);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }


}

