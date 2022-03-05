package com.yuefeng.serviceTask;

import com.yuefeng.serviceTask.model.Evection;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Wu Yuefeng
 * @Date: Created on 2022/3/5
 */
public class MyLeaveWithParamMapTest {

    @Test
    public void deployTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/MyLeaveWithParamMap.bpmn")
                .addClasspathResource("processes/MyLeaveWithParamMap.png")
                .name("LeaveWithParam")
                .key("leaveWithParam")
                .deploy();
    }


    @Test
    public void startInstanceWithParam() {
        String keyName = "leaveWithParam";
        Evection evection = new Evection();
        evection.setNum(50);

        Map<String, Object> map = new HashMap<>();
        map.put("evection", evection);

        map.put("assignee0", "张三");
        map.put("assignee1", "王经理");
        map.put("assignee2", "李财务");
        map.put("assignee3", "林总监");

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 根据流程定义id启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(keyName, map);

        System.out.println("流程定义的id: " + processInstance.getProcessDefinitionId());
        System.out.println("实例的id: " + processInstance.getProcessInstanceId());
    }

    /**
     * 打印审批实例进行的过程
     *
     */
    @Test
    public void findHistoryInfo() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        HistoricActivityInstanceQuery haiq = historyService.createHistoricActivityInstanceQuery();

        // 总经理审批流程实例id 117501
        haiq.processInstanceId("117501");
        haiq.orderByHistoricActivityInstanceStartTime().asc();
        List<HistoricActivityInstance> activityInstances = haiq.list();

        for (HistoricActivityInstance instance : activityInstances) {
            System.out.println(instance.getActivityId());
            System.out.println(instance.getActivityName());
            System.out.println(instance.getStartTime());
            System.out.println("操作人" + instance.getAssignee());
            System.out.println(" =================== ");
        }
    }

    @Test
    public void completeTaskTest() {
//        String assignee = "张三";
        String assignee = "王经理";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key和任务的负责人查询任务
        // 返回一个任务对象
        Task task = taskService.createTaskQuery().processInstanceId("117501")
//                .processDefinitionKey("myLeave") // 流程key
                .taskAssignee(assignee) // 要查询的负责人
                .singleResult();

        // 完成任务，参数：任务id
        if (task == null) return;
        taskService.complete(task.getId());
    }
}
