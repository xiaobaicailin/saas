package com.lzh.service.system;

import com.github.pagehelper.PageInfo;
import com.lzh.domain.system.User;

import java.util.List;

public interface UserService {
    //根据企业id查询全部，分页
    PageInfo findAll(String companyId, int page, int size);

    //根据id查询
    User findById(String userId);

    //根据id删除
    void delete(String userId);

    //保存
    void save(User user);

    //更新
    void update(User user);

    //找到当前用户的角色列表
    List<String> findRoleById(String userId);

    //保存当前用户的角色改变
    void saveChange(String userid,String roleIds);

    //根据email查询该用户
    User findUserByemail(String email);

}
