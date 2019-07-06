package com.lzh.test;

import com.github.pagehelper.PageInfo;
import com.lzh.domain.system.Dept;
import com.lzh.service.system.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*")
public class DeptTest {

    @Autowired
    private DeptService deptService;

    @Test
    public void testFindAll(){
        PageInfo all = deptService.findAll("1", 1, 10);
        for (Object o : all.getList()) {
            System.out.println(o);
        }

    }

    @Test
    public void testSave(){
        Dept dept = new Dept();
        dept.setId("1");
        dept.setCompanyName("林子");
        dept.setCompanyId("1001");
        dept.setDeptName("hao");
        dept.setState(1);
        deptService.save(dept);
    }
}
