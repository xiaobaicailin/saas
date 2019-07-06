package com.lzh.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.system.Dept;
import com.lzh.domain.system.Role;
import com.lzh.domain.system.User;
import com.lzh.service.system.DeptService;
import com.lzh.service.system.RoleService;
import com.lzh.service.system.UserService;
import com.lzh.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

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
        PageInfo pageInfo = userService.findAll(companyId,page,size);
        request.setAttribute("page",pageInfo );
        return "system/user/user-list";
    }


    /**
     * 新建   提供部门列表
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList", deptList);
        return "system/user/user-add";
    }


    /**
     * 保存或更新
     * @param user
     * @return
     */
    @RequestMapping("/edit")
    public String edit(User user){
        //判断用户id是否为空
        if (UtilFuns.isEmpty(user.getId())){
            //id为空，保存
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            userService.save(user);
        }else{
            //id不为空，更新
            //根据id从数据库得到该user对象
            User dbuser = userService.findById(user.getId());
            //替换，保证数据不丢失
            BeanUtils.copyProperties(user, dbuser,new String[]{"deptId","companyId","companyName",});
            userService.update(dbuser);
        }
        return "redirect:/system/user/list";
    }


    /**
     * 更新
     * @param
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        User upUser = userService.findById(id);
        //先把user保存到request域中
        request.setAttribute("user", upUser);
        //把部门列表存进去
        //先从数据库得到列表
        List<Dept> all = deptService.findAll(companyId);
        request.setAttribute("deptList",all );
        return "system/user/user-update";
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id){
        userService.delete(id);
        return "redirect:/system/user/list";
    }

    /**
     * 去往分配角色页面
     * @param id
     * @return
     */
    @RequestMapping("/roleList")
    public String roleList(String id){
        User user = userService.findById(id);
        List<Role> RoleList = roleService.findAll(companyId);
        //属于该用户的角色集合
        List<String> roleIdList = userService.findRoleById(id);
        request.setAttribute("user", user);
        request.setAttribute("roleList", RoleList);
        request.setAttribute("userRoleStr",roleIdList.toString() );
        System.out.println(roleIdList.toString());
        return "system/user/user-role";
    }

    /**
     * 保存该用户下的角色
     * @param userid
     * @param roleIds
     * @return
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userid,String roleIds){
        userService.saveChange(userid,roleIds);
        return "redirect:/system/user/list";
    }

}
