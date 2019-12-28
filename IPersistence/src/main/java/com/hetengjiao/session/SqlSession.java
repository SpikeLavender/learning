package com.hetengjiao.session;

import com.hetengjiao.session.result.ResultHandler;

import java.util.List;

public interface SqlSession {
//	//查询所有
//	<E> List<E> selectList(String statementId, Object... params) ;
//
//	//根据条件查询单个
//	<T> T selectOne(String statementId, Object... params);

	//为Dao接口生成代理实现类
	//<T> T getMapper(Class<?> mapClass);


	/**
	 * Retrieve a single row mapped from the statement key
	 * @param <T> the returned object type
	 * @param statement
	 * @return Mapped object
	 */
	<T> T selectOne(String statement);

	/**
	 * Retrieve a single row mapped from the statement key and parameter.
	 * @param <T> the returned object type
	 * @param statement Unique identifier matching the statement to use.
	 * @param parameter A parameter object to pass to the statement.
	 * @return Mapped object
	 */
	<T> T selectOne(String statement, Object parameter);

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter.
	 * @param <E> the returned list element type
	 * @param statement Unique identifier matching the statement to use.
	 * @return List of mapped object
	 */
	<E> List<E> selectList(String statement);

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter.
	 * @param <E> the returned list element type
	 * @param statement Unique identifier matching the statement to use.
	 * @param parameter A parameter object to pass to the statement.
	 * @return List of mapped object
	 */
	<E> List<E> selectList(String statement, Object parameter);

	/**
	 * Retrieve a single row mapped from the statement key and parameter
	 * using a {@code ResultHandler}.
	 * @param statement Unique identifier matching the statement to use.
	 * @param parameter A parameter object to pass to the statement.
	 * @param handler ResultHandler that will handle each retrieved row
	 */
	void select(String statement, Object parameter, ResultHandler handler);

	/**
	 * Retrieve a single row mapped from the statement
	 * using a {@code ResultHandler}.
	 * @param statement Unique identifier matching the statement to use.
	 * @param handler ResultHandler that will handle each retrieved row
	 */
	void select(String statement, ResultHandler handler);

	/**
	 * Execute an insert statement.
	 * @param statement Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the insert.
	 */
	int insert(String statement);

	/**
	 * Execute an insert statement with the given parameter object. Any generated
	 * autoincrement values or selectKey entries will modify the given parameter
	 * object properties. Only the number of rows affected will be returned.
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.
	 * @return int The number of rows affected by the insert.
	 */
	int insert(String statement, Object parameter);

	/**
	 * Execute an update statement. The number of rows affected will be returned.
	 * @param statement Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the update.
	 */
	int update(String statement);

	/**
	 * Execute an update statement. The number of rows affected will be returned.
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.
	 * @return int The number of rows affected by the update.
	 */
	int update(String statement, Object parameter);

	/**
	 * Execute a delete statement. The number of rows affected will be returned.
	 * @param statement Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the delete.
	 */
	int delete(String statement);

	/**
	 * Execute a delete statement. The number of rows affected will be returned.
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.
	 * @return int The number of rows affected by the delete.
	 */
	int delete(String statement, Object parameter);

	/**
	 * Retrieves current configuration
	 * @return Configuration
	 */
	Configuration getConfiguration();

	/**
	 * Retrieves a mapper.
	 * @param <T> the mapper type
	 * @param type Mapper interface class
	 * @return a mapper bound to this SqlSession
	 */
	<T> T getMapper(Class<T> type);
}
