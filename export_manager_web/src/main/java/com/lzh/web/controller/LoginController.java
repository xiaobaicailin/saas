package com.lzh.web.controller;


import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.system.Module;
import com.lzh.domain.system.User;
import com.lzh.service.system.ModuleService;
import com.lzh.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class LoginController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;


//shiro方式登录
    @RequestMapping("/login")
    @ResponseBody
    public String login(String email,String password) {
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            return "1";
        }
        if (UtilFuns.isEmpty(email)){
            return "redirect:/login.jsp";
        }
        try {
            //1.首先使用SecurityUtils得到subject代表当前用户
            Subject subject = SecurityUtils.getSubject();
            //2.创建带有用户账号和密码的令牌
            UsernamePasswordToken token = new UsernamePasswordToken(email,password);
            //3.使用subject的login方法登录，验证是否正确  ,调用realm  ，自己写的那个认证
            subject.login(token);
            //4.假如正确，用subject得到主体，并将其强转为user类型
             user = (User) subject.getPrincipal();

            //登录成功
            session.setAttribute("user",user );
            //按照登录用户的权限显示模块
            List<Module> modulesList = moduleService.showModulesByUser(user);
            session.setAttribute("modules", modulesList);

            return "1";
        } catch (Exception e) {
            //5.假如错误，抛出异常
            /*request.setAttribute("error","用户名或密码错误" );
            return "forward:/login.jsp";*/
            return "0";
        }
    }


	/*传统登录方式
	@RequestMapping("/login")
	public String login(String email,String password) {
	    if (UtilFuns.isEmpty(email)){
	        return "redirect:/login.jsp";
        }
        User user = userService.findUserByemail(email);
	    //如果user为空，email账号不相符
        if (user==null){
            request.setAttribute("error", "密码或账号错误");
            return "forward:/login.jsp";
        }
        //如果user为空，email账号不相符
        if (!user.getPassword().equals(password)){
            request.setAttribute("error", "密码或账号错误");
            return "forward:/login.jsp";
        }
        //登录成功
        session.setAttribute("user",user );
        //按照登录用户的权限显示模块
        List<Module> modulesList = moduleService.showModulesByUser(user);
        session.setAttribute("modules", modulesList);

        return "home/main";
	}
*/
    //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }


    @RequestMapping("/home")
    public String home(){
	    return "home/home";
    }


    @RequestMapping("/hmain")
    public String mHome(){
        return "home/main";
    }
}
