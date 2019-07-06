package com.lzh.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.dao.system.ModuleDao;
import com.lzh.domain.system.Module;
import com.lzh.domain.system.User;
import com.lzh.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        PageHelper.startPage(page,size);
        List<Module> moduleList = moduleDao.findAll();
        return new PageInfo(moduleList);
    }

    @Override
    public Module findById(String moduleId) {
        return moduleDao.findById(moduleId);
    }

    @Override
    public void delete(String moduleId) {
        moduleDao.delete(moduleId);

    }

    @Override
    public void save(Module module) {
        module.setId(UtilFuns.generateId());
        moduleDao.save(module);
    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public List<Module> showModulesByUser(User user) {
        //saas管理员
        if (user.getDegree()==0){
            return  moduleDao.findMoudulesByBelong(0);
        }else if (user.getDegree()==1){
            //企业管理员
            return  moduleDao.findMoudulesByBelong(1);
        }else {
            //普通员工
            return moduleDao.findModulesByUser(user.getId());
        }
    }
}
