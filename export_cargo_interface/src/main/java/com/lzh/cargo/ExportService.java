package com.lzh.cargo;

import com.lzh.domain.cargo.Export;
import com.lzh.domain.cargo.ExportExample;
import com.github.pagehelper.PageInfo;


public interface ExportService {

    Export findById(String id);

    void save(Export export);

    void update(Export export);

    void delete(String id);

	PageInfo findAll(ExportExample example, int page, int size);
}
