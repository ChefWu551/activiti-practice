package com.yuefeng;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @Description: activiti 常用service测试
 * @Author: Wu Yuefeng
 * @Date: Created on 2022/2/20
 */
public class ActivitiServiceTest {

    /**
     * 创建、更新数据库表结构
     *
     *  act_ge_xx 表示（generation）通用的属性值
     *  act_hi_xx 表示 history 历史操作相关
     *  act_ru_xx 表示runtime 运行时候相关的记录
     *  act_re_xx 表示repository 流程模型存储的记录
     */
    @Test
    public void createTableTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    }

    /**
     * 创建部署
     *
     *  部署相关的信息会写入到act_re_xx相关表里面，
     *  同时也会写入到act_ge_xx记录表相关的附属文件二进制信息
     *
     */
    @Test
    public void deploymentTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/MyLeave.bpmn")
                .addClasspathResource("processes/MyLeave.png")
                .name("请假申请流程")
                .key("myLeave")  // 一个key对应的是一个部署流程，是唯一的
                .deploy();
        System.out.println("流程部署id： " + deployment.getId());
        System.out.println("流程部署name： " + deployment.getName());
        System.out.println("流程部署key： " + deployment.getKey());
    }

    /**
     * 启动部署的实例
     *
     *
     */
    @Test
    public void startInstanceTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 根据流程定义id启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myLeave");
        System.out.println("流程定义的id: " + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id: " + processInstance.getId());
        System.out.println("当前活动id: " + processInstance.getActivityId());
    }

    /**
     * 查找当前个人待执行的任务
     *
     */
    @Test
    public void findPersonalTaskListTest() {
        String assignee = "manager";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key和任务负责人查询任务
        List<Task> list = taskService.createTaskQuery().processDefinitionKey("myLeave").taskAssignee(assignee).list();

        for (Task task : list) {
            System.out.println("流程实例id： " + task.getProcessInstanceId());
            System.out.println("任务id： " + task.getId());
            System.out.println("任务负责人id： " + task.getAssignee());
            System.out.println("任务名称： " + task.getName());
        }
    }

    /**
     * worker提交审批流程
     *  complete当前节点后，流程会往下一个人走，也就是 woker -> manager
     *  此时执行 findPersonalTaskListTest.assignee = worker 是没有返回值的
     *  此时执行 findPersonalTaskListTest.assignee = manager 返回经理节点相关的内容
     *  参照数据库表： act_hi_actinst 里面会多增加一个manager字段
     */
    @Test
    public void completeTaskTest() {
        String assignee = "woker";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key和任务的负责人查询任务
        // 返回一个任务对象
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myLeave") // 流程key
                .taskAssignee(assignee) // 要查询的负责人
                .singleResult();

        // 完成任务，参数：任务id
        taskService.complete(task.getId());
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteDeploymentTest() {
        String deploymentId = "5001";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 删除路程定义，如果流程定义已有启动则删除出错
        repositoryService.deleteDeployment(deploymentId);
        // 设置true 级联删除流程定义，及时该流程实例启动也可以删除，设置为false非级别删除方式
//        repositoryService.deleteDeployment(deploymentId, true);
    }


    /**
     * 下载定义的资源
     */
}
