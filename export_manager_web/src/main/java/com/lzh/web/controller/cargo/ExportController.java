package com.lzh.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.lzh.cargo.ContractService;
import com.lzh.cargo.ExportProductService;
import com.lzh.cargo.ExportService;
import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.cargo.*;
import com.lzh.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference
    private ContractService contractService;

    @Reference
    private ExportService exportService;

    @Reference
    private ExportProductService exportProductService;

    //合同管理列表
    @RequestMapping("/contractList.do")
    public String toList(@RequestParam(defaultValue = "1") int page , @RequestParam(defaultValue = "5") int size){
        ContractExample example = new ContractExample();
        example.createCriteria().andStateEqualTo(1).andCompanyIdEqualTo(super.getCurrentUser().getCompanyId());
       example.setOrderByClause("create_time desc");
        PageInfo all = contractService.findAll(example, page, size);
        request.setAttribute("page", all);
        return "cargo/export/export-contractList";
    }

    //出口报运
    @RequestMapping("/list.do")
    public String list(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        ExportExample exportExample = new ExportExample();
        exportExample.setOrderByClause("create_time desc");
        exportExample.createCriteria().andCompanyIdEqualTo(super.getCurrentUser().getCompanyId());
        PageInfo all = exportService.findAll(exportExample, page, size);
        request.setAttribute("page",all );
        return "cargo/export/export-list";
    }

    //多选报运
    @RequestMapping("/toExport.do")
    public String toExport(String id){
            request.setAttribute("id",id );
        return "cargo/export/export-toExport";
    }

    //保存这一批报运合同到报运单中
    @RequestMapping("/edit.do")
    public String edit(Export export){
        if (UtilFuns.isEmpty(export.getId())){
            //保存
            export.setCompanyName(super.getCurrentUser().getCompanyName());
            export.setCompanyId(super.getCurrentUser().getCompanyId());
            exportService.save(export);
        }else {
            //更新
            exportService.update(export);
        }
        return "redirect:/cargo/export/list.do";
    }

    //出口运单编辑更新
    @RequestMapping("/toUpdate.do")
    public String toUpdate(String id){
        //根据id得到export
        Export export = exportService.findById(id);
        //将export存储到request域中
        request.setAttribute("export", export);
        //将运单货物集合也存储到request域中
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> all = exportProductService.findAll(exportProductExample);
        request.setAttribute("eps",all );
        return "cargo/export/export-update";
    }

    //删除
    @RequestMapping("/delete.do")
    public String delete(String id){
        exportService.delete(id);
        return "redirect:/cargo/export/list.do";
    }

}
