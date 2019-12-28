package com.hetengjiao.builder;

import com.hetengjiao.session.Configuration;
import com.hetengjiao.io.Resources;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

	private Configuration configuration;

	public XMLConfigBuilder() {
		this.configuration = new Configuration();
	}

	/**
	 * 该方法使用dom4j将配置文件进行解析，封装Configuration
	 * @param inputStream in
	 * @return config
	 */
	public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException, ClassNotFoundException {
		Document document = new SAXReader().read(inputStream);
		Element rootElement = document.getRootElement();
		List<Element> list = rootElement.selectNodes("//property");
		Properties properties = new Properties();
		for (Element element : list) {
			String name = element.attributeValue("name");
			String value = element.attributeValue("value");
			properties.setProperty(name, value);
		}

		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
		comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
		comboPooledDataSource.setUser(properties.getProperty("username"));
		comboPooledDataSource.setPassword(properties.getProperty("password"));

		configuration.setDataSource(comboPooledDataSource);

		// mapper.xml解析：拿到路径--字节输入流--dom4j解析
		List<Element> mappers = rootElement.selectNodes("//mapper");
		for (Element mapper : mappers) {
			String mapperPath = mapper.attributeValue("resource");
			InputStream resourceAsStream = Resources.getResourceAsStream(mapperPath);
			XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
			xmlMapperBuilder.parse(resourceAsStream);
		}
		return configuration;

	}
}
