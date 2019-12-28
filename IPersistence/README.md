    
	 JDBC问题分析：
	    1. 数据库配置信息存在硬编码问题
	    2. 频繁创建释放数据库连接
	 
	     解决：1、配置文件
	          2、连接池
	 
	     1. SQL语句、设置参数、获取结果集参数均存在硬编码问题
	 
	     解决：配置文件
	
	     1. 手动封装返回结果集，较为繁琐
	 
	     解决：反射、内省
	 
	public static void main(String[] args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		ResultSet resultSet = null;
		User user = new User();

		try {
			// 1
			// 加载数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 通过驱动管理类获取数据库链接
			connection = DriverManager.getConnection("jdbc:mysql://localhost", "root", "root");
			// 定义sql语句， ? 表示占位符
			String sql = "select * from user where username = ?";
			// 获取预处理statement
			preparedStatement = connection.prepareStatement(sql);
			// 设置参数，第一个参数为sql语句中参数的序号（从1开始），第二个参数为设置的参数值
			preparedStatement.setString(1, "tom");
			// 向数据库发出sql执行查询，查询出结果集
			resultSet = preparedStatement.executeQuery();
			// 遍历查询结果集
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String username = resultSet.getString("username");
				//封装user
				user.setId(id);
				user.setUsername(username);
			}

			System.out.println(user);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	 自定义持久层框架设计思路：
	 使用端：（项目）：引入自定义持久层框架的jar包
	       提供两部分配置信息：数据库配置信息、sql配置信息：sql语句、参数类型、返回值类型
	       使用配置文件来提供这两部分配置信息：
	           （1）sqlMapConfig.xml：存放数据库配置信息，存放mapper.xml的全路径
	           （2）mapper.xml：存放sql配置信息
	 
	 
	 自定义持久层框架本身：（工程）：本质就是对JDBC代码进行了封装
	    （1）加载配置文件：根据配置文件的路径，加载配置文件成字节输入流，存储在内存中
	           创建Resources类    方法：InputStream getResourceAsStream(String path)
	    （2）创建两个javaBean：（容器对象）：存放的就是对配置文件解析出来的内容
	           Configuration：核心配置类：存放sqlMapConfig.xml解析出来的内容
	           MappedStatement：映射配置类：存放mapper.xml解析出来的内容
	    （3）解析配置文件：dom4j
	           创建类：SqlSessionFactoryBuilder 方法：build(InputStream in)
	           第一：使用dom4j解析配置文件，将解析出来的内容封装到容器对象中
	           第二：创建SqlSessionFactory对象；生产sqlSession：会话对象（工厂模式）
	
	    （4）创建sqlSessionFactory接口及实现类DefaultSqlSessionFactory
	           第一：openSession()：生产sqlSession
	 
	    （5）创建SqlSession接口及实现类DefaultSession
	           定义对数据库的crud操作：  selectList()
	                                   selectOne()
	                                   update()
	                                   delete()
	    （6）创建Executor接口及实现类SimpleExecutor实现类
	           query(Configuration, MappedStatement, Object... params)：执行的就是JDBC代码
