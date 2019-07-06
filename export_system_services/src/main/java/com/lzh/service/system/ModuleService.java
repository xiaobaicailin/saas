package com.lzh.service.system;

import com.github.pagehelper.PageInfo;
import com.lzh.domain.system.Module;
import com.lzh.domain.system.User;

import java.util.List;

public interface ModuleService {
    //根据企业id查询全部
    PageInfo findAll(String companyId, int page, int size);

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    void delete(String moduleId);

    //保存
    void save(Module module);

    //更新
    void update(Module module);

    //查询所有模块
    List<Module> findAll();

    //根据userId查询所有模块
    List<Module> showModulesByUser(User user);
}
