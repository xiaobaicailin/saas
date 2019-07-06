package com.lzh.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.common.utils.UtilFuns;
import com.lzh.dao.system.UserDao;
import com.lzh.domain.system.User;
import com.lzh.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        PageHelper.startPage(page,size);
        List<User> userList = userDao.findAll(companyId);
        return new PageInfo(userList);
    }

    @Override
    public User findById(String userId) {
        User userByid = userDao.findById(userId);
        return userByid;
    }

    @Override
    public void delete(String userId) {
        userDao.delete(userId);

    }

    @Override
    public void save(User user) {
        user.setId(UtilFuns.generateId());
        userDao.save(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public List<String> findRoleById(String userId) {
        return userDao.findRoleById(userId);
    }

    @Override
    public void saveChange(String userid, String roleIds) {
        //先删除
        userDao.deleteRoleByUserId(userid);
        //再添加
        String[] roleId = roleIds.split(",");
        for (String s : roleId) {
            userDao.saveRoleById(userid,s);
        }
    }

    @Override
    public User findUserByemail(String email) {
        return userDao.findUserByemail(email);
    }
}
