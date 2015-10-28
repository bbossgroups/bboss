package com.frameworkset.common.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.cfg.Environment;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.sql.PoolManDataSource;
import com.frameworkset.common.poolman.sql.PrimaryKeyCacheManager;
import com.frameworkset.util.NoSupportTypeCastException;
import com.frameworkset.util.ValueObjectUtil;

/**
 * <p>Title: KeyGenerator</p>
 *
 * <p>Description: 主键生成器</p>
 *
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-6-1 下午08:58:51
 * @author biaoping.yin
 * @version 1.0
 */
public class KeyGenerator 
//    implements IdentifierGenerator, Configurable,net.sf.hibernate.id.IdentifierGenerator,net.sf.hibernate.id.Configurable {
implements Serializable,net.sf.hibernate.id.IdentifierGenerator,net.sf.hibernate.id.Configurable {
    private static Configuration config = null;
    private static Logger log = Logger.getLogger(KeyGenerator.class);
    private String tableName;
	private String poolName;
	private String typeClass;
	private net.sf.hibernate.type.Type type;

    DataSource ds;
    public KeyGenerator() {

    }
    
    public static void init(Configuration configs)
    {
        if(config == null)
            config = configs;
    }

//    public void configure(Type type, Properties properties, Dialect dialect) throws MappingException {
//		tableName = properties.getProperty("tableName");
//		if(tableName == null)
//		{
//			String msg = "tableName was not specified by property tableName" ;
//			log.fatal(msg);
//			throw new HibernateException(msg);
//		}
//		poolName = properties.getProperty("poolName");
//		String jndiName = Environment.getProperties().getProperty("connection.datasource");
//		if (jndiName==null) {
//			String msg = "datasource JNDI name was not specified by property " + Environment.DATASOURCE;
//			log.fatal(msg);
//			return;
//			//throw new HibernateException(msg);
//		}
//
//		String user = Environment.getProperties().getProperty(Environment.USER);
//		String pass = Environment.getProperties().getProperty(Environment.PASS);
//
//		try {
//			ds =  (DataSource)NamingHelper.getInitialContext(Environment.getProperties()).lookup("connection.datasource");
//			
//		}
//		catch (Exception e) {
//			log.fatal( "Could not find datasource: " + jndiName, e );
//			return;
//			//throw new HibernateException( "Could not find datasource", e );
//		}
//		//if (ds==null) throw new HibernateException( "Could not find datasource: " + jndiName );
//		if (ds==null)
//		{
//			log.info("Could not find datasource: " + jndiName);
//            return;
//        }
//		log.info( "Using datasource: " + jndiName );
//
//    }
//
//    public Serializable generate(SessionImplementor sessionImplementor, Object object) throws HibernateException {
//
//            if (ds != null && ds instanceof PoolManDataSource)
//                try{
//                    return ((PoolManDataSource) ds).getPrimaryKey(tableName);
//                }
//
//                catch (SQLException ex) {
//                    log.error("generate primarykey for table["
//                              + ((PoolManDataSource) ds)
//                              + ":"
//                              + tableName
//                              + "] error:"
//                              + ex.getMessage());
//                    throw new HibernateException("generate primarykey for table["
//                                                 + ((PoolManDataSource) ds).getJNDIName()
//                                                 + ":"
//                                                 + tableName
//                                                 + "] error:"
//                                                 + ex.getMessage()
//                        );
//                }
//			else
//			{
//				if(poolName == null)
//                {
//                    String msg = "poolName was not specified by property poolName,please check your hibernate mapping file";
//                    log.fatal(msg);
//                    throw new HibernateException(msg);
//                }
//                try
//                {
//                    return getPrimaryKey(poolName, tableName);
//                }
//                catch(SQLException e)
//                {
//                    String msg = "getPrimaryKey for poolName[" + poolName + "] tableName [" + tableName + "] error:" + e.getMessage();
//                    log.fatal(msg);
//                    throw new HibernateException(msg);
//
//                }
//            }
//
//
//    }

	public Serializable getPrimaryKey(String poolName,String tableName,Connection con) throws SQLException
	{
//		Long key = new Long(PrimaryKeyCacheManager.getInstance()
//							 .getPrimaryKeyCache(poolName)
//							 .getIDTable(tableName.toLowerCase()).generateKey());
		return PrimaryKeyCacheManager.getInstance()
	      .getPrimaryKeyCache(poolName)
	      .getIDTable(tableName.toLowerCase()).generateObjectKey(con).getPrimaryKey();
	}

    /* (non-Javadoc)
     * @see net.sf.hibernate.id.IdentifierGenerator#generate(net.sf.hibernate.engine.SessionImplementor, java.lang.Object)
     */
    public Serializable generate(net.sf.hibernate.engine.SessionImplementor arg0, Object arg1) throws SQLException, net.sf.hibernate.HibernateException {
        if (ds != null && ds instanceof PoolManDataSource)
            try{
            	
                Serializable primaryKey = ((PoolManDataSource) ds).getPrimaryKey(tableName,arg0.connection());
                if(primaryKey.getClass().getName().equals(this.typeClass))
                    return primaryKey;
                else
                {                    
                    return (Serializable)ValueObjectUtil.basicTypeCast(primaryKey,primaryKey.getClass(),type.getReturnedClass());
                }
            }

            catch (SQLException ex) {
                log.error("generate primarykey for table["
                          + ((PoolManDataSource) ds)
                          + ":"
                          + tableName
                          + "] error:"
                          + ex.getMessage());
                throw new HibernateException("generate primarykey for table["
                                             + ((PoolManDataSource) ds).getJNDIName()
                                             + ":"
                                             + tableName
                                             + "] error:"
                                             + ex.getMessage()
                    );
            } catch (NumberFormatException e) {
               
                throw new HibernateException("generate primarykey for table["
                        + ((PoolManDataSource) ds).getJNDIName()
                        + ":"
                        + tableName
                        + "] error:"
                        + e.getMessage());
            } catch (NoSupportTypeCastException e) {
                throw new HibernateException("generate primarykey for table["
                        + ((PoolManDataSource) ds).getJNDIName()
                        + ":"
                        + tableName
                        + "] error:"
                        + e.getMessage());
            }
		else
		{
			if(poolName == null)
            {
                String msg = "poolName was not specified by property poolName,please check your hibernate mapping file";
                log.fatal(msg);
                throw new HibernateException(msg);
            }
            try
            {
                Serializable primaryKey = getPrimaryKey(poolName, tableName,arg0.connection());
                if(primaryKey.getClass().getName().equals(this.typeClass))
                    return primaryKey;
                else
                {                    
                    return (Serializable)ValueObjectUtil.basicTypeCast(primaryKey,primaryKey.getClass(),type.getReturnedClass());
                }
            }
            catch(SQLException e)
            {
                String msg = "getPrimaryKey for poolName[" + poolName + "] tableName [" + tableName + "] error:" + e.getMessage();
                log.fatal(msg);
                throw new HibernateException(msg);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                String msg = "getPrimaryKey for poolName[" + poolName + "] tableName [" + tableName + "] error:" + e.getMessage();
                log.fatal(msg);
                throw new HibernateException(msg);
            } catch (NoSupportTypeCastException e) {
                String msg = "getPrimaryKey for poolName[" + poolName + "] tableName [" + tableName + "] error:" + e.getMessage();
                log.fatal(msg);
                throw new HibernateException(msg);
            }
        }
    }

    /* (non-Javadoc)
     * @see net.sf.hibernate.id.Configurable#configure(net.sf.hibernate.type.Type, java.util.Properties, net.sf.hibernate.dialect.Dialect)
     */
    public void configure(net.sf.hibernate.type.Type type, Properties properties, net.sf.hibernate.dialect.Dialect arg2) throws net.sf.hibernate.MappingException {
        tableName = properties.getProperty("tableName");  
        this.type = type;
        
        typeClass = type.getReturnedClass().getName();
        
        
        
        
		if(tableName == null)
		{
			String msg = "tableName was not specified by property tableName" ;
			log.fatal(msg);
			throw new MappingException(msg);
		}
		poolName = properties.getProperty("poolName");
//		Set keys = config.getProperties().keySet();
//		Iterator iter = keys.iterator();
//		while(iter.hasNext())
//		{
//		    String ks = iter.next().toString();
//		    System.out.println(ks + "=" + config.getProperty(ks));
//		}
		
		
		String jndiName = config.getProperty(Environment.DATASOURCE);
		if (jndiName==null) {
			String msg = "datasource JNDI name was not specified by property " + Environment.DATASOURCE;
			log.fatal(msg);
			return;
			//throw new HibernateException(msg);
		}

		String user = config.getProperty(Environment.USER);
		String pass = config.getProperty(Environment.PASS);

		try {
		    Context ctx = new InitialContext();
			ds =  (DataSource)ctx.lookup(jndiName);
		}
		catch (Exception e) {
			log.fatal( "Could not find datasource: " + jndiName, e );
			return;
			//throw new HibernateException( "Could not find datasource", e );
		}
		//if (ds==null) throw new HibernateException( "Could not find datasource: " + jndiName );
		if (ds==null)
		{
			log.info("Could not find datasource: " + jndiName);
            return;
        }
		log.info(tableName + " Using datasource: " + jndiName + " to get primary_key");
        
    }

}
