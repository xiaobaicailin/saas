package com.lzh.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.system.Dept;
import com.lzh.domain.system.Role;
import com.lzh.service.system.DeptService;
import com.lzh.service.system.RoleService;
import com.lzh.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;


    /**
     * 根据公司id查询全部用户
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        PageInfo pageInfo = roleService.findAll(companyId,page,size);
        request.setAttribute("page",pageInfo );
        return "system/role/role-list";
    }


    /**
     * 新建
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "system/role/role-add";
    }


    /**
     * 保存或更新
     * @param role
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Role role){
        //判断用户id是否为空
        if (UtilFuns.isEmpty(role.getId())){
            //id为空，保存
            role.setCompanyId(companyId);
            role.setCompanyName(companyName);
            roleService.save(role);
        }else{
            //id不为空，更新
            //根据id从数据库得到该role对象
            Role dbrole = roleService.findById(role.getId());
            //替换，保证数据不丢失
            BeanUtils.copyProperties(role, dbrole,new String[]{"companyId","companyName",});
            roleService.update(dbrole);
        }
        return "redirect:/system/role/list";
    }


    /**
     * 更新
     * @param
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Role upRole = roleService.findById(id);
        //先把role保存到request域中
        request.setAttribute("role", upRole);
        return "system/role/role-update";
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id){
        roleService.delete(id);
        return "redirect:/system/role/list";
    }

    /**
     * 前往角色权限
     * @param roleid
     * @return
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleid){
        Role role = roleService.findById(roleid);
        request.setAttribute("role", role);
        return "system/role/role-module";
    }

    /**
     * 模块树形图
     * @param id
     * @return
     */
    @RequestMapping("/initModuleData")
    public  @ResponseBody List<Map> initModuleData(String id){

       return  roleService.findRoleModuleById(id);
    }

    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleid,String moduleIds){
        roleService.saveRoleModule(roleid,moduleIds);
        return "redirect:/system/role/list";
    }

}
