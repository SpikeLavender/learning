package com.hetengjiao.test;

import com.hetengjiao.dao.IUserMapper;
import com.hetengjiao.entity.User;
import com.hetengjiao.io.Resources;
import com.hetengjiao.session.SqlSession;
import com.hetengjiao.session.SqlSessionFactory;
import com.hetengjiao.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class IPersistenceTest {

	private SqlSession sqlSession;
	@Before
	public void setUp() throws Exception {
		InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
		sqlSession = sqlSessionFactory.openSession();
	}

	@Test
	public void findByConditionTest() throws Exception {
		// 调用
		User user = new User();
		user.setId(1);
		user.setUsername("hetengjiao");

		IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);

		User user1 = userMapper.findByCondition(user);
		System.out.println(user1);

	}

	@Test
	public void findAllTest() throws Exception {

		IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);

		List<User> users = userMapper.findAll();
		users.forEach(System.out::println);
	}

	@Test
	public void addUserTest() throws Exception {

		// 调用
		User user = new User();
		user.setId(120);
		user.setUsername("hetengjiao");

		IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);
		if (userMapper.findByCondition(user) != null) {
			userMapper.deleteUser(user);
		}

		int count = userMapper.addUser(user);
		System.out.println("insert number is " + count);

	}

	@Test
	public void updateUserTest() throws Exception {

		IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);
		User user = new User();
		user.setId(5);
		String name = UUID.randomUUID().toString();
		user.setUsername(name);

		System.out.println("before update" + userMapper.findByCondition(user));

		int count = userMapper.updateUser(user);
		System.out.println("update number is " + count);
		System.out.println("after update" + userMapper.findByCondition(user));
	}

	@Test
	public void deleteUserTest() throws Exception {
		int before, add, delete;
		IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);
		System.out.println(before = userMapper.findAll().size());

		User user = new User();
		user.setId(101);
		user.setUsername("hetengjiao");
		userMapper.addUser(user);
		System.out.println(add = userMapper.findAll().size());
		assert (before + 1 == add);

		int count = userMapper.deleteUser(user);
		System.out.println("delete number is " + count);
		System.out.println(delete = userMapper.findAll().size());

		assert (delete == add - 1);
	}
}
