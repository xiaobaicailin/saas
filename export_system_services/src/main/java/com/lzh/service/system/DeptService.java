package com.lzh.service.system;

import com.github.pagehelper.PageInfo;
import com.lzh.domain.system.Dept;

import java.util.List;

public interface DeptService {

    /**
     * 查询所有，分页pageHelper
     * @param companyId  根据companyID查询该企业下的部门
     * @param page 当前页
     * @param size 当前显示条数
     * @return
     *
     */
    PageInfo findAll(String companyId,int page,int size);

    //根据id查询
    Dept findById(String deptId);

    //根据id删除
    void delete(String deptId);

    //保存
    void save(Dept dept);

    //更新
    void update(Dept dept);

    /**
     * 根据id查询该企业下所有部门
     * @param companyId
     * @return
     */
    List<Dept> findAll(String companyId);

    /**
     * 查询该id下的子部门
     * @param id
     * @return
     */
    List<String> findByParentId(String id);
}
