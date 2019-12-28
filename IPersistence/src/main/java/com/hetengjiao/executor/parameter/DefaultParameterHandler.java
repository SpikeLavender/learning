package com.hetengjiao.executor.parameter;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.session.Configuration;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.mapping.ParameterMapping;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.List;

public class DefaultParameterHandler implements ParameterHandler {

	private final MappedStatement mappedStatement;
	private final  Object parameterObject;
	private final BoundSql boundSql;
	private final Configuration configuration;//todo：多参数使封装参数列表以及类型转换时会调用，暂时预留


	public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		this.mappedStatement = mappedStatement;
		this.configuration = mappedStatement.getConfiguration();
		this.boundSql = boundSql;
		this.parameterObject = parameterObject;
	}

	@Override
	public Object getParameterObject() {
		return parameterObject;
	}

	//todo：单参数，完善多参数的情况
	@Override
	public void setParameters(PreparedStatement ps) throws Exception {
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappingList();
		Class<?> parameterTypeClass = mappedStatement.getParameterType();
		if (parameterMappings != null) {
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				String content = parameterMapping.getContent();
				// 反射
				Field declaredField = parameterTypeClass.getDeclaredField(content);
				// 暴力访问
				declaredField.setAccessible(true);
				Object parameterObject = this.getParameterObject();

				Object o = declaredField.get(Array.get(parameterObject, 0));
				ps.setObject(i + 1, o);
			}
		}
	}
}
