package com.lzh.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.system.Dept;
import com.lzh.domain.system.Module;
import com.lzh.service.system.DeptService;
import com.lzh.service.system.ModuleService;
import com.lzh.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;


    /**
     * 根据公司id查询全部用户
     * @param page
     * @param size
     * @return
     */
    @RequiresPermissions("企业管理")
    @RequestMapping(value = "/list",name = "企业管理")
    public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        PageInfo pageInfo = moduleService.findAll(companyId,page,size);
        request.setAttribute("page",pageInfo );
        return "system/module/module-list";
    }


    /**
     * 新建   提供模块列表
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        List<Module> moduleList = moduleService.findAll();
        request.setAttribute("menus", moduleList);
        return "system/module/module-add";
    }


    /**
     * 保存或更新
     * @param module
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Module module){
        //判断用户id是否为空
        if (UtilFuns.isEmpty(module.getId())){
            //id为空，保存
            moduleService.save(module);
        }else{
            //id不为空，更新
            //根据id从数据库得到该module对象
            Module dbmodule = moduleService.findById(module.getId());
            //替换，保证数据不丢失
            BeanUtils.copyProperties(module, dbmodule,new String[]{"companyId","companyName",});
            moduleService.update(dbmodule);
        }
        return "redirect:/system/module/list";
    }


    /**
     * 更新
     * @param
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Module upModule = moduleService.findById(id);
        //先把module保存到request域中
        request.setAttribute("module", upModule);
        //把部门列表存进去
        //先从数据库得到列表
        List<Module> all = moduleService.findAll();
        request.setAttribute("menus",all );
        return "system/module/module-update";
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id){
        moduleService.delete(id);
        return "redirect:/system/module/list";
    }



}
