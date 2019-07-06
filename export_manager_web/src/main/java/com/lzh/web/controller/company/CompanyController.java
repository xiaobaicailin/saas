package com.lzh.web.controller.company;


import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.system.Company;
import com.lzh.service.company.CompanyService;
import com.lzh.web.controller.BaseController;
import com.lzh.web.exceptions.CustomeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/company")
@Controller
public class CompanyController extends BaseController {

    @Reference
    private CompanyService companyService;

//企业查询分页
    @RequestMapping("/list")
    public String list( @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "2") int size){
        PageInfo pageInfo = companyService.findByPageHelper(page, size);
        //List<Company> all = companyService.findAll();
        request.setAttribute("page", pageInfo);
        return "company/company-list";
    }

    /**
     * 新建
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "company/company-add";
    }

    /**
     * 保存或更新
     * @param company
     * @return
     */
    @RequestMapping("/edit")
    public String save(Company company) throws Exception {

        if (UtilFuns.isEmpty(company.getName())){
            throw new CustomeException("公司名字必须存在");
        }

        if (UtilFuns.isEmpty(company.getId())){
            companyService.save(company);
        }else {
            companyService.update(company);
        }
        return "redirect:/company/list";
    }

    /**
     * 前往更新页面编辑
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){

        Company byId = companyService.findById(id);
        request.setAttribute("company",byId );
        return "company/company-update";
    }

    @RequestMapping("/delete")
    public String delete(String id){
            companyService.delete(id);
            return "redirect:/company/list";
    }
}
