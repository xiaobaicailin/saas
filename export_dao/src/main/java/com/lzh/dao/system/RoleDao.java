package com.lzh.dao.system;


import com.lzh.domain.system.Role;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;


public interface RoleDao {

    //根据id查询
    Role findById(String id);

    //查询全部用户
    List<Role> findAll(String companyId);

	//根据id删除
    int delete(String id);

	//添加
    int save(Role role);

	//更新
    int update(Role role);

    //根据roleid删除原来权限
    void deleteRoleModule(String id);

    //保存树形图
    void saveRoleModules(@Param("roleId") String roleId,@Param("moduleId") String moduleId);
}