package com.xie.process.service;

import com.xie.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author xie
 * @since 2023-04-03
 */
public interface OaProcessRecordService extends IService<ProcessRecord> {

    void record(Long processId, Integer status, String description);
}
