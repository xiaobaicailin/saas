package com.lzh.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.lzh.cargo.ExtCproductService;
import com.lzh.cargo.FactoryService;
import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.cargo.ExtCproduct;
import com.lzh.domain.cargo.ExtCproductExample;
import com.lzh.domain.cargo.Factory;
import com.lzh.domain.cargo.FactoryExample;
import com.lzh.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController{

    @Reference
    private ExtCproductService extCproductService;

    @Reference
    private FactoryService factoryService;

    @RequestMapping("/list.do")
    public String list(String contractId, String contractProductId, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);
        request.setAttribute("contractProductId", contractProductId);
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);
        PageInfo all = extCproductService.findAll(extCproductExample, page, size);
        request.setAttribute("page",all );
        request.setAttribute("contractId", contractId);
        return "cargo/extc/extc-list";
    }

    @RequestMapping("/toUpdate.do")
    public String toUpdate(String id,String contractId,String contractProductId){
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> all = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", all);
        request.setAttribute("contractProductId", contractProductId);
        request.setAttribute("contractId", contractId);
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct", extCproduct);
        return "cargo/extc/extc-update";
    }

    @RequestMapping("/edit.do")
    public String edit(ExtCproduct extCproduct){
        if (UtilFuns.isEmpty(extCproduct.getId())){
            extCproduct.setCompanyId(super.getCurrentUser().getCompanyId());
            extCproduct.setCompanyName(super.getCurrentUser().getCompanyName());
            extCproductService.save(extCproduct);
        }else {
            extCproductService.update(extCproduct);
        }
        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();
    }

    @RequestMapping("/delete.do")
    public String delete(String id,String contractId,String contractProductId){
        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }
}
