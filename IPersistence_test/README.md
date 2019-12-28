    自定义持久层框架问题分析：
    1. Dao层使用自定义持久层框架，存在代码重复，整个操作过程的模板重复（加载配置文件、创建sqlSessionFactory、生产sqlSession）
    2. statementId存在硬编码问题
    
    解决思路：使用代理模式生成Dao层接口的代理实现类
    
    IUserDao userDao = sqlSession.getMapper(IUserDao.class);
    
    List<User> all = userDao.findAll(); 代理对象调用接口中的任意方法，都会执行invoke方法
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<?> mapClass) {
        //使用JDK动态代理来为Dao接口生产代理对象，并返回
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapClass}, new InvocationHandler() {
        	/**
        	 * proxy: 当前代理对象的引用
        	 * method: 当前被调用方法的引用 
        	 * args: 传递的参数	      
        	 */
        	@Override
        	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        		return null;
        	}
        });
        		
        return (T) proxyInstance;
    }
    
   	