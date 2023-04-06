package com.xie.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTest01 {

    //注入RepositoryService
    @Autowired
    private RepositoryService repositoryService;

    //注入RunTimeService
    @Autowired
    private RuntimeService runtimeService;

    //注入TaskService
    @Autowired
    private TaskService taskService;

    //注入HistoryService
    @Autowired
    private HistoryService historyService;

    String assign = "谢俊龙";

    //查询已处理的任务
    @Test
    public void findCompletedTask(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                                                        .taskAssignee(assign)
                                                        .finished()
                                                        .list();

        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("流程实例id：" + historicTaskInstance.getProcessInstanceId());
            System.out.println("任务id：" + historicTaskInstance.getId());
            System.out.println("任务负责人：" + historicTaskInstance.getAssignee());
            System.out.println("任务名称：" + historicTaskInstance.getName());
        }
    }

    //处理任务
    @Test
    public void completeTask(){
        Task task = taskService.createTaskQuery().taskAssignee(assign).singleResult();//返回一条任务
        taskService.complete(task.getId());//处理任务，参数为id
    }

    //查询代办任务
    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void findTaskList(){
        List<Task> taskList = taskService.createTaskQuery()
                                         .taskAssignee(assign)////只查询该任务负责人的任务
                                         .list();
        for(Task task : taskList){
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    //启动流程实例
    @Test
    public void startProcess(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia");

        System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
        System.out.println("流程实例id:"+processInstance.getId());
        System.out.println("流程活动id:"+processInstance.getActivityId());
    }


    //流程定义部署
    @Test
    public void deployProcess(){
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia.bpmn20.xml")
                .addClasspathResource("process/qingjia.png")
                .name("请假审批流程")
                .deploy();

        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
