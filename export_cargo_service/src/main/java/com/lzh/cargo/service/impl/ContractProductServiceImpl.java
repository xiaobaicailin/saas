package com.lzh.cargo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.cargo.ContractProductService;
import com.lzh.common.utils.UtilFuns;
import com.lzh.dao.cargo.ContractDao;
import com.lzh.dao.cargo.ContractProductDao;
import com.lzh.dao.cargo.ExtCproductDao;
import com.lzh.domain.cargo.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    /**
     * 分页查询
     *
     * @param example
     * @param page
     * @param size
     */
    @Override
    public PageInfo findAll(ContractProductExample example, int page, int size) {
        PageHelper.startPage(page,size );
        List<ContractProduct> contractProductList = contractProductDao.selectByExample(example);
        return new PageInfo(contractProductList);
    }

    /**
     * 保存
     *
     * @param contractProduct
     */
    @Override
    public void save(ContractProduct contractProduct) {
        //设置货物的总金额
        double amount = 0d;
        //当货物数量和价钱都不为空时
        if (contractProduct.getCnumber() !=null&&contractProduct.getPrice()!=null){
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
        }
        contractProduct.setAmount(amount);
        contractProduct.setId(UtilFuns.generateId());
        contractProductDao.insertSelective(contractProduct);
        //更新合同金钱
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()+contractProduct.getAmount());
        contract.setProNum(contract.getProNum()+1);
        contractDao.updateByPrimaryKeySelective(contract);

    }

    /**
     * 更新
     *
     * @param contractProduct
     */
    @Override
    public void update(ContractProduct contractProduct) {
        //拿出数据库货物的原始总金额
        ContractProduct contractProduct1 = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        Double oldAmount = contractProduct1.getAmount();

        //设置货物的总金额
        double amount = 0d;
        //当货物数量和价钱都不为空时
        if (contractProduct.getCnumber() !=null&&contractProduct.getPrice()!=null){
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
        }
        contractProduct.setAmount(amount);
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
        //更新合同金钱
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()+contractProduct.getAmount()-oldAmount);
        contractDao.updateByPrimaryKeySelective(contract);

    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        //1.先把货物的总金额算出来
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        Double amount = contractProduct.getAmount();
        //得到货物的附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        //循环遍历
        //得到附件总金额
        double excAmount = 0d;
        for (ExtCproduct extCproduct : extCproducts) {
            excAmount += extCproduct.getAmount();
            //删除附件
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }
        //得到合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //设置合同金额
        contract.setTotalAmount(contract.getTotalAmount()-amount-excAmount);
        //设计货物种类
        contract.setProNum(contract.getProNum()-1);
        //设计附件数量,减去附件集合的长度，也就是数量
        contract.setExtNum(contract.getExtNum()-extCproducts.size());
        //删除货物
        contractProductDao.deleteByPrimaryKey(id);
        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @Override
    public ContractProduct findById(String id) {

        return contractProductDao.selectByPrimaryKey(id);

    }


}
