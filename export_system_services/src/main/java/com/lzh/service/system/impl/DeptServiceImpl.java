package com.lzh.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.dao.system.DeptDao;
import com.lzh.domain.system.Dept;
import com.lzh.service.system.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
   private DeptDao deptDao;

    /**
     * 查询所有，分页pageHelper
     *
     * @param companyId 根据companyID查询该企业下的部门
     * @param page      当前页
     * @param size      当前显示条数
     * @return
     */
    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        PageHelper.startPage(page,size);
        List<Dept> deptList = deptDao.findAll(companyId);
        return new PageInfo(deptList);
    }

    @Override
    public Dept findById(String deptId) {
        return deptDao.findById(deptId);
    }

    @Override
    public void delete(String deptId) {
        deptDao.delete(deptId);
    }

    @Override
    public void save(Dept dept) {
        dept.setId(UtilFuns.generateId());
        deptDao.save(dept);
    }

    @Override
    public void update(Dept dept) {
        deptDao.update(dept);
    }

    /**
     * 根据id查询该企业下所有部门
     *
     * @param companyId
     * @return
     */
    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    /**
     * 查询该id下的子部门
     *
     * @param id
     * @return
     */
    @Override
    public List<String> findByParentId(String id) {

        return deptDao.findByParentId(id);
    }
}
