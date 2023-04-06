package com.xie.process.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.model.process.ProcessTemplate;
import com.xie.model.process.ProcessType;
import com.xie.process.mapper.OaProcessTemplateMapper;
import com.xie.process.mapper.OaProcessTypeMapper;
import com.xie.process.service.OaProcessService;
import com.xie.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.process.service.OaProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author xie
 * @since 2023-04-03
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {


    @Resource
    private OaProcessTypeService oaProcessTypeService;

    @Autowired
    private OaProcessService oaProcessService;

    @Override
    public IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> processTemplatePage) {
        //1.查询所有的审批模板
        IPage<ProcessTemplate> page = baseMapper.selectPage(processTemplatePage,null);
        List<ProcessTemplate> records = page.getRecords();

        //2.遍历其中的审批类型id
        for (ProcessTemplate processTemplate : records){

            //3.根据id在审批类型中查询审批类型名称
            QueryWrapper<ProcessType> typeQueryWrapper = new QueryWrapper<>();
            typeQueryWrapper.eq("id",processTemplate.getProcessTypeId());
            ProcessType processType = oaProcessTypeService.getOne(typeQueryWrapper);

            if(processType == null){
                continue;
            }

            //4.赋值给审批模板
            processTemplate.setProcessTypeName(processType.getName());
        }
        return page;
    }

    @Transactional
    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = this.getById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);

        //部署流程定义
        if(!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
            oaProcessService.deployByZip(processTemplate.getProcessDefinitionPath());
        }
    }
}
