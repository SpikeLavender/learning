package com.hetengjiao.session;

import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.executor.Executor;
import com.hetengjiao.session.result.ResultHandler;

import java.util.List;

public class DefaultSqlSession implements SqlSession {

	private final Configuration configuration;
	private final Executor executor;

	public DefaultSqlSession(Configuration configuration, Executor executor) {
		this.configuration = configuration;
		this.executor = executor;
	}

//	@Override
//	public <E> List<E> selectList(String statementId, Object... params) {
//		//将要去完成对SimpleExecutor里的query方法的调用
//		MappedStatement statement = configuration.getMappedStatement(statementId);
//		List<E> res = null;
//		try {
//			res = executor.query(configuration, statement, params);
//		} catch (Exception e) {
//			throw new RuntimeException("Error select database.  Cause: " + e, e);
//		}
//		return res;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public <T> T selectOne(String statementId, Object... params) {
//		List<Object> objects =  selectList(statementId, params);
//		if (objects.size() == 1) {
//			return (T) objects.get(0);
//		} else {
//			throw new RuntimeException("查询结果为空或者返回结果过多");
//		}
//	}

//	@Override
//	@SuppressWarnings("unchecked")
//	public <T> T getMapper(Class<?> mapClass) {
//		//使用JDK动态代理来为Dao接口生产代理对象，并返回
//		Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(),
//				new Class[]{mapClass}, new InvocationHandler() {
//			/**
//			 * 代理对象调用接口中的任意方法，都会执行invoke方法
//			 * @param proxy     当前代理对象的引用
//			 * @param method    当前被调用方法的引用
//			 * @param args      传递的参数
//			 */
//			@Override
//			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//				// 底层还是去执行JDBC代码
//				// 根据不同情况，来调用selectList或者selectOne
//				// 准备参数1: statementId: sql语句的唯一标识: namespace.id=接口全限定名.方法名
//				// 方法名: findAll
//				String methodName = method.getName();
//				String className = method.getDeclaringClass().getName();
//
//				String statementId = className + "." + methodName;
//				// 准备参数2: params: args
//				// 获取被调用方法的返回值类型
//				Type genericReturnType = method.getGenericReturnType();
//				// 判断是否进行了 泛型类型参数化
////				if (genericReturnType instanceof ParameterizedType) {
////					List<Object> objects = selectList(statementId, args);
////					return objects;
////				}
//
//				return method.invoke(proxy, args);
//			}
//		});
//
//		return (T) proxyInstance;
//	}

	/**
	 * Retrieve a single row mapped from the statement key
	 *
	 * @param statement
	 * @return Mapped object
	 */
	@Override
	public <T> T selectOne(String statement) {
		return selectOne(statement, null);
	}

	/**
	 * Retrieve a single row mapped from the statement key and parameter.
	 *
	 * @param statement Unique identifier matching the statement to use.
	 * @param parameter A parameter object to pass to the statement.
	 * @return Mapped object
	 */
	@Override
	public <T> T selectOne(String statement, Object parameter) {
		List<T> list = this.selectList(statement, parameter);
		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() > 1) {
			throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
		} else {
			return null;
		}
	}

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter.
	 *
	 * @param statement Unique identifier matching the statement to use.
	 * @return List of mapped object
	 */
	@Override
	public <E> List<E> selectList(String statement) {
		return selectList(statement, null);
	}

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter.
	 *
	 * @param statement Unique identifier matching the statement to use.
	 * @param parameter A parameter object to pass to the statement.
	 * @return List of mapped object
	 */
	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		try {
			MappedStatement ms = configuration.getMappedStatement(statement);
			return executor.query(ms, parameter, Executor.NO_RESULT_HANDLER);
		} catch (Exception e) {
			throw new RuntimeException("Error querying database.  Cause: " + e, e);
		}
	}

	/**
	 * Retrieve a single row mapped from the statement key and parameter
	 * using a {@code ResultHandler}.
	 *
	 * @param statement Unique identifier matching the statement to use.
	 * @param parameter A parameter object to pass to the statement.
	 * @param handler   ResultHandler that will handle each retrieved row
	 */
	@Override
	public void select(String statement, Object parameter, ResultHandler handler) {
		try {
			MappedStatement ms = configuration.getMappedStatement(statement);
			executor.query(ms, parameter, handler);
		} catch (Exception e) {
			throw new RuntimeException("Error querying database.  Cause: " + e, e);
		}
	}

	/**
	 * Retrieve a single row mapped from the statement
	 * using a {@code ResultHandler}.
	 *
	 * @param statement Unique identifier matching the statement to use.
	 * @param handler   ResultHandler that will handle each retrieved row
	 */
	@Override
	public void select(String statement, ResultHandler handler) {
		select(statement, null, handler);
	}

	/**
	 * Execute an insert statement.
	 *
	 * @param statement Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the insert.
	 */
	@Override
	public int insert(String statement) {
		return insert(statement, null);
	}

	/**
	 * Execute an insert statement with the given parameter object. Any generated
	 * autoincrement values or selectKey entries will modify the given parameter
	 * object properties. Only the number of rows affected will be returned.
	 *
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.
	 * @return int The number of rows affected by the insert.
	 */
	@Override
	public int insert(String statement, Object parameter) {
		return update(statement, parameter);
	}

	/**
	 * Execute an update statement. The number of rows affected will be returned.
	 *
	 * @param statement Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the update.
	 */
	@Override
	public int update(String statement) {
		return update(statement, null);
	}

	/**
	 * Execute an update statement. The number of rows affected will be returned.
	 *
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.
	 * @return int The number of rows affected by the update.
	 */
	@Override
	public int update(String statement, Object parameter) {
		MappedStatement ms = configuration.getMappedStatement(statement);
		try {
			return executor.update(ms, parameter);
		} catch (Exception e) {
			throw new RuntimeException("Error updating database.  Cause: " + e, e);
		}
	}

	/**
	 * Execute a delete statement. The number of rows affected will be returned.
	 *
	 * @param statement Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the delete.
	 */
	@Override
	public int delete(String statement) {
		return update(statement, null);
	}

	/**
	 * Execute a delete statement. The number of rows affected will be returned.
	 *
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.
	 * @return int The number of rows affected by the delete.
	 */
	@Override
	public int delete(String statement, Object parameter) {
		return update(statement, parameter);
	}

	/**
	 * Retrieves current configuration
	 *
	 * @return Configuration
	 */
	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Retrieves a mapper.
	 *
	 * @param type Mapper interface class
	 * @return a mapper bound to this SqlSession
	 */
	@Override
	public <T> T getMapper(Class<T> type) {
		return configuration.getMapper(type, this);
	}
}
