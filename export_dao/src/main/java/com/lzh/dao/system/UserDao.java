package com.lzh.dao.system;

import com.lzh.domain.system.User;
import com.lzh.domain.system.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface UserDao {

	//根据企业id查询全部
	List<User> findAll(String companyId);

	//根据id查询
    User findById(String userId);

	//根据id删除
	int delete(String userId);

	//保存
	int save(User user);

	//更新
	int update(User user);

	//查找该用户的所有角色列表
	List<String> findRoleById(String userId);

	//根据userid删除该用户下的role
	void deleteRoleByUserId(String userid);

	//保存该用户下的role
	void saveRoleById(@Param("userid") String userid,@Param("roleid") String roleid);

	//根据email查询得到user
	User findUserByemail(String email);
}