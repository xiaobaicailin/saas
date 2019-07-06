package com.lzh.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.dao.system.SysLogDao;
import com.lzh.domain.system.SysLog;
import com.lzh.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;
    @Override
    public PageInfo findAll(String companyId,int page,int size) {
        PageHelper.startPage(page,size);
        List<SysLog> logList = sysLogDao.findAll(companyId);
        return new PageInfo(logList);
    }

    @Override
    public void save(SysLog log) {
        log.setId(UtilFuns.generateId());
        sysLogDao.save(log);
    }
}
