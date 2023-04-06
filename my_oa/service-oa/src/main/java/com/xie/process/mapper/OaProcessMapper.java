package com.xie.process.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.model.process.Process;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.vo.process.ProcessQueryVo;
import com.xie.vo.process.ProcessVo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author xie
 * @since 2023-04-03
 */
@Mapper
public interface OaProcessMapper extends BaseMapper<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> page, @Param("vo") ProcessQueryVo processQueryVo);

}
