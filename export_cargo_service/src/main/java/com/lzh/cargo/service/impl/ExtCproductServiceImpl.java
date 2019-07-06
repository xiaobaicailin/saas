package com.lzh.cargo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.cargo.ExtCproductService;
import com.lzh.common.utils.UtilFuns;
import com.lzh.dao.cargo.ContractDao;
import com.lzh.dao.cargo.ExtCproductDao;
import com.lzh.domain.cargo.Contract;
import com.lzh.domain.cargo.ExtCproduct;
import com.lzh.domain.cargo.ExtCproductExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ContractDao contractDao;

    /**
     * 保存
     *
     * @param extCproduct
     */
    @Override
    public void save(ExtCproduct extCproduct) {
        //计算附件总金额
        double amount = 0d;
        if (extCproduct.getCnumber()!=null&&extCproduct.getPrice()!=null){
            amount += extCproduct.getCnumber() *extCproduct.getPrice();
        }
        //保存附件的总金额
        extCproduct.setAmount(amount);
        //创建id
        extCproduct.setId(UtilFuns.generateId());
        //创建合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //设置合同金额
        contract.setTotalAmount(contract.getTotalAmount()+amount);
        //设置附件数量
        contract.setExtNum(contract.getExtNum()+1);
        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);
        //保存附件
        extCproductDao.insertSelective(extCproduct);
    }

    /**
     * 更新
     *
     * @param extCproduct
     */
    @Override
    public void update(ExtCproduct extCproduct) {

        //得到数据库的excproduct的金额
        ExtCproduct extCproduct1 = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        Double dbAmount = extCproduct1.getAmount();

        //计算附件总金额
        double amount = 0d;
        if (extCproduct.getCnumber()!=null&&extCproduct.getPrice()!=null){
            amount += extCproduct.getCnumber() *extCproduct.getPrice();
        }
        //保存附件的总金额
        extCproduct.setAmount(amount);
        //创建合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //设置合同金额
        contract.setTotalAmount(contract.getTotalAmount()-dbAmount+amount);
        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);
        //更新附件
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        //得到附件总金额
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        Double amount = extCproduct.getAmount();
        //删除附件
        extCproductDao.deleteByPrimaryKey(id);
        //更新合同金额
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //设置合同金额
        contract.setTotalAmount(contract.getTotalAmount()-amount);
        //设置附件数
        contract.setExtNum(contract.getExtNum()-1);
        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @Override
    public ExtCproduct findById(String id) {

        return extCproductDao.selectByPrimaryKey(id);
    }

    /**
     * 分页查询
     *
     * @param example
     * @param page
     * @param size
     */
    @Override
    public PageInfo findAll(ExtCproductExample example, int page, int size) {
        PageHelper.startPage(page,size );
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(example);
        return new PageInfo(extCproductList);
    }
}
