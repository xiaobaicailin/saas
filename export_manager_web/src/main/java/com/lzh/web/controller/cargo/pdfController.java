package com.lzh.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lzh.cargo.ExportProductService;
import com.lzh.cargo.ExportService;
import com.lzh.common.utils.BeanMapUtils;
import com.lzh.domain.cargo.Export;
import com.lzh.domain.cargo.ExportProduct;
import com.lzh.domain.cargo.ExportProductExample;
import com.lzh.web.controller.BaseController;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cargo/export")
public class pdfController extends BaseController{

    @Reference
    private ExportService exportService;

    @Reference
    private ExportProductService exportProductService;

    @RequestMapping("/exportPdf.do")
    public void pdf(String id) throws JRException, IOException {
        Export export = exportService.findById(id);
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> all = exportProductService.findAll(exportProductExample);
        Map<String, Object> map = BeanMapUtils.beanToMap(export);
        String path = session.getServletContext().getRealPath("/jasper/export.jasper");
        JasperPrint jp = JasperFillManager.fillReport(path, map, new JRBeanCollectionDataSource(all));

        JasperExportManager.exportReportToPdfStream(jp, response.getOutputStream());
    }
}
