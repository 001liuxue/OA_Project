package com.xie.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.auth.mapper.SysRoleMapper;
import com.xie.auth.service.SysRoleService;
import com.xie.common.config.exception.XieException;
import com.xie.common.result.Result;
import com.xie.model.system.SysRole;
import com.xie.vo.system.AssginRoleVo;
import com.xie.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    //注入service
    @Autowired
    private SysRoleService sysRoleService;

    //获取所有角色和当前用户的角色
    @ApiOperation("查询所有角色和当前用户的角色")
    @GetMapping("/toAssign/{id}")
    public Result toAssign(@PathVariable Long id){
        Map<String,Object> map = sysRoleService.findRoleById(id);
        return Result.ok(map);
    }

    //更改用户角色
    @ApiOperation("更改当前用户角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }

    //查询所有角色
    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result findAll(){
        List<SysRole> list = sysRoleService.list();
//        try{
//            int i = 10 / 0;
//        }catch (Exception e){
//            throw new XieException(20001,"执行自定义异常");
//        }

        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据id查询信息")
    @GetMapping("/findById/{id}")
    public Result findById(@PathVariable Long id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    //分页查询角色
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("分页查询信息")
    @GetMapping("{page}/{limit}")
    public Result pageQuery(@PathVariable Long page,
                            @PathVariable Long limit,
                            SysRoleQueryVo sysRoleQueryVo){
        //
        Page<SysRole> pageParam = new Page<>(page, limit);

        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();

        if(!StringUtils.isEmpty(roleName)){
            wrapper.like("role_name",roleName);
        }

        IPage<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);

        return Result.ok(pageModel);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加数据")
    @PostMapping("/insert")
    public Result insert(@RequestBody SysRole sysRole){
        boolean flag = sysRoleService.save(sysRole);
        if (flag) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("修改数据")
    @PutMapping("/update")
    public Result update(@RequestBody SysRole sysRole){
        boolean flag = sysRoleService.updateById(sysRole);
        if (flag) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据id删除数据")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id){
        boolean flag = sysRoleService.removeById(id);
        if (flag) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除数据")
    @DeleteMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Long> list){
        boolean flag = sysRoleService.removeByIds(list);
        if (flag) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}
