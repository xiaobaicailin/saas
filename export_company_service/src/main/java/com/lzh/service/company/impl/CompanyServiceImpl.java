package com.lzh.service.company.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.dao.system.CompanyDao;
import com.lzh.domain.system.Company;
import com.lzh.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.UUID;

@Service  //换成duboo
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;


    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    /**
     * 存储
     *
     * @param company
     */
    @Override
    public void save(Company company) {

        //1.生成id
        String id = UUID.randomUUID().toString().replace("_","" );
        //2.给company赋值
        company.setId(id);
        //3.调用dao保存
        companyDao.save(company);

    }

    /**
     * 更新
     *
     * @param company
     */
    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }


    /**
     * 查询带有分页的结果集，使用mybatis的pageHelper插件
     *
     * @param page 当前页
     * @param size 每页显示条数
     */
    @Override
    public PageInfo findByPageHelper(int page, int size) {
        //使用pagehelper中提供的静态方法startPage
        PageHelper.startPage(page,size);
        //这个方法会被分页  pageHelper作用解耦。  pageHelper再次增强dao的动态代理类
        List<Company> list = companyDao.findAll();
        //3创建返回值对象
        PageInfo pageInfo = new PageInfo(list);
        return  pageInfo;

    }
}
