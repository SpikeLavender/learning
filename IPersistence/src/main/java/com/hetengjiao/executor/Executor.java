package com.hetengjiao.executor;

import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.session.result.ResultHandler;

import java.util.List;

public interface Executor {
	ResultHandler NO_RESULT_HANDLER = null;

	//<E> List<E> query(Configuration configuration, MappedStatement statement, Object... params) throws Exception;

	<E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler) throws Exception;

	int update(MappedStatement ms, Object parameter) throws Exception;
}
