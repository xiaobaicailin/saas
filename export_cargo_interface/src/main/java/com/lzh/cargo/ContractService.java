package com.lzh.cargo;

import com.github.pagehelper.PageInfo;
import com.lzh.domain.cargo.Contract;
import com.lzh.domain.cargo.ContractExample;
import com.lzh.domain.cargo.ContractProductVo;

import java.util.List;

public interface ContractService {

	//根据id查询
    Contract findById(String id);

    //保存
    void save(Contract contract);

    //更新
    void update(Contract contract);

    //删除
    void delete(String id);

    //分页查询
	public PageInfo findAll(ContractExample example, int page, int size);

	//print 根据船期和公司id 查询合同
      List<ContractProductVo> print(String shipTime, String companyId);
}
