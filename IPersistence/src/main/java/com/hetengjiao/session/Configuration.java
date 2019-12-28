package com.hetengjiao.session;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.executor.BatchExecutor;
import com.hetengjiao.executor.Executor;
import com.hetengjiao.executor.SimpleExecutor;
import com.hetengjiao.executor.parameter.DefaultParameterHandler;
import com.hetengjiao.executor.parameter.ParameterHandler;
import com.hetengjiao.executor.resultset.DefaultResultSetHandler;
import com.hetengjiao.executor.resultset.ResultSetHandler;
import com.hetengjiao.executor.statement.RoutingStatementHandler;
import com.hetengjiao.executor.statement.StatementHandler;
import com.hetengjiao.session.result.ResultHandler;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

	private DataSource dataSource;

	/**
	 * key: statementId     value: 封装好的MappedStatement对象
	 */
	Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

	protected final MapperRegistry mapperRegistry = new MapperRegistry(this);

	protected ExecutorType defaultExecutorType = ExecutorType.SIMPLE;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Map<String, MappedStatement> getMappedStatementMap() {
		return mappedStatementMap;
	}

	public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
		this.mappedStatementMap = mappedStatementMap;
	}

	public boolean hasStatement(String statementId) {
		return mappedStatementMap.containsKey(statementId);
	}

	public MappedStatement getMappedStatement(String statementId) {
		return mappedStatementMap.get(statementId);
	}

	public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement,
	                                            Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
		StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject, resultHandler, boundSql);
		return statementHandler;
	}

	public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
		return parameterHandler;
	}

	public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, ResultHandler resultHandler, BoundSql boundSql) {
		ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql);
		return resultSetHandler;
	}

	public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
		return mapperRegistry.getMapper(type, sqlSession);
	}

	public <T> void addMapper(Class<T> type) {
		mapperRegistry.addMapper(type);
	}

	public void addMappers(String packageName, Class<?> superType) {
		mapperRegistry.addMappers(packageName, superType);
	}

	public void addMappers(String packageName) {
		mapperRegistry.addMappers(packageName);
	}

	public boolean hasMapper(Class<?> type) {
		return mapperRegistry.hasMapper(type);
	}

	public MapperRegistry getMapperRegistry() {
		return mapperRegistry;
	}

	public ExecutorType getDefaultExecutorType() {
		return defaultExecutorType;
	}

	public void setDefaultExecutorType(ExecutorType defaultExecutorType) {
		this.defaultExecutorType = defaultExecutorType;
	}

	public Executor newExecutor(ExecutorType executorType) {
		executorType = executorType == null ? defaultExecutorType : executorType;
		executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
		Executor executor;
		if (ExecutorType.BATCH == executorType) {
			executor = new BatchExecutor(this);
		} else {
			executor = new SimpleExecutor(this);
		}
//		if (cacheEnabled) {
//			executor = new CachingExecutor(executor);
//		}
//		executor = (Executor) interceptorChain.pluginAll(executor);
		return executor;
	}
}
