package com.lzh.service.system;

import com.github.pagehelper.PageInfo;
import com.lzh.domain.system.SysLog;

import java.util.List;

public interface SysLogService {
    //查询全部
    PageInfo findAll(String companyId,int page,int size);

    //添加
    void save(SysLog log);
}
