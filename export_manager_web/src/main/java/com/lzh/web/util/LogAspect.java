package com.lzh.web.util;

import com.lzh.domain.system.SysLog;
import com.lzh.domain.system.User;
import com.lzh.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysLogService sysLogService;

    //ProceedingJoinPoint相当于proxy类的方法里的method
    @Around("execution(* com.lzh.web.controller.*.*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint pjp){
        //判断user是否登录
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            //1.获取方法签名
            Signature signature = pjp.getSignature();
            //2.判断是不是方法签名
            if (signature instanceof MethodSignature){
                //强转
                MethodSignature methodSignature = (MethodSignature)signature;
                //3.获取当前执行方法
                Method method = methodSignature.getMethod();
                //4.判断当前方法上是否有RequestMapping注解
                boolean hasAnnotated = method.isAnnotationPresent(RequestMapping.class);
                if (hasAnnotated){
                    //有注解，取出注解
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
/*                //6.得到注解属性，用于填充syslog对象
                String[] value = annotation.value();
                StringBuilder ss = new StringBuilder();
                if (value.length>1){
                    for (String s : value) {
                        ss.append(s).append(",");
                    }
                }else {
                    ss.append(value);
                }*/
                    String name = annotation.name();
                    //7.创建日志对象
                    SysLog sysLog = new SysLog();
                    sysLog.setIp(request.getRemoteAddr());
                    sysLog.setTime(new Date());
                    sysLog.setMethod(method.getName());
                    sysLog.setAction(name);
                    sysLog.setUserName(user.getUserName());
                    sysLog.setCompanyId(user.getCompanyId());
                    sysLog.setCompanyName(user.getCompanyName());
                    sysLogService.save(sysLog);
                }

            }
        }


        Object[] args = pjp.getArgs(); //得到方法参数
        Object proceed = null;
        try {
             proceed = pjp.proceed(args);//执行方法
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }
}
