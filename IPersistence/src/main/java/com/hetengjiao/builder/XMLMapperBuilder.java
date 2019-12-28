package com.hetengjiao.builder;

import com.hetengjiao.mapping.BoundSql;
import com.hetengjiao.mapping.SqlCommandType;
import com.hetengjiao.session.Configuration;
import com.hetengjiao.mapping.MappedStatement;
import com.hetengjiao.mapping.StatementType;
import com.hetengjiao.utils.GenericTokenParser;
import com.hetengjiao.mapping.ParameterMapping;
import com.hetengjiao.utils.ParameterMappingTokenHandler;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

	private Configuration configuration;

	public XMLMapperBuilder(Configuration configuration) {
		this.configuration = configuration;
	}

	public void parse(InputStream inputStream) throws DocumentException, ClassNotFoundException {
		Document document = new SAXReader().read(inputStream);
		Element rootElement = document.getRootElement();
		String namespace = rootElement.attributeValue("namespace");

		Class<?> boundType;
		boundType = Class.forName(namespace);
		if (!configuration.hasMapper(boundType)) {
			configuration.addMapper(boundType);
		}
		parseOperation(rootElement, namespace, SqlCommandType.SELECT);
		parseOperation(rootElement, namespace, SqlCommandType.UPDATE);
		parseOperation(rootElement, namespace, SqlCommandType.INSERT);
		parseOperation(rootElement, namespace, SqlCommandType.DELETE);
		//bindMapperForNamespace();

	}

	@SuppressWarnings("unchecked")
	private void parseOperation(Element rootElement, String namespace, SqlCommandType commandType) throws ClassNotFoundException {

		List<Element> elements = rootElement.selectNodes("//" + commandType.name().toLowerCase());
		for (Element element : elements) {
			String id = element.attributeValue("id");
			String resultType = element.attributeValue("resultType");
			String parameterType = element.attributeValue("parameterType");
			String sqlText = element.getTextTrim();

			Class<?> resultTypeClass = getClassType(resultType);
			Class<?> parameterTypeClass = getClassType(parameterType);
			MappedStatement mappedStatement = new MappedStatement();
			mappedStatement.setId(id);
			mappedStatement.setResultType(resultTypeClass);
			mappedStatement.setParameterType(parameterTypeClass);
			mappedStatement.setSql(sqlText);
			mappedStatement.setSqlCommandType(commandType);
			mappedStatement.setConfiguration(configuration);
			mappedStatement.setBoundSql(getBoundSql(sqlText));
			mappedStatement.setStatementType(StatementType.PREPARED);
			String key = namespace + "." + id;
			configuration.getMappedStatementMap().put(key, mappedStatement);
		}
	}

	/**
	 * 完成对#{}的解析工作
	 * 1. 将#{}使用? 进行代替
	 * 2. 解析出#{}里面的值进行存储
	 * @param sql sql
	 * @return res
	 */
	private BoundSql getBoundSql(String sql) {
		//标记处理类：配置标记解析器完成对占位符的解析处理工作
		ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
		GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", tokenHandler);
		// 解析出来的sql
		String parseSql = genericTokenParser.parse(sql);
		// #{}里面解析出来的参数名称
		List<ParameterMapping> parameterMappings = tokenHandler.getParameterMappings();
		BoundSql boundSql = new BoundSql(parseSql, parameterMappings);

		return boundSql;
	}

	private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
		if (parameterType != null) {
			Class<?> aClass = Class.forName(parameterType);
			return aClass;
		}
		return null;
	}

//	private void bindMapperForNamespace() {
//		String namespace = builderAssistant.getCurrentNamespace();
//		if (namespace != null) {
//			Class<?> boundType = null;
//			try {
//				boundType = Resources.classForName(namespace);
//			} catch (ClassNotFoundException e) {
//				//ignore, bound type is not required
//			}
//			if (boundType != null) {
//				if (!configuration.hasMapper(boundType)) {
//					// Spring may not know the real resource name so we set a flag
//					// to prevent loading again this resource from the mapper interface
//					// look at MapperAnnotationBuilder#loadXmlResource
//					configuration.addLoadedResource("namespace:" + namespace);
//					configuration.addMapper(boundType);
//				}
//			}
//		}
//	}
}
