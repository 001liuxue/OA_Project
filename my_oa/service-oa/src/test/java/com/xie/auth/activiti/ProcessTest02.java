package com.xie.auth.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTest02 {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    //部署流程定义
    @Test
    public void deployProcess(){
        Deployment jiaban = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban.bpmn20.xml")
                .name("jiaban")
                .deploy();

        System.out.println(jiaban.getName());
    }

    //启动流程实例
    @Test
    public void startProcess(){
        ProcessInstance jiaban = runtimeService.startProcessInstanceByKey("process");
        System.out.println(jiaban.getProcessInstanceId());
        System.out.println(jiaban.getId());
    }
}
