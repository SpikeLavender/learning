package com.hetengjiao.session;

import com.hetengjiao.mapping.SqlCommandType;
import com.hetengjiao.mapping.MappedStatement;

import java.lang.reflect.*;

public class MapperMethod {

	private final SqlCommand command;
	private final MethodSignature method;

	public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
		this.command = new SqlCommand(config, mapperInterface, method);
		this.method = new MethodSignature(config, mapperInterface, method);
	}

	public Object execute(SqlSession sqlSession, Object[] args) {
		Object result;
		switch (command.getType()){
			case INSERT:
				result = rowCountResult(sqlSession.insert(command.getName(), args));
				break;
			case UPDATE:
				result = rowCountResult(sqlSession.update(command.getName(), args));
				break;
			case DELETE:
				result = rowCountResult(sqlSession.delete(command.getName(), args));
				break;
			case SELECT:
				if (method.getType() instanceof ParameterizedType) {
					result = sqlSession.selectList(command.getName(), args);
				} else {
					result = sqlSession.selectOne(command.getName(), args);
				}
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + command.getType());
		}
		return result;
	}



	private Object rowCountResult(int rowCount) {
		final Object result;
		if (method.returnsVoid()) {
			result = null;
		} else if (Integer.class.equals(method.getReturnType()) || Integer.TYPE.equals(method.getReturnType())) {
			result = rowCount;
		} else if (Long.class.equals(method.getReturnType()) || Long.TYPE.equals(method.getReturnType())) {
			result = (long)rowCount;
		} else if (Boolean.class.equals(method.getReturnType()) || Boolean.TYPE.equals(method.getReturnType())) {
			result = rowCount > 0;
		} else {
			throw new RuntimeException("Mapper method '" + command.getName() + "' has an unsupported return type: " + method.getReturnType());
		}
		return result;
	}

	public static class SqlCommand {

		private final SqlCommandType type;
		private final String name;

		public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
			final String methodName = method.getName();
			final Class<?> declaringClass = method.getDeclaringClass();

			MappedStatement ms = resolveMappedStatement(mapperInterface, methodName, declaringClass, configuration);

			if (ms != null) {
				type = ms.getSqlCommandType();
				name = mapperInterface.getName() + "." + methodName;
			} else {
				type = SqlCommandType.UNKNOWN;
				name = null;
			}

		}


		public SqlCommandType getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		private MappedStatement resolveMappedStatement(Class<?> mapperInterface, String methodName,
		                                               Class<?> declaringClass, Configuration configuration) {
			String statementId = mapperInterface.getName() + "." + methodName;
			if (configuration.hasStatement(statementId)) {
				return configuration.getMappedStatement(statementId);
			} else if (mapperInterface.equals(declaringClass)) {
				return null;
			}
			for (Class<?> superInterface : mapperInterface.getInterfaces()) {
				if (declaringClass.isAssignableFrom(superInterface)) {
					MappedStatement ms = resolveMappedStatement(superInterface, methodName,
							declaringClass, configuration);
					if (ms != null) {
						return ms;
					}
				}
			}
			return null;
		}
	}

	public static class MethodSignature {

		private final boolean returnsVoid;
		private final Class<?> returnType;
		private final Type type;


		public MethodSignature(Configuration configuration, Class<?> mapperInterface, Method method) {
			//Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, mapperInterface);
			//todo
			type = method.getGenericReturnType();
			//Class<?> declaringClass = method.getDeclaringClass();

			if (type instanceof Class<?>) {
				this.returnType = (Class<?>) type;
			} else if (type instanceof ParameterizedType) {
				this.returnType = (Class<?>) ((ParameterizedType) type).getRawType();
			} else {
				this.returnType = method.getReturnType();
			}
			this.returnsVoid = void.class.equals(this.returnType);
		}


		public Class<?> getReturnType() {
			return returnType;
		}

		public boolean returnsVoid() {
			return returnsVoid;
		}

		public Type getType() {
			return type;
		}
	}
}


