package com.lzh.test;

import com.lzh.service.system.UserService;
import com.lzh.service.system.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class AppTest {
    @Test
    public void apTest(){
        UserServiceImpl userService = new UserServiceImpl();

     UserService userService1 = (UserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(), userService.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("save")){
                            System.out.println("baocun chengg ");
                        }
                        return null;
                    }
                });
     userService1.save(null);
    }
}
