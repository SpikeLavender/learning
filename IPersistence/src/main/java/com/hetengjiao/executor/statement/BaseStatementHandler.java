package com.hetengjiao.executor.statement;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.session.Configuration;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.executor.Executor;
import com.hetengjiao.executor.parameter.ParameterHandler;
import com.hetengjiao.executor.resultset.ResultSetHandler;
import com.hetengjiao.session.result.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseStatementHandler implements StatementHandler {

	protected final Configuration configuration;
	protected final ResultSetHandler resultSetHandler;

	protected final ParameterHandler parameterHandler;
	protected final Executor executor;
	protected final MappedStatement mappedStatement;
	protected BoundSql boundSql;

	public BaseStatementHandler(Executor executor, MappedStatement mappedStatement,
	                            Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
		this.configuration = mappedStatement.getConfiguration();

		this.executor = executor;
		this.mappedStatement = mappedStatement;

		if (boundSql != null) {
			//get the key before calculating the statement
		}
		this.boundSql = boundSql;
		this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
		this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql);
	}


	@Override
	public Statement prepare(Connection connection) {
		Statement statement = null;
		try {
			statement = instantiateStatement(connection);
			return statement;
		} catch (Exception e) {
			closeStatement(statement);
			throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
		}
	}

	protected abstract Statement instantiateStatement(Connection connection) throws SQLException;

	@Override
	public BoundSql getBoundSql() {
		return null;
	}

	@Override
	public ParameterHandler getParameterHandler() {
		return null;
	}


	protected void closeStatement(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			//ignore
		}
	}
}
