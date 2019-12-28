package com.hetengjiao.executor.statement;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.executor.Executor;
import com.hetengjiao.executor.parameter.ParameterHandler;
import com.hetengjiao.session.result.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RoutingStatementHandler implements StatementHandler {

	private final StatementHandler delegate;

	public RoutingStatementHandler(Executor executor, MappedStatement ms, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
		switch (ms.getStatementType()) {
			case PREPARED:
				delegate = new PreparedStatementHandler(executor, ms, parameterObject, resultHandler, boundSql);
				break;
			case STATEMENT:
				delegate = new SimpleStatementHandler(executor, ms, parameterObject, resultHandler, boundSql);
				break;
			default:
				throw new RuntimeException("Unknown statement type: " + ms.getStatementType());
		}
	}

	@Override
	public Statement prepare(Connection connection) throws SQLException {
		return delegate.prepare(connection);
	}

	@Override
	public void parameterize(Statement statement) throws Exception {
		delegate.parameterize(statement);
	}

	@Override
	public void batch(Statement statement) throws Exception {
		delegate.batch(statement);
	}

	@Override
	public int update(Statement statement) throws Exception {
		return delegate.update(statement);
	}

	@Override
	public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws Exception {
		return delegate.query(statement, resultHandler);
	}

	@Override
	public BoundSql getBoundSql() {
		return delegate.getBoundSql();
	}

	@Override
	public ParameterHandler getParameterHandler() {
		return delegate.getParameterHandler();
	}
}
