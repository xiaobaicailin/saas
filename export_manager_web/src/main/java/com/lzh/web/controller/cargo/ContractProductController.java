package com.lzh.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.lzh.cargo.ContractProductService;
import com.lzh.cargo.FactoryService;
import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.cargo.ContractProduct;
import com.lzh.domain.cargo.ContractProductExample;
import com.lzh.domain.cargo.Factory;
import com.lzh.domain.cargo.FactoryExample;
import com.lzh.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;

    @Reference
    private FactoryService factoryService;

    //货物列表
    @RequestMapping("/list.do")
    public String list(String contractId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size){
        ContractProductExample example = new ContractProductExample();
        example.createCriteria().andContractIdEqualTo(contractId);
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);
        PageInfo pageInfo = contractProductService.findAll(example, page, size);
        request.setAttribute("page", pageInfo);
        request.setAttribute("contractId", contractId);

        return "cargo/product/product-list";
    }

    //保存或更新
    @RequestMapping("/edit.do")
    public String edit( ContractProduct contractProduct, MultipartFile productPhoto){
        if (UtilFuns.isEmpty(contractProduct.getId())){
            //保存
            contractProduct.setCompanyName(super.getCurrentUser().getCompanyName());
            contractProduct.setCompanyId(super.getCurrentUser().getCompanyId());
            contractProductService.save(contractProduct);


        }else {
            //更新
            contractProductService.update(contractProduct);

        }
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractProduct.getContractId();
    }

    //修改货物
    @RequestMapping("/toUpdate.do")
    public String toUpdate(String id){
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct", contractProduct);
        return "cargo/product/product-update";

    }




    //删除
    @RequestMapping("/delete.do")
    public String delete(String id,String contractId){
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }
}
