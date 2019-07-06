package com.lzh.service.system;

import com.github.pagehelper.PageInfo;
import com.lzh.domain.system.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    //根据企业id查询全部,分页
    PageInfo findAll(String companyId, int page, int size);

    //根据id查询
    Role findById(String roleId);

    //根据id删除
    void delete(String roleId);

    //保存
    void save(Role role);

    //更新
    void update(Role role);

    //根据id查找该角色的module，树形图
    List<Map> findRoleModuleById(String id);

    //更新保存树形图
    void saveRoleModule (String roleid,String moduleIds);

    //根据企业id查询全部
    List<Role> findAll(String companyId);
}
