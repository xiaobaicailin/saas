package com.lzh.cargo.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.cargo.ContractService;
import com.lzh.common.utils.UtilFuns;
import com.lzh.dao.cargo.ContractDao;
import com.lzh.dao.cargo.ContractProductDao;
import com.lzh.dao.cargo.ExtCproductDao;
import com.lzh.domain.cargo.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo findAll(ContractExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<Contract> contractList = contractDao.selectByExample(example);
        return new PageInfo(contractList);
    }

    @Override
    public Contract findById(String id) {

        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Contract contract) {
        contract.setId(UtilFuns.generateId());
        contract.setState(0);
        contract.setProNum(0);
        contract.setProNum(0);
        contract.setTotalAmount(0d);
        contract.setCreateTime(new Date());
        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //先删除合同下的货物
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdEqualTo(id);
        List<ContractProduct> contractProductList = contractProductDao.selectByExample(contractProductExample);
        for (ContractProduct contractProduct : contractProductList) {
            contractProductDao.deleteByPrimaryKey(contractProduct.getId());
        }
        //删除货物下的附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractIdEqualTo(id);
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);
        for (ExtCproduct extCproduct : extCproductList) {
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }


        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ContractProductVo> print(String shipTime, String companyId) {
        return contractDao.print(shipTime, companyId);
    }
}
