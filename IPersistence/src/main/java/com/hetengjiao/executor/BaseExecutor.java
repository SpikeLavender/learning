package com.hetengjiao.executor;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.session.Configuration;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.session.result.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseExecutor implements Executor {

	protected Configuration configuration;

	public BaseExecutor(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler) throws Exception {
		BoundSql boundSql = ms.getBoundSql();
		return queryFromDatabase(ms, parameter, resultHandler, boundSql);
	}

	@Override
	public int update(MappedStatement ms, Object parameter) throws Exception {

		return doUpdate(ms, parameter);
	}

	protected abstract int doUpdate(MappedStatement ms, Object parameter)
			throws Exception;

	private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) throws Exception {
		List<E> list;

		list = doQuery(ms, parameter, resultHandler, boundSql);

		return list;
	}

	protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql)
			throws Exception;

	protected Connection getConnection() {
		try {
			return configuration.getDataSource().getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("获取连接池失败");
		}
	}

	protected void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}
}
