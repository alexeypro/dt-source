package com.objecty.dtsource.dao;

import java.util.List;

/**
 * 
 * Interface for Entity DAO - this is how we get a partial list and complete size of
 * all records/entities.
 * 
 * @author olexiy
 *
 */
public interface EntityDAO extends DAO {

	public List listEntities(String query, Long startFrom, Long perPage);
	public Long sizeOfListEntities(String query);
	public Long updateEntities(String query);

}
