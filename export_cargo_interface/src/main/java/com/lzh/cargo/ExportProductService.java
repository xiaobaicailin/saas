package com.lzh.cargo;


import com.lzh.domain.cargo.ExportProduct;
import com.github.pagehelper.PageInfo;
import com.lzh.domain.cargo.ExportProductExample;
import com.lzh.domain.cargo.ExtCproductExample;

import java.util.List;


public interface ExportProductService {

	ExportProduct findById(String id);

	void save(ExportProduct exportProduct);

	void update(ExportProduct exportProduct);

	void delete(String id);

	PageInfo findAll(String companyId, int page, int size);

	List<ExportProduct> findAll(ExportProductExample example);
}
