package com.lzh.dao.system;


import com.lzh.domain.system.Company;

import java.util.List;

/*企业的持久层*/
public interface CompanyDao {

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
}
