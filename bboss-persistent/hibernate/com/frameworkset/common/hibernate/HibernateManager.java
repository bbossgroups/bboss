package com.frameworkset.common.hibernate;

/**
 * <p>Title: HibernateManager</p>
 *
 * <p>Description: </p>
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

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.frameworkset.common..Manager;

public final class HibernateManager implements Serializable
{
	private static Logger log = Logger.getLogger(HibernateManager.class);
    public HibernateManager()
    {

    }

    public static SessionFactory getSessionFactory()
        throws HibernateException
    {
        if(sessionFactory == null)
        {
			log.debug("sessionFactory == null,Get session factory with name[" + DEFAULT_SESSION_FACTORY_BEAN_NAME + "]");
            if(Manager.getApplicationContext() == null || Manager.getApplicationContext().getBean(DEFAULT_SESSION_FACTORY_BEAN_NAME) == null)
            {
				log.debug("Manager.getApplicationContext() == null,Get session factory from hibernate directly");
                sessionFactory = (new Configuration()).configure().buildSessionFactory();
                return sessionFactory;
            }
			log.debug("Get session factory through Manager");
            sessionFactory = (SessionFactory)Manager.getApplicationContext().getBean(DEFAULT_SESSION_FACTORY_BEAN_NAME);
			log.debug("Get session factory success!");

        }

        return sessionFactory;
    }

    public static Session openSession()
        throws HibernateException
    {
        return getSessionFactory().openSession();
    }

    public static void closeSession(Session session)
    {
        if(session != null)
            try
            {
                session.close();
            }
            catch(HibernateException e)
            {
                e.printStackTrace();
            }
    }

    public static void rollbackTransaction(Transaction transaction)
    {
        if(transaction != null)
            try
            {
                transaction.rollback();
            }
            catch(HibernateException e)
            {
                e.printStackTrace();
            }
    }

    private static SessionFactory sessionFactory;
    public static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";
}
