package com.lzh.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lzh.domain.system.Company;
import com.lzh.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {

    @Reference
    private CompanyService companyService;

   @RequestMapping("/apply.do")
    public @ResponseBody String apply(Company company){
        companyService.save(company);
        return "1";
    }
}
