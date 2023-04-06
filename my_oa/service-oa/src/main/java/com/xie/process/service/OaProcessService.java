package com.xie.process.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.model.process.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.vo.process.ApprovalVo;
import com.xie.vo.process.ProcessFormVo;
import com.xie.vo.process.ProcessQueryVo;
import com.xie.vo.process.ProcessVo;

import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author xie
 * @since 2023-04-03
 */
public interface OaProcessService extends IService<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    void deployByZip(String deployPath);

    void startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Page<Process> pageParam);

    void approve(ApprovalVo approvalVo);

    Map<String, Object> show(Long id);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
