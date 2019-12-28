package com.hetengjiao.executor.statement;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.executor.Executor;
import com.hetengjiao.session.result.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleStatementHandler extends BaseStatementHandler {
	public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
		super(executor, mappedStatement, parameterObject, resultHandler, boundSql);
	}

	@Override
	protected Statement instantiateStatement(Connection connection) throws SQLException {

		return connection.createStatement();
	}

	@Override
	public void parameterize(Statement statement) throws Exception {

	}

	@Override
	public void batch(Statement statement) throws Exception {

	}

	@Override
	public int update(Statement statement) throws Exception {
		String sql = boundSql.getSqlText();
		int rows;
		statement.execute(sql);
		rows = statement.getUpdateCount();
		return rows;
	}

	@Override
	public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws Exception {
		String sql = boundSql.getSqlText();
		statement.execute(sql);
		return resultSetHandler.handleResultSets(statement);
	}
}
