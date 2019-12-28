package com.hetengjiao.executor.statement;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.executor.parameter.ParameterHandler;
import com.hetengjiao.session.result.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {
	Statement prepare(Connection connection) throws SQLException;

	void parameterize(Statement statement)
			throws Exception;

	void batch(Statement statement)
			throws Exception;

	int update(Statement statement)
			throws Exception;

	<E> List<E> query(Statement statement, ResultHandler resultHandler)
			throws Exception;


	BoundSql getBoundSql();

	ParameterHandler getParameterHandler();
}
