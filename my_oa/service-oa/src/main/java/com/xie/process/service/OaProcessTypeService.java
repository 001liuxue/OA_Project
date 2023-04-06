package com.xie.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.model.process.ProcessType;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author xie
 * @since 2023-04-03
 */
public interface OaProcessTypeService extends IService<ProcessType> {

    List<ProcessType> findProcessType();
}
