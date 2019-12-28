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

		//		// 1.注册驱动，获取连接
//		Configuration configuration = ms.getConfiguration();
//		Connection connection = configuration.getDataSource().getConnection();
//
//		//2.获取sql语句: select * from user where id = #{id} and username = #{username}
//
//
//		//3.获取预处理对象：preparedStatement
//		PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
//		//4.设置参数
//		//5.执行sql
//		//6.结果集处理
//
//		String parameterType = ms.getParameterType();
//		Class<?> parameterTypeClass = getClassType(parameterType);
//		List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
//		for (int i = 0; i < parameterMappingList.size(); i++) {
//			ParameterMapping parameterMapping = parameterMappingList.get(i);
//			String content = parameterMapping.getContent();
//
//			// 反射
//			Field declaredField = parameterTypeClass.getDeclaredField(content);
//			// 暴力访问
//			declaredField.setAccessible(true);
//			Object o = declaredField.get(parameter);
//			preparedStatement.setObject(i + 1, o);
//
//		}
//		// 5.执行sql
//		ResultSet resultSet = preparedStatement.executeQuery();
//
//		String resultType = ms.getResultType();
//		Class<?> resultTypeClass = getClassType(resultType);
//
//		ArrayList<Object> objects = new ArrayList<>();
//
//		// 6.封装返回结果集
//		while (resultSet.next()) {
//			// 元数据
//			Object o = resultTypeClass.newInstance();
//			ResultSetMetaData metaData = resultSet.getMetaData();
//			for (int i = 1; i <= metaData.getColumnCount(); i++) {
//				// 字段名
//				String columnName = metaData.getColumnName(i);
//				// 字段的值
//				Object value = resultSet.getObject(columnName);
//
//				// 使用反射或者内省，根据数据库表和实体的对应关系，完成封装
//				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
//				Method writeMethod = propertyDescriptor.getWriteMethod();
//				writeMethod.invoke(o, value);
//			}
//			objects.add(o);
//		}
//
//		return (List<E>) objects;
	}

	private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
		if (parameterType != null) {
			Class<?> aClass = Class.forName(parameterType);
			return aClass;
		}
		return null;
	}


	public Statement prepareStatement(StatementHandler handler) throws Exception {
		Statement stmt;
		Connection connection = getConnection();
		stmt = handler.prepare(connection);
		handler.parameterize(stmt);
		return stmt;
	}
}
