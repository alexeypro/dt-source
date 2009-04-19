package com.objecty.dtsource.service.impl;

import java.util.List;

import com.objecty.dtsource.dao.EntityDAO;
import com.objecty.dtsource.service.EntityManager;

/**
 * 
 * Implementation of Entity manager, which access specific DAO, injected with Spring config.
 * Actually, you can think about this implementation and it's interface like of some 
 * stand-alone simple Business Delegate.
 * 
 * @author olexiy
 *
 */
public class EntityManagerImpl implements EntityManager {
	private EntityDAO dao;

	public void setEntityDAO(EntityDAO dao) {
		this.dao = dao;
	}

	public List listEntities(String query, Long startWith, Long perPage) {
		return dao.listEntities(query, startWith, perPage);
	}
	
	public Long sizeOfListEntities(String query) {
		return dao.sizeOfListEntities(query);
	}
	
	public Long updateEntities(String query) {
		return dao.updateEntities(query);
	}

}
