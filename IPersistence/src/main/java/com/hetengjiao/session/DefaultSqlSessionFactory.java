package com.hetengjiao.session;

import com.hetengjiao.executor.Executor;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
	private Configuration configuration;

	public DefaultSqlSessionFactory(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public SqlSession openSession() {

		return openSession(configuration.getDefaultExecutorType());
	}

	private SqlSession openSession(ExecutorType execType) {
		try {
			final Executor executor = configuration.newExecutor(execType);
			return new DefaultSqlSession(configuration, executor);
		} catch (Exception e) {
			throw new RuntimeException("Error opening session.  Cause: " + e, e);
		}
	}
}
