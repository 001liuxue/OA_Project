package com.xie.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xie.model.process.ProcessTemplate;
import com.xie.model.process.ProcessType;
import com.xie.process.mapper.OaProcessTypeMapper;
import com.xie.process.service.OaProcessTemplateService;
import com.xie.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author xie
 * @since 2023-04-03
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;
    @Override
    public List<ProcessType> findProcessType() {
        //1.查询所有的审批类型
        List<ProcessType> processTypes = baseMapper.selectList(null);

        //2.遍历所有审批类型取出id
        for(ProcessType processType : processTypes){
            Long id = processType.getId();
            QueryWrapper<ProcessTemplate> wrapper = new QueryWrapper<>();
            wrapper.eq("process_type_id",id);
            List<ProcessTemplate> list = oaProcessTemplateService.list(wrapper);

            processType.setProcessTemplateList(list);
        }

        return processTypes;
    }
}
