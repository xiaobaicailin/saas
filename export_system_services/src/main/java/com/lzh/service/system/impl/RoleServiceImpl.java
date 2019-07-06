package com.lzh.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.dao.system.ModuleDao;
import com.lzh.dao.system.RoleDao;
import com.lzh.domain.system.Role;
import com.lzh.service.system.ModuleService;
import com.lzh.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        PageHelper.startPage(page,size);
        List<Role> roleList = roleDao.findAll(companyId);
        return new PageInfo(roleList);
    }

    @Override
    public Role findById(String roleId) {
        Role roleByid = roleDao.findById(roleId);
        return roleByid;
    }

    @Override
    public void delete(String roleId) {
        roleDao.delete(roleId);

    }

    @Override
    public void save(Role role) {
        role.setId(UtilFuns.generateId());
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }


    @Override
    public List<Map> findRoleModuleById(String id) {
        return moduleDao.findRoleModuleById(id);
    }

    @Override
    public void saveRoleModule(String roleid, String moduleIds) {
        //先根据roleid删除原来的
        roleDao.deleteRoleModule(roleid);
        //添加保存
        String[] models = moduleIds.split(",");
        for (String model : models) {
            roleDao.saveRoleModules(roleid,model);
        }

    }

    @Override
    public List<Role> findAll(String companyId) {

        return roleDao.findAll(companyId);
    }

}
