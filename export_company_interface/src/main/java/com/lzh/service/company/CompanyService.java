package com.lzh.service.company;

import com.github.pagehelper.PageInfo;
import com.lzh.domain.system.Company;

import java.util.List;

public interface CompanyService {
    /**
     * 查询所有
     * @return
     */
    List<Company> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Company findById(String id);

    /**
     * 存储
     * @param company
     */
    void save(Company company);

    /**
     * 更新
     * @param company
     */
    void update(Company company);

    /**
     * 根据id删除
     * @param id
     */
    void delete(String id);

    /**
     * 查询带有分页的结果集，使用mybatis的pageHelper插件
     * @param page 当前页
     * @param size 每页显示条数
     */
    PageInfo findByPageHelper(int page, int size);
}
