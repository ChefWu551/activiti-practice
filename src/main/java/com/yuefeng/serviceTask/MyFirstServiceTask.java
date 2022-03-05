package com.yuefeng.serviceTask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @Description:
 * @Author: Wu Yuefeng
 * @Date: Created on 2022/2/23
 */
public class MyFirstServiceTask implements JavaDelegate {

    private Expression text1;

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("serviceTask执行");
        String value = (String) text1.getValue(execution);
        System.out.println("执行器输入的值是： " + value);

        execution.setVariable("var1", new StringBuffer(value).reverse().toString());

    }
}
