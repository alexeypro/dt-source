package com.objecty.dtsource.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.objecty.dtsource.dao.EntityDAO;

/**
 * 
 * Implementation of Entity Data Access Object using HibernateTemplate.
 * 
 * @author olexiy
 *
 */
public class EntityDAOHibernateImpl extends HibernateDaoSupport implements EntityDAO {

    private static final Logger log = Logger.getLogger(EntityDAOHibernateImpl.class);

    public List listEntities(final String query, final Long startWith, final Long perPage) {
        log.debug("Called listEntities(\"" + query + "\", " + startWith + ", " + perPage +")");
        List all = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session sess) throws HibernateException {
                Query q = sess.createQuery(query);
                log.debug("Inside nested method: q is \"" + q.getQueryString() + "\"");
                // TODO: stupid workaround here, need to be fixed!
                // Explanation is the following
                // These methods accept only Integer
                // But what if we have more than "Integer" number of records?
                // And we want to fetch from more than "Integer" number of records till the end?
                // That's not going to work with Hibernate at the moment.
                int fetchSize = (perPage == null) ? 0 : Integer.valueOf(perPage.toString());
                log.debug("Inside nested method: fetchSize is " + fetchSize);
                int maxResults = (perPage == null) ? 0 : Integer.valueOf(perPage.toString());
                log.debug("Inside nested method: maxResults is " + maxResults);
                int firstResult = (startWith == null) ? 0 : Integer.valueOf(startWith.toString());
                log.debug("Inside nested method: firstResult is " + firstResult);
                q.setFetchSize(fetchSize);
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
                List retL = q.list();
                log.debug("Inside nested method: retL is " + ((retL == null) ? "null" : "not null"));
                if (retL == null)   retL = new ArrayList();
                log.debug("Inside nested method: retL has " + retL.size() + " element(s)");
                return retL;
            }
        });
        return (all);
    }

    public Long sizeOfListEntities(final String query) {
        log.debug("Called sizeOfListEntities(\"" + query + "\")");
        class ReturnValue  {
                Long value;
        }
        final ReturnValue rv = new ReturnValue();
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session sess) throws HibernateException {
                Query q = sess.createQuery(query);
                log.debug("Inside nested method: q is \"" + q.getQueryString() + "\"");
                //was: rv.value = ((Integer)q.iterate().next());
                Object retO = q.iterate().next();
                log.debug("Inside nested method: retO is " + ((retO == null) ? "null" : ("not null, = " + retO.toString())));
                Long retL = (retO == null) ? (long)0 : Long.valueOf(retO.toString());
                log.debug("Inside nested method: Returning Long is " + retL);
                rv.value = retL;
                return null;
            }
        });
        log.debug("Returning Long is " + rv.value);
        return rv.value;
    }
    
    public Long updateEntities(final String query) {
    	log.debug("Called updateEntities(\"" + query + "\"");
        class ReturnValue  {
            Long value;
        }
        final ReturnValue rv = new ReturnValue();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess) throws HibernateException {
				Query q = sess.createQuery(query);
				Integer retI = q.executeUpdate();
				log.debug("Inside nested method: retI is " + ((retI == null) ? "null" : ("not null, = " + retI.toString())));
				Long retL = (retI == null) ? (long)0 : Long.valueOf(retI.toString());
				log.debug("Inside nested method: Returning Long is " + retL);
				rv.value = retL;
				return null;
			}
		});
		log.debug("Returning Long is " + rv.value);
		return (rv.value);
    }

}
