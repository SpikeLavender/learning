package com.hetengjiao.dao;

import com.hetengjiao.entity.User;

import java.util.List;

public interface IUserMapper {
	//查询所有用户
	List<User> findAll() throws Exception;

	//根据条件进行用户查询
	User findByCondition(User user) throws Exception;

	//插入
	int addUser(User user);

	//更新
	int updateUser(User user);

	//删除
	int deleteUser(User user);
}
