package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.model.TestProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author: gaofeng
 * @Date: 2019-09-11
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProjectServiceTest {

    @Autowired
    private TestProjectService testProjectService;

    @Test
    public void getTemplate() {
        TestProject testProject = new TestProject();
        testProject.setProject_rank("S1");
        String str = testProjectService.getTemplate(testProject);
    }
}