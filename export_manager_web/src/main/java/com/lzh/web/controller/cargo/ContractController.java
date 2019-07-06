package com.lzh.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.lzh.cargo.ContractService;
import com.lzh.common.utils.DownloadUtil;
import com.lzh.common.utils.UtilFuns;
import com.lzh.domain.cargo.Contract;
import com.lzh.domain.cargo.ContractExample;
import com.lzh.domain.cargo.ContractProductVo;
import com.lzh.domain.system.Dept;
import com.lzh.domain.system.User;
import com.lzh.service.system.DeptService;
import com.lzh.web.controller.BaseController;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jms.IllegalStateException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController{

    @Reference
    private ContractService contractService;

    @Autowired
    private DeptService deptService;

    //展示合同列表
    @RequestMapping("/list.do")
    public String list( @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size) throws IllegalStateException {
        User currentUser = super.getCurrentUser();
        ContractExample example = new ContractExample();
        ContractExample.Criteria criteria = example.createCriteria();
        Integer degree = currentUser.getDegree();
        switch (degree){
            case 0:
                //saas管理员
                throw new IllegalStateException("saas不能查看企业信息");
            case 1:
                //1-企业管理员
                criteria.andCompanyIdEqualTo(currentUser.getCompanyId());
                break;
            case 2:
                //  2-管理所有下属部门和人员
                List<String> byParentId = deptService.findByParentId(currentUser.getDeptId());
                criteria.andCreateDeptIn(byParentId);
                break;
            case 3:
                //3-管理本部门
                criteria.andCreateDeptEqualTo(currentUser.getDeptId());
                break;
            case 4:
                //4-普通员工
                criteria.andCreateByEqualTo(currentUser.getId());
                break;
            default:
                break;
        }
        //criteria.andCompanyIdEqualTo(currentUser.getCompanyId());
        PageInfo pageInfo = contractService.findAll(example, page, size);
        request.setAttribute("page", pageInfo);
        return "cargo/contract/contract-list";
    }

    //前往添加页面
    @RequestMapping("/toAdd.do")
    public String toAdd(){

        return "cargo/contract/contract-add";
    }

    //保存或更新
    @RequestMapping("/edit.do")
    public String edit(Contract contract){
        //判断合同id是否存在
        if (!UtilFuns.isEmpty(contract.getId())){
            //更新
            contractService.update(contract);
        }else {
            //保存
            contract.setCompanyId(super.getCurrentUser().getCompanyId());
            contract.setCompanyName(super.getCurrentUser().getCompanyName());
            contractService.save(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }

    //更新
    @RequestMapping("/toUpdate.do")
    public String toUpdate(String id){
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);
        return "cargo/contract/contract-update";
    }

    //删除
    @RequestMapping("/delete.do")
    public String delete(String id){
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    //提交
    @RequestMapping("/submit.do")
    public String submit(String id) throws Exception {
        Contract contract = contractService.findById(id);
        //判断 是否已经提交
        //1.草稿
        if (contract.getState()==0){
            contract.setState(1);
            contractService.update(contract);
        }else {
            response.getWriter().print("<script> alert('已经上报') </script>");
        }
        return "redirect:/cargo/contract/list.do";
    }

    //取消提交
    @RequestMapping("/cancel.do")
    public String cancel(String id)throws Exception{
        Contract contract = contractService.findById(id);
        //判断 是否已经提交
        //1.上报
        if (contract.getState()==1){
            contract.setState(0);
            contractService.update(contract);
        }else {
            response.getWriter().print("<script> alert('无法取消') </script>");
        }
        return "redirect:/cargo/contract/list.do";
    }

  /*  //打印出货表（无格式）
    @RequestMapping("/printExcel.do")
    public void printExcel(String inputDate) throws IOException {
        String companyId = super.getCurrentUser().getCompanyId();
        List<ContractProductVo> contractProductVoList = contractService.print(inputDate, companyId);

        int rowNum = 0;
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        //大标题
        Row bigRow = sheet.createRow(rowNum++);
        Cell cell = bigRow.createCell(1);
        cell.setCellValue(inputDate.replace("-0","-" ).replace("-","年" )+"月份出货表");
        //小标题
        Row littleRow = sheet.createRow(rowNum++);
        String[] small_title = {"客户", "订单号", "货号", "数量", "工厂", "工厂交期", "船期", "贸易条款"};
        for (int i = 0;i<small_title.length;i++){
            Cell cell1 = littleRow.createCell(i + 1);
            cell1.setCellValue(small_title[i]);
        }
        //将contractProductVoList内容添加到内容中
        for (ContractProductVo contractProductVo : contractProductVoList) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 1;
            row.createCell(cellNum++).setCellValue(contractProductVo.getCustomName());
            row.createCell(cellNum++).setCellValue(contractProductVo.getContractNo());
            row.createCell(cellNum++).setCellValue(contractProductVo.getProductNo());
            row.createCell(cellNum++).setCellValue(contractProductVo.getCnumber());
            row.createCell(cellNum++).setCellValue(contractProductVo.getFactoryName());
            row.createCell(cellNum++).setCellValue(contractProductVo.getDeliveryPeriod());
            row.createCell(cellNum++).setCellValue(contractProductVo.getShipTime());
            row.createCell(cellNum).setCellValue(contractProductVo.getTradeTerms());
        }

        //下载
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        new DownloadUtil().download(byteArrayOutputStream, response, "出货表.xlsx");

    }*/

    //打印出货表（有格式）
    @RequestMapping("/printExcel.do")
    public void printExcel(String inputDate) throws IOException {
        String companyId = super.getCurrentUser().getCompanyId();
        List<ContractProductVo> contractProductVoList = contractService.print(inputDate, companyId);

        int rowNum = 0;
        String path = session.getServletContext().getRealPath("/make/xlsprint/tOUTPRODUCT.xlsx");
        Workbook workbook = new XSSFWorkbook(path);
        Sheet sheet = workbook.getSheetAt(0);
        //大标题
        Row bigRow = sheet.getRow(rowNum++);
        Cell cell = bigRow.getCell(1);
        cell.setCellValue(inputDate.replace("-0","-" ).replace("-","年" )+"月份出货表");
        //小标题
        rowNum++;
        //将contractProductVoList内容添加到内容中
        //并且要处理第三行的样式
        //创建数组存储第三行的样式
        CellStyle[] css = new CellStyle[9];
        Row row = sheet.getRow(rowNum++);
        for (int i = 1; i < row.getLastCellNum(); i++) {
              css[i] = row.getCell(i).getCellStyle();
        }
        for (ContractProductVo contractProductVo : contractProductVoList) {
            Row row_content = sheet.createRow(rowNum++);
            int cellNum = 1;
            int cellStyle = 1;
            Cell cell1 = row_content.createCell(cellNum++);
            cell1.setCellValue(contractProductVo.getCustomName());
            cell1.setCellStyle(css[cellStyle++]);

            Cell cell2 = row_content.createCell(cellNum++);
            cell2.setCellValue(contractProductVo.getContractNo());
            cell2.setCellStyle(css[cellStyle++]);

            Cell cell3 = row_content.createCell(cellNum++);
            cell3.setCellValue(contractProductVo.getProductNo());
            cell3.setCellStyle(css[cellStyle++]);

            Cell cell4 = row_content.createCell(cellNum++);
            cell4.setCellValue(contractProductVo.getCnumber());
            cell4.setCellStyle(css[cellStyle++]);

            Cell cell5 = row_content.createCell(cellNum++);
            cell5.setCellValue(contractProductVo.getFactoryName());
            cell5.setCellStyle(css[cellStyle++]);

            Cell cell6 = row_content.createCell(cellNum++);
            cell6.setCellValue(contractProductVo.getDeliveryPeriod());
            cell6.setCellStyle(css[cellStyle++]);

            Cell cell7 = row_content.createCell(cellNum++);
            cell7.setCellValue(contractProductVo.getShipTime());
            cell7.setCellStyle(css[cellStyle++]);

            Cell cell8 = row_content.createCell(cellNum);
            cell8.setCellValue(contractProductVo.getTradeTerms());
            cell8.setCellStyle(css[cellStyle]);

        }

        //下载
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        new DownloadUtil().download(byteArrayOutputStream, response, "出货表.xlsx");

    }


    //跳转出货表
    @RequestMapping("/print.do")
    public String print(){
        return "cargo/print/contract-print";
    }
}
