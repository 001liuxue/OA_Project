package com.xie.process.service.impl;

import com.xie.auth.service.SysUserService;
import com.xie.model.process.ProcessRecord;
import com.xie.model.system.SysUser;
import com.xie.process.mapper.OaProcessRecordMapper;
import com.xie.process.service.OaProcessRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批记录 服务实现类
 * </p>
 *
 * @author xie
 * @since 2023-04-03
 */
@Service
public class OaProcessRecordServiceImpl extends ServiceImpl<OaProcessRecordMapper, ProcessRecord> implements OaProcessRecordService {

    @Autowired
    private SysUserService sysUserService;
    @Override
    public void record(Long processId, Integer status, String description) {
        //取出用户id
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser user = sysUserService.getById(userId);

        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUserId(userId);
        processRecord.setOperateUser(user.getName());
        baseMapper.insert(processRecord);


    }
}
