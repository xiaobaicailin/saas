package com.lzh.dao.cargo;

import com.lzh.domain.cargo.Contract;
import com.lzh.domain.cargo.ContractExample;
import com.lzh.domain.cargo.ContractProductVo;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface ContractDao {

	//删除
    int deleteByPrimaryKey(String id);

	//保存
    int insertSelective(Contract record);

	//条件查询
    List<Contract> selectByExample(ContractExample example);

	//id查询
    Contract selectByPrimaryKey(String id);

	//更新
    int updateByPrimaryKeySelective(Contract record);

    //print 根据船期和公司id 查询合同
    List<ContractProductVo> print(@Param("shipTime") String shipTime, @Param("companyId") String companyId);
}