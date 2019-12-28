package com.hetengjiao.executor.resultset;

import java.sql.Statement;
import java.util.List;

public interface ResultSetHandler {
	<E> List<E> handleResultSets(Statement statement) throws Exception;

}
