package com.objecty.dtsource.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.objecty.dtsource.dao.EntityDAO;

/**
 * 
 * Implementation of Entity Data Access Object using JdbcTemplate.
 * 
 * @author olexiy
 *
 */
public class EntityDAOJdbcImpl extends JdbcDaoSupport implements EntityDAO {

    private static final Logger log = Logger.getLogger(EntityDAOJdbcImpl.class);

    public List listEntities(final String query, final Long startWith, final Long perPage) {
        log.debug("Called listEntities(\"" + query + "\", " + startWith + ", " + perPage +")");
        String limitSubquery = " LIMIT " + startWith + ", " + perPage;
        log.debug("limitSubquery is \"" + limitSubquery + "\"");
        List retL = getJdbcTemplate().queryForList(query + limitSubquery);
        log.debug("retL is " + ((retL == null) ? "null" : "not null"));
        if (retL == null)   retL = new ArrayList();
        log.debug("retL has " + retL.size() + " element(s)");
        return retL;
    }

    public Long sizeOfListEntities(final String query) {
        // was: return getJdbcTemplate().queryForInt(query);
        log.debug("Called sizeOfListEntities(\"" + query + "\")");
        Object retO = getJdbcTemplate().queryForObject(query, String.class);
        log.debug("retO is " + ((retO == null) ? "null" : ("not null, = " + retO.toString())));
        Long retL = (retO == null) ? (long)0 : Long.valueOf(retO.toString());
        log.debug("Returning Long is " + retL);
        return retL;
    }
    
    public Long updateEntities(final String query) {
    	log.debug("Called updateEntities(\"" + query + "\"");
		Integer retI = getJdbcTemplate().update(query);
		log.debug("retI is " + ((retI == null) ? "null" : ("not null, = " + retI.toString())));
		Long retL = (retI == null) ? (long)0 : Long.valueOf(retI.toString());
		log.debug("Returning Long is " + retL);
		return retL;
    }


}
