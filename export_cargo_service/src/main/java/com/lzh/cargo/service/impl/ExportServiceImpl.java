package com.lzh.cargo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.cargo.ExportService;
import com.lzh.common.utils.UtilFuns;
import com.lzh.dao.cargo.*;
import com.lzh.domain.cargo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ExportProductDao exportProductDao;

    @Autowired
    private ExtEproductDao extEproductDao;

    @Autowired
    private ContractProductDao contractProductDao;


    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        //设置export基本信息
        export.setId(UtilFuns.generateId());
        export.setCreateTime(new Date());
        export.setState(0);
        //得到这批合同的id
        //每个id是以逗号隔开
        String customerContract = "" ;
        //得到合同id数组
        String[] contractIds = export.getContractIds().split(",");
        for (String contractId : contractIds) {
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            //改变合同状态
            contract.setState(2);
            //更新合同
            contractDao.updateByPrimaryKeySelective(contract);
            //拼接合同id
            customerContract += contractId + " ";
        }
        //设置运单号
        export.setCustomerContract(customerContract);

        ContractProductExample contractProductExample = new ContractProductExample();
        //将数组转换为集合  并作为方法参数
        contractProductExample.createCriteria().andContractIdIn(Arrays.asList(contractIds));
        //得到contractProduct集合
        List<ContractProduct> contractProducts = contractProductDao.selectByExample(contractProductExample);

        //创建一个map 来存储  合同货物id   和   运单货物id
        Map<String,String> map = new HashMap<>();
        //创建一个list集合存储运单货物
        List<ExportProduct> list = new ArrayList<>();
        //遍历contractProduct
        for (ContractProduct contractProduct : contractProducts) {

            //创建运单货物
            ExportProduct exportProduct = new ExportProduct();
            //将合同的信息封装到运单货物中
            BeanUtils.copyProperties(contractProduct,exportProduct);
            //保存exportproduct
            exportProduct.setId(UtilFuns.generateId());
            exportProduct.setExportId(export.getId());
            exportProductDao.insertSelective(exportProduct);
            //存储  合同货物id   和   运单货物id
            map.put(contractProduct.getId(), exportProduct.getId());
            list.add(exportProduct);
        }
        //把list封装进export
        export.setExportProducts(list);

        //根据合同id得到所有附件
            ExtCproductExample extCproductExample = new ExtCproductExample();
            extCproductExample.createCriteria().andContractIdIn(Arrays.asList(contractIds));
             List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);

        for (ExtCproduct extCproduct : extCproducts) {
            //创建运单附件
            ExtEproduct extEproduct = new ExtEproduct();
            extEproduct.setId(UtilFuns.generateId());
            //将合同附件封装到运单附件中
            //根据合同附件获得合同货物id,再获得运单货物id
            String exportproductid = map.get(extCproduct.getContractProductId());
            ExportProduct exportProduct = exportProductDao.selectByPrimaryKey(exportproductid);
            extEproduct.setExportProductId(exportProduct.getId());
            //将合同的附件信息封装到运单附件中
            BeanUtils.copyProperties(extCproduct,extEproduct );
            //保存extEproduct
            extEproductDao.insertSelective(extEproduct);
        }
        //设置附件和货物的数量
        export.setExtNum(extCproducts.size());
        export.setProNum(contractProducts.size());
        //保存
        exportDao.insertSelective(export);
    }

    @Override
    public void update(Export export) {
        List<ExportProduct> exportProducts = export.getExportProducts();
        if (exportProducts != null){
            for (ExportProduct exportProduct : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
        exportDao.updateByPrimaryKeySelective(export);
    }

    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ExportExample example, int page, int size) {
        PageHelper.startPage(page,size );
        List<Export> exportList = exportDao.selectByExample(example);
        return new PageInfo(exportList);
    }
}
