package com.lzh.cargo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lzh.cargo.FactoryService;
import com.lzh.dao.cargo.FactoryDao;
import com.lzh.domain.cargo.Factory;
import com.lzh.domain.cargo.FactoryExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryDao factoryDao;

    /**
     * 保存
     *
     * @param factory
     */
    @Override
    public void save(Factory factory) {

    }

    /**
     * 更新
     *
     * @param factory
     */
    @Override
    public void update(Factory factory) {

    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {

    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @Override
    public Factory findById(String id) {

        return null;
    }

    @Override
    public List<Factory> findAll(FactoryExample example) {
        return  factoryDao.selectByExample(example);
    }
}
