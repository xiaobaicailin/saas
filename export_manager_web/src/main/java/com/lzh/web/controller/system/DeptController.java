package com.lzh.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.system.Dept;
import com.lzh.service.system.DeptService;
import com.lzh.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    //查看该企业id下所有部门列表
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size){
        //String companyId = "1";
        PageInfo all = deptService.findAll(companyId, page, size);
        request.setAttribute("page",all );
        return "system/dept/dept-list";
    }

    //保存页面下的上级部门list
    @RequestMapping("/toAdd")
    public String toAdd(){
        List<Dept> all = deptService.findAll(companyId);
        request.setAttribute("deptList",all );
        return "system/dept/dept-add";
    }

    //保存或更新
    @RequestMapping("/edit")
    public String edit(Dept dept){
        if (UtilFuns.isEmpty(dept.getId())){
            //添加
            dept.setCompanyName(companyName);
            dept.setCompanyId(companyId);
            deptService.save(dept);
        }else {
            //更新
            Dept dbDept = deptService.findById(dept.getId());
            BeanUtils.copyProperties(dept, dbDept,new String[]{"companyId","companyName"});
            deptService.update(dbDept);
        }
        return "redirect:/system/dept/list";
    }

    //编辑
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Dept dept = deptService.findById(id);
        request.setAttribute("dept",dept);
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList", deptList);
        return "system/dept/dept-update";
    }

    //删除
    @RequestMapping("/delete")
    public String delete(String id){
        deptService.delete(id);
        return "redirect:/system/dept/list";
    }
}
