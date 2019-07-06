package com.lzh.dao.system;


import com.lzh.domain.system.Module;
import com.lzh.domain.system.User;

import java.util.List;
import java.util.Map;

/**
 */
public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    int delete(String moduleId);

    //添加用户
    int save(Module module);

    //更新用户
    int update(Module module);

    //查询全部
    List<Module> findAll();

    //根据id查找该角色的module，树形图
    List<Map> findRoleModuleById(String id);

    //根据userId查询所有模块
    List<Module> findModulesByUser(String userId);

    //根据degree查询模块
    List<Module> findMoudulesByBelong(Integer belong);
}