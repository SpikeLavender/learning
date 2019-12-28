package com.hetengjiao.executor.resultset;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.session.Configuration;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.executor.Executor;
import com.hetengjiao.executor.parameter.ParameterHandler;
import com.hetengjiao.session.result.ResultHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetHandler implements ResultSetHandler {

	private final Executor executor;
	private final Configuration configuration;
	private final MappedStatement mappedStatement;
	private final ParameterHandler parameterHandler;
	private final ResultHandler<?> resultHandler;
	private final BoundSql boundSql;

	public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement,
	                               ParameterHandler parameterHandler, ResultHandler resultHandler, BoundSql boundSql) {
		this.mappedStatement = mappedStatement;
		this.executor = executor;
		this.configuration = mappedStatement.getConfiguration();
		this.parameterHandler = parameterHandler;
		this.resultHandler = resultHandler;
		this.boundSql = boundSql;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E> List<E> handleResultSets(Statement statement) throws Exception {

		ResultSet resultSet = getFirstResultSet(statement);
		Class<?> resultTypeClass = mappedStatement.getResultType();
		//Class<?> resultTypeClass = getClassType(resultType);

		ArrayList<Object> objects = new ArrayList<>();

		while (resultSet.next()) {
			// 元数据
			Object o = resultTypeClass.newInstance();
			ResultSetMetaData metaData = resultSet.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				// 字段名
				String columnName = metaData.getColumnName(i);
				// 字段的值
				Object value = resultSet.getObject(columnName);

				// 使用反射或者内省，根据数据库表和实体的对应关系，完成封装
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
				Method writeMethod = propertyDescriptor.getWriteMethod();
				writeMethod.invoke(o, value);
			}
			objects.add(o);
		}

		return (List<E>) objects;
	}


	private ResultSet getFirstResultSet(Statement stmt) throws SQLException {
		ResultSet rs = stmt.getResultSet();
		while (rs == null) {
			// move forward to get the first resultset in case the driver
			// doesn't return the resultset as the first result (HSQLDB 2.1)
			if (stmt.getMoreResults()) {
				rs = stmt.getResultSet();
			} else {
				if (stmt.getUpdateCount() == -1) {
					// no more results. Must be no resultset
					break;
				}
			}
		}
		return rs;
	}
}
