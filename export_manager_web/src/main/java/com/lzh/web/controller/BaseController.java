package com.lzh.web.controller;



import com.lzh.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;
    protected String companyId="1";
    protected String companyName="传智001";
    //protected User user;

    public User getCurrentUser(){
       return (User) session.getAttribute("user");
    }

   /* @ModelAttribute
    public void setReqAndResp(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        this.request = request;
        this.response = response;
        this.session = session;
        //模拟数据
	    //user = (User)session.getAttribute("loginUser");
	    //if(user != null) {
		//    this.companyId = user.getCompanyId();
		//    this.companyName=user.getCompanyName();
	    //}
    }*/
}
