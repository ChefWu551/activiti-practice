package com.yuefeng;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * @Description:
 * @Author: Wu Yuefeng
 * @Date: Created on 2022/2/21
 */
public class ActivitiSuspendedTest {

    /**
     * acitviti Repository Service 挂起测试
     *  repository 部署后的流程被挂起后，此时当前相关的工作流不能再被使用：
     *  并提示： Process definition 我的请假审批 (id = myLeave:3:5004) is suspended
     *  并且不会影响已经提交过的审批实例
     */
    @Test
    public void suspendedRepositoryTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition("myLeave:3:5004");
        boolean isSuspended = processDefinition.isSuspended();
        System.out.println("流程5001当前状态： " + isSuspended);
        if (!isSuspended) {
            System.out.println("挂起流程");
            repositoryService.suspendProcessDefinitionById("myLeave:3:5004");
            System.out.println("流程已挂起");
        }

        if (isSuspended) {
            System.out.println("激活流程");
            repositoryService.activateProcessDefinitionById("myLeave:3:5004");
            System.out.println("激活流程： ");
        }
    }

    /**
     * acitviti Runtime Service 挂起测试
     *
     */
    @Test
    public void suspendedSingleInstanceTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
//                .processInstanceId("17501")
//                .singleResult();

        runtimeService.suspendProcessInstanceById("17501");
        System.out.println("实例流程id: 17501 已经被挂起");

    }
}
