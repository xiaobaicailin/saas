package com.lzh.cargo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageInfo;
import com.lzh.cargo.ExportProductService;
import com.lzh.dao.cargo.ExportProductDao;
import com.lzh.domain.cargo.ExportProduct;
import com.lzh.domain.cargo.ExportProductExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExportProductServiceImpl implements ExportProductService {

    @Autowired
    private ExportProductDao exportProductDao;

    @Override
    public ExportProduct findById(String id) {
        return null;
    }

    @Override
    public void save(ExportProduct exportProduct) {

    }

    @Override
    public void update(ExportProduct exportProduct) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        return null;
    }

    @Override
    public List<ExportProduct> findAll(ExportProductExample example) {

        return exportProductDao.selectByExample(example);
    }
}
