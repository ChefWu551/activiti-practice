package com.yuefeng.serviceTask;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Wu Yuefeng
 * @Date: Created on 2022/2/23
 */
public class ServiceTaskTest {

    @Test
    public void deployTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/ServiceTaskTest.bpmn")
                .addClasspathResource("processes/ServiceTaskTest.png")
                .name("JavaServiceTaskTest")
                .key("ServiceTask001")
                .deploy();
    }

    @Test
    public void startInstanceTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 根据流程定义id启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");
        System.out.println("流程定义的id: " + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id: " + processInstance.getId());
        System.out.println("当前活动id: " + processInstance.getActivityId());
    }

}
