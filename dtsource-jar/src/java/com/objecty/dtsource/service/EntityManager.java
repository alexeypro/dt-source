package com.objecty.dtsource.service;

import java.util.List;

/**
 * 
 * Our Entity manager interface. Business interface provided for kind of "end-client",
 * which in our case is custom JSP tag library.
 * 
 * @author olexiy
 *
 */
public interface EntityManager {

	public List listEntities(String query, Long startFrom, Long stopAt);
	public Long sizeOfListEntities(String query);
	public Long updateEntities(String query);

}