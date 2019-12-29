package com.hetengjiao.executor;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.session.Configuration;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.executor.statement.StatementHandler;
import com.hetengjiao.session.result.ResultHandler;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class SimpleExecutor extends BaseExecutor {

	public SimpleExecutor(Configuration configuration) {
		super(configuration);
	}

	@Override
	protected int doUpdate(MappedStatement ms, Object parameter) throws Exception {
		Statement stmt = null;
		try {
			Configuration configuration = ms.getConfiguration();
			BoundSql boundSql = ms.getBoundSql();
			StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, null, boundSql);
			stmt = prepareStatement(handler);
			return handler.update(stmt);
		} finally {
			closeStatement(stmt);
		}
	}

	@Override
	protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) throws Exception {
		Statement stmt = null;
		try {
			Configuration configuration = ms.getConfiguration();
			StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, resultHandler, boundSql);
			stmt = prepareStatement(handler);
			return handler.query(stmt, resultHandler);
		} catch (Exception e) {
			throw e;
		}
		finally {
			closeStatement(stmt);
		}
	}


	public Statement prepareStatement(StatementHandler handler) throws Exception {
		Statement stmt;
		Connection connection = getConnection();
		stmt = handler.prepare(connection);
		handler.parameterize(stmt);
		return stmt;
	}
}
