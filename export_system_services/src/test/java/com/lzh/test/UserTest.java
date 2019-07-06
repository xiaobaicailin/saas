package com.lzh.test;

import com.github.pagehelper.PageInfo;
import com.lzh.service.system.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*")
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void findAll(){
        PageInfo all = userService.findAll("1", 1, 10);
        List list = all.getList();
        for (Object o : list) {
            System.out.println(o);
        }

    }
}
