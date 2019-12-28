package com.hetengjiao.executor;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.session.Configuration;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.session.result.ResultHandler;

import java.sql.SQLException;
import java.util.List;

public class BatchExecutor extends BaseExecutor {
	//todo 批量插入
	public BatchExecutor(Configuration configuration) {
		super(configuration);
	}

	@Override
	protected int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
		return 0;
	}

	@Override
	protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
		return null;
	}

}
