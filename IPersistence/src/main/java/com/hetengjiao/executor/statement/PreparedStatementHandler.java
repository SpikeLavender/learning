package com.hetengjiao.executor.statement;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.executor.Executor;
import com.hetengjiao.session.result.ResultHandler;

import java.sql.*;
import java.util.List;

public class PreparedStatementHandler extends BaseStatementHandler {
	public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
		super(executor, mappedStatement, parameterObject, resultHandler, boundSql);
	}

	@Override
	protected Statement instantiateStatement(Connection connection) throws SQLException {
		String sql = boundSql.getSqlText();
		return connection.prepareStatement(sql);
	}

	@Override
	public void parameterize(Statement statement) throws Exception {
		parameterHandler.setParameters((PreparedStatement) statement);
	}

	@Override
	public void batch(Statement statement) throws Exception {
		//todo
	}

	@Override
	public int update(Statement statement) throws Exception {
		PreparedStatement ps = (PreparedStatement) statement;
		int rows;
		ps.execute();
		rows = statement.getUpdateCount();
		return rows;
	}

	@Override
	public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws Exception {
		PreparedStatement ps = (PreparedStatement) statement;
		ps.execute();
		return resultSetHandler.handleResultSets(ps);
	}
}
