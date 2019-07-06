package com.lzh.web.exceptions;

import com.lzh.web.exceptions.CustomeException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*自定义异常处理器*/
@Component
public class CustomeExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //创建返回值对象
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        //判断异常
        if (ex instanceof CustomeException){
            //业务异常给用户提示
            mv.addObject("errorMsg",ex.getMessage() );
        }else {
            //系统异常，给开发人员看
            mv.addObject("errorMsg","服务器忙" );
        }


        return mv;
    }
}
