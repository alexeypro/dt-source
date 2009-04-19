package com.objecty.dtsource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.util.ParamEncoder;
import org.apache.log4j.Logger;

import com.objecty.dtsource.tags.HibernateTag;
import com.objecty.dtsource.tags.JdbcTag;

/**
 * 
 * Class in which we will keep re-usable common methods for {@link HibernateTag} and {@link JdbcTag}.
 * Intentions of these methods are to help with routine operations by parsing, processing
 * and generating HQL/SQL queries.
 * 
 * @author olexiy
 * 
 */
public class ProcessingUtil {

    // Instantiate Logger class
	private static final Logger log = Logger.getLogger(ProcessingUtil.class);

    /**
	 * Returns the name of encoded DisplayTag's parameter specified as paramName.
	 * 
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @param paramName is a String with a name of parameter.
	 * @return Name of encoded DisplayTag's parameter.
	 */
	public String getDisplayTagParameter(final String tableId, final String paramName) {
		return new ParamEncoder(tableId).encodeParameterName(paramName);
	}

	/**
	 * Creates subquery (ORDER BY), which is the same for HQL and for SQL. In case if no sort is done yet (fields are
	 * null), we use default sorting field.
	 * 
	 * @param sortName String, by which name we sort {@link RequestDependent#getDisplayTagSortName(HttpServletRequest, String)}
	 * @param sortOrder String, how do we sort by this field {@link RequestDependent#getDisplayTagSortOrder(HttpServletRequest, String)}
	 * @param givenAlias String specified as "alias" in dtsource tag. 
	 * @param defaultsortName String specified as "defaultsortName" in dtsource tag.
	 * @return SQL/HQL subquery which need to be placed into the main query.
	 */
	public String createSortSubquery(final String sortName, final String sortOrder, final String givenAlias, final String defaultsortName) {
		String subquery = null;
		if ((sortName != null) && (sortOrder != null)) {
			subquery = givenAlias + "." + sortName + " " + (sortOrder.equalsIgnoreCase("1")?Constants.QL_ASC:Constants.QL_DESC);
		}
		if (subquery == null) {
			subquery = givenAlias + "." + defaultsortName;		
		}
		return subquery;
	}
	
	/**
	 * We need to count total elements in the list, even if we fetch only few of them. This is required to provide
	 * complete paging for all records. Because of that we need to create query just to count records. This method is
	 * for generating SQL query, but counting records in the table.
	 * 
	 * @param table String, name of database table, specified as "table" in dtsource tag.
	 * @param alias String specified as "alias" in dtsource tag.
	 * @return SQL query which need to be perfomed to count all records.
	 */
	public String createCountQuerySQL(final String table, final String alias) {
		return "SELECT COUNT(*) FROM " + table + " AS " + alias;
	}
	
	/**
	 * We need to count total elements in the list, even if we fetch only few of them. This is required to provide
	 * complete paging for all records. Because of that we need to create query just to count records. This method is
	 * for generating SQL query, but counting records in the table. This one also adds WHERE clause to fulfill
	 * criteria requirements.
	 * 
	 * @param table String, name of database table, specified as "table" in dtsource tag.
	 * @param alias String specified as "alias" in dtsource tag.
	 * @param whereSubstring is a part of WHERE clause
	 * @return SQL query which need to be performed to count all records.
	 */
	public String createCountQuerySQL(final String table, final String alias, final String whereSubstring) {
		return "SELECT COUNT(*) FROM " + table + " AS " + alias + " " + whereSubstring.trim();
	}
	
	/**
	 * We need an UPDATE query which will update required field with required value by locating this field using an ID.
	 * It is common as for SQL as to HQL query generation. We do not have last ; because with it HQL is not considered
	 * valid.
	 * 
	 * @param table String name of database table (think entity when using for HQL)
	 * @param field String name of the field which we are updating
	 * @param fieldValue Value of the field which we need to set for it
	 * @param id String name of the field which is an ID for the field we are updating
	 * @param idValue Value of the id field which we need to locate by
	 * @return SQL query which need to be performed to update all records
	 */
	public String createUpdateQuery(final String table, final String field, final String fieldValue, final String id, final String idValue) {
		return "UPDATE " + table + " SET " + field + " = '" + fieldValue + "' WHERE " + id + "='" + idValue + "'"; 
	}
	
	/**
	 * We need to count total elements in the list, even if we fetch only few of them. This is required to provide
	 * complete paging for all records. Because of that we need to create query just to count records. This method is
	 * for generating HQL query, but counting records of this entity.
	 * 
	 * @param entity String, name of mapped entity, specified as "entity" in dtsource tag.
	 * @param alias String specified as "alias" in dtsource tag.
	 * @return HQL query which need to be perfomed to count all records.
	 */
	public String createCountQueryHQL(final String entity, final String alias) {
		return "select count(" + alias + ") from " + entity + " as " + alias;
	}

	/**
	 * We need to count total elements in the list, even if we fetch only few of them. This is required to provide
	 * complete paging for all records. Because of that we need to create query just to count records. This method is
	 * for generating HQL query, but counting records of this entity. This one also adds WHERE clause to fulfill
	 * criteria requirements.
	 * 
	 * @param entity String, name of mapped entity, specified as "entity" in dtsource tag.
	 * @param alias String specified as "alias" in dtsource tag.
	 * @param whereSubstring is a WHERE clause
	 * @return HQL query which need to be perfomed to count all records.
	 */
	public String createCountQueryHQL(final String entity, final String alias, final String whereSubstring) {
		return "select count(" + alias + ") from " + entity + " as " + alias + " " + whereSubstring.trim();
	}
	
	/**
	 * Creates substring for criteria fetching. Generally, it's a part of 
	 * WHERE clause in SQL request. 
	 * 
	 * @param alias String specified as "alias" in dtsource tag.
	 * @param field String with field name by which we need to filter.
	 * @param value String which criteria to apply to the given field.
	 * @return SQL subquery which need to be added to WHERE clause.
	 */
	public String createCriteriaWhereSQL(final String alias, final String field, final String value) {
		String parsedValue = value.replace('"', Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_SINGLECHAR, Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_MANYCHARS, Constants.QL_MANYCHARS);
		return alias + "." + field + " LIKE \"" + parsedValue + "\"";
	}

	/**
	 * Creates substring for search fetching. Generally, it's a part of 
	 * WHERE clause in SQL request. 
	 * 
	 * @param alias String specified as "alias" in dtsource tag.
	 * @param field String with field name by which we need to search.
	 * @param from String which From to apply to the given field.
	 * @param to String which To to apply to the given field.
	 * @return SQL subquery which need to be added to WHERE clause.
	 */
	public String createSearchWhereSQL(final String alias, final String field, final String from, final String to) {

		String parsedFrom = "";
		String parsedTo = "";		

		if (from != null ) {
			parsedFrom = from.replace('\'', Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_SINGLECHAR, Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_MANYCHARS, Constants.QL_MANYCHARS);
		}

		if (to != null ) {
			parsedTo = to.replace('\'', Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_SINGLECHAR, Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_MANYCHARS, Constants.QL_MANYCHARS);
		}

		String whereQuery;

		if (parsedTo.length() < 1) {
			whereQuery = alias + "." + field + " LIKE \"" + parsedFrom + "\"";
		} else {
			whereQuery = alias + "." + field + " >= \"" + parsedFrom + "\" AND " + alias + "." + field + " <= \"" + parsedTo + "\"";
		}
		return whereQuery;
	}


	/**
	 * Creates substring for criteria fetching. Generally, it's a part of 
	 * WHERE clause in HQL request. 
	 * 
	 * @param alias String specified as "alias" in dtsource tag.
	 * @param field String with field name by which we need to filter.
	 * @param value String which criteria to apply to the given field.
	 * @return HQL subquery which need to be added to WHERE clause.
	 */
	public String createCriteriaWhereHQL(final String alias, final String field, final String value) {
		String parsedValue = value.replace('\'', Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_SINGLECHAR, Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_MANYCHARS, Constants.QL_MANYCHARS);
		return alias + "." + field + " like '" + parsedValue + "'";
	}

	/**
	 * Creates substring for search fetching. Generally, it's a part of 
	 * WHERE clause in HQL request. 
	 * 
	 * @param alias String specified as "alias" in dtsource tag.
	 * @param field String with field name by which we need to search.
	 * @param from String which From to apply to the given field.
	 * @param to String which To to apply to the given field.
	 * @return HQL subquery which need to be added to WHERE clause.
	 */
	public String createSearchWhereHQL(final String alias, final String field, final String from, final String to) {

		String parsedFrom = "";
		String parsedTo = "";		

		if (from != null ) {
			parsedFrom = from.replace('\'', Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_SINGLECHAR, Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_MANYCHARS, Constants.QL_MANYCHARS);
		}

		if (to != null ) {
			parsedTo = to.replace('\'', Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_SINGLECHAR, Constants.QL_SINGLECHAR).replace(Constants.WILDCARD_MANYCHARS, Constants.QL_MANYCHARS);
		}

		String whereQuery;

		if (parsedTo.length() < 1) {
			whereQuery = alias + "." + field + " like '" + parsedFrom + "'";
		} else {
			whereQuery = alias + "." + field + " >= '" + parsedFrom + "' AND " + alias + "." + field + " <= '" + parsedTo + "'";
		}
		return whereQuery;
	}

	
	/**
	 * Method to fetch out complete WHERE clause from query (HQL or SQL, does not matter)
	 * 
	 * @param query to fetch from
	 * @return fetched WHERE clause substring
	 */
	public String getCompleteWhereClause(final String query) {
		String completeWhereClause = "";
		int whereClauseStarts = query.toUpperCase().lastIndexOf(" WHERE".toUpperCase());
		int whereClauseStops = query.toUpperCase().lastIndexOf(" ORDER BY".toUpperCase());
		if (whereClauseStarts != -1) {
			if (whereClauseStops == -1) {
				completeWhereClause = query.substring(whereClauseStarts);
			} else { 
				completeWhereClause = query.substring(whereClauseStarts, whereClauseStops);
			}
			completeWhereClause = this.upperCaseIntoCorrectWords(completeWhereClause);
		}
		return completeWhereClause.trim();
	}
	
	/**
	 * Not too complex technically, but definately hard to understand. This method inserts WHERE ... or .. AND clause
	 * (in case if there is WHERE already!) into the specified query. 
	 * 
	 * @param query into which we need to insert/add WHERE clause
	 * @param criteriaRule our criteria for insering/adding into WHERE clause
	 * @return given query, but with inserted/added WHERE clause and our criteria
	 */
	public String insertCriteriaRule(final String query, final String criteriaRule) {
		// first we need to find where is ORDER BY, becasue we need to insert
		// criteria and where clause _before_ it. if not find - just add to the tail
		String fixedQuery = this.upperCaseIntoCorrectWords(query); 
		int orderByPlace = fixedQuery.lastIndexOf(" ORDER BY ");
		if (orderByPlace == -1) {
			orderByPlace = fixedQuery.length();		// add to the tail
			if (fixedQuery.charAt(orderByPlace-1) == ';') {
				orderByPlace--;						// tailed with ; step back
			}
		}
		String insertWhereClause;
		if (fixedQuery.lastIndexOf(" WHERE ") == -1) {
			// not found
			// that means that we need to add before '... ORDER BY ...' string ' WHERE ourcriteria '
			insertWhereClause = " WHERE ";
		} else {
			// found
			// that means that we need to add before '... ORDER BY ...' string '... AND ourcriteria ' 
			// because it will be a tail for existing WHERE clause
			insertWhereClause = " AND ";
		}
		StringBuffer sb = new StringBuffer(fixedQuery);
		sb.insert(orderByPlace, insertWhereClause + "(" + criteriaRule + ")");
		return sb.toString();
	}
	
	/**
	 * This method does output on it's own. It creates a bunch of "<a href></a>" links.
	 * For each criteria.
	 * 
	 * @param allCriterias Created list of parsed criterias
	 * @param oldParams String with old params generated by {@link RequestDependent#getURIwithOldParameters(javax.servlet.http.HttpServletRequest, String)}
	 * @param requestUri which request URI to use.
	 * @param currCriteriaValue current value of currently selected criteria (if any)
	 * @return String, which need to be gone to output (HTML)
	 */
	public String showHtmlWithCriterias(final List<HashMap> allCriterias, final String oldParams, final String requestUri, final String currCriteriaValue, final String tableId, final String fieldNameToFilter) {
		String out = "";
		String filterField = this.getDisplayTagParameter(tableId, Constants.PARAMETER_FILTER_FIELD);
		String filterValue = this.getDisplayTagParameter(tableId, Constants.PARAMETER_FILTER_CRITERIA);
		out += "<table id=\"" + tableId + "-" + Constants.CSS_TABLE + "\" class=\"" + Constants.CSS_TABLE + "\">" + "\n";
		out += "<thead>" + "\n";
		out += "<tr>" + "\n";
        for (HashMap elem : allCriterias) {
            String newParams = filterField + "=" + fieldNameToFilter + "&" + filterValue + "=" + elem.get(Constants.CRITERIAVALUE_KEY);
            String uri = requestUri + oldParams + newParams;
            if ((currCriteriaValue != null) && (currCriteriaValue.equalsIgnoreCase((String) elem.get(Constants.CRITERIAVALUE_KEY))))
            {
                out += "<th class=\"" + Constants.CSS_TH_SELECTED + "\">";
            } else {
                out += "<th class=\"" + Constants.CSS_TH + "\">";
            }
            out += "<a href=\"" + uri + "\">";
            out += elem.get(Constants.CRITERIALABEL_KEY);
            out += "</a>";
            out += "</th>";
        }
		out += "\n";
		out += "</tr>" + "\n";
		out += "</thead>" + "\n";
		out += "</table>" + "\n";
		return out;
	}
	
	/**
	 * Parses tag body and for each found criteria creates HashMap with key for Label of this criteria
	 * and key for Value of it. Then all these HashMaps will be placed in the List; and returned.
	 * 
	 * @param body String, tag body which need to be parsed
	 * @return List of HashMaps with Criteria's label and value in them
	 */
	public List<HashMap> parseFilterTagBody(final String body) {
		// here we will keep in HashMaps all criterias
		List<HashMap> allCriterias = new ArrayList<HashMap>(1);
		
		// we have each criteria separated by breakspaces (new lines).
		// let's parse first each criteria
		StringTokenizer token = new StringTokenizer(body.trim(), Constants.FILTERBODY_DELIMITERS, false);
		while (token.hasMoreTokens()) {
			String criteria = token.nextToken();
			// now we need to separate label and value of criteria
			StringTokenizer subtoken = new StringTokenizer(criteria, Constants.FILTERBODYCRITERIA_DELIMITERS, false);
			if (subtoken.countTokens() == 2) {
				HashMap<String,String> hm = new HashMap<String,String>();
				hm.put(Constants.CRITERIALABEL_KEY, subtoken.nextToken().trim());			// first comes label
				hm.put(Constants.CRITERIAVALUE_KEY, subtoken.nextToken().trim());			// then value
				allCriterias.add(hm);
			} else {
				// we do not have 2 tokens - something wrong, ignoring this criteria
			}
		}
		return allCriterias;
	}

	/**
	 * Parses tag body and places fields in the List; and returned.
	 * 
	 * @param body String, tag body which need to be parsed
	 * @return List of HashMap with fields
	 */
	public List<HashMap> parseSearchTagBody(final String body) {
		List<HashMap> allSearches = new ArrayList<HashMap>(1);
		
		// we have each field separated by breakspaces (new lines).
		// let's parse first each field
		StringTokenizer token = new StringTokenizer(body.trim(), Constants.SEARCHBODY_DELIMITERS, false);
		while (token.hasMoreTokens()) {
			String field = token.nextToken();

			HashMap<String,String> hm = new HashMap<String,String>();

			// now we need to separate label and value of field
			StringTokenizer subtoken = new StringTokenizer(field, Constants.SEARCHFIELD_DELIMITERS, false);
			if (subtoken.countTokens() == 2) {
				hm.put(Constants.SEARCHFIELD_KEY, subtoken.nextToken().trim());			// first comes label
				hm.put(Constants.SEARCHLABEL_KEY, subtoken.nextToken().trim());			// then value
			} else {
				// we do not have 2 tokens - we will show the field label as it is
				hm.put(Constants.SEARCHFIELD_KEY, field);			// first comes label
				hm.put(Constants.SEARCHLABEL_KEY, field);			// then value
			}

			allSearches.add(hm);
		}
		return allSearches;
	}

	/**
	 * This method does output on it's own. It creates a search form.
	 *
	 * @param allSearches Created list of parsed searches.
	 * @param oldParams String with old params generated by {@link RequestDependent#getURIwithOldParameters(javax.servlet.http.HttpServletRequest, String)}
	 * @param requestUri which request URI to use.
	 * @param currSearchField current value of currently selected search field (if any)
	 * @param currSearchFrom current value of currently selected search From (if any)
	 * @param currSearchTo current value of currently selected search To (if any)
     * @param classSubmit value of CSS class for submit button (if any)
	 * @return String, which need to be gone to output (HTML)
	 */
	public String showHtmlWithSearch(final List<HashMap> allSearches, final String oldParams, final String requestUri, final String currSearchField, final String currSearchFrom, final String currSearchTo, final String tableId, final String classSubmit, final String srchBanner) {

		PropertiesLoader pl = new PropertiesLoader();

		String out = "";

        String searchField = this.getDisplayTagParameter(tableId, Constants.PARAMETER_SEARCH_FIELD);
		String searchFrom = this.getDisplayTagParameter(tableId, Constants.PARAMETER_SEARCH_FROM);
		String searchTo = this.getDisplayTagParameter(tableId, Constants.PARAMETER_SEARCH_TO);

        // read the properties, if they doesn't exist, use the defaults
        String searchFieldLabel = pl.getProperty(Constants.SEARCH_FIELD_LABEL);
        String searchFromLabel = pl.getProperty(Constants.SEARCH_FROM_LABEL);
        String searchToLabel = pl.getProperty(Constants.SEARCH_TO_LABEL);
        String searchResetLabel = pl.getProperty(Constants.SEARCH_RESET_LABEL);

        String searchBanner = "";

        // srchBanner is an attribute passed to a table.  It overrides the sitewide
        // settings, read from properties
        if (srchBanner != null) {
            searchBanner = srchBanner;
        } else {
            searchBanner = pl.getProperty(Constants.SEARCH_BANNER);
        }

        if (searchFieldLabel == null) {
            searchFieldLabel = Constants.SEARCH_FIELD_LABEL_DEFAULT;
        }

        if (searchFromLabel == null) {
            searchFromLabel = Constants.SEARCH_FROM_LABEL_DEFAULT;
        }

        if (searchToLabel == null) {
            searchToLabel = Constants.SEARCH_TO_LABEL_DEFAULT;
        }

        if (searchResetLabel == null) {
            searchResetLabel = Constants.SEARCH_RESET_LABEL_DEFAULT;
        }

        if (searchBanner == null) {
            searchBanner = Constants.SEARCH_BANNER_DEFAULT;
        }

        log.debug("search.field.label = " + searchFieldLabel);
        log.debug("search.from.label = " + searchFromLabel);
        log.debug("search.to.label = " + searchToLabel);
        log.debug("search.banner = " + searchBanner);

        // ready HTML code for From: field of search FORM
        String htmlFrom = "<input class=\"" + Constants.CSS_SEARCH_INPUT + "\" type=\"text\" name=\"" + searchFrom + "\" MAXLENGTH=\"255\" value=\"" + ((currSearchFrom == null) ? "" : currSearchFrom.replace('<',' ').replace('>',' ')) + "\"/>";

        // ready HTML code for To: field of search FORM
        String htmlTo = "<input class=\"" + Constants.CSS_SEARCH_INPUT + "\" type=\"text\" name=\"" + searchTo + "\" MAXLENGTH=\"255\" value=\"" + ((currSearchTo == null) ? "" : currSearchTo.replace('<',' ').replace('>',' ')) + "\"/>";

        // ready HTML code for Search By: field of search FORM
        String htmlBy = "<select class=\"" + Constants.CSS_SEARCH_INPUT + "\" name=\"" + searchField + "\">";

        Iterator i = allSearches.iterator();

        while (i.hasNext()) {
			HashMap elem = (HashMap) i.next();

			if ((currSearchField != null) && (currSearchField.equalsIgnoreCase((String)elem.get(Constants.SEARCHFIELD_KEY)))) {
				htmlBy += "<option value=\"" + elem.get(Constants.SEARCHFIELD_KEY) + "\" selected>" + elem.get(Constants.SEARCHLABEL_KEY) + "</option>" + "\n";
			} else {
				htmlBy += "<option value=\"" + elem.get(Constants.SEARCHFIELD_KEY) + "\">" + elem.get(Constants.SEARCHLABEL_KEY) + "</option>" + "\n";
			}
		}

		htmlBy += "</select>";

        // ready HTML code for Submit button of search FORM
        String htmlSubmit = "";

        if ((classSubmit != null) && (!classSubmit.equals(""))) {
            htmlSubmit = "<input class=\"" + classSubmit + "\" type=\"submit\" value=\"Search\"/>\n";
        } else {
            htmlSubmit = "<input class=\"" + Constants.CSS_SEARCH_SUBMIT + "\" type=\"submit\" value=\"Search\"/>\n";
        }

        // Closes Submit FORM and opens Reset FORM

        // Adding old parameters which were converted from regular query string in the URI to
        // HTML code for FORM with <input type=hidden> values.
        if (oldParams != null) {
            htmlSubmit += "\n" + oldParams + "\n";
        }
        htmlSubmit += "</form>\n";
        htmlSubmit += "<form method=\"get\" name=\"" + tableId + "ResetForm\" action=\"" + requestUri + "\">" + "\n";

        // ***

        // ready HTML code for Reset button of search FORM
        String htmlReset = "<input class=\"" + Constants.CSS_SEARCH_RESET + "\" type=\"submit\" value=\"" + searchResetLabel + "\"/>\n";

        // Adding old parameters which were converted from regular query string in the URI to
        // HTML code for FORM with <input type=hidden> values.
        if (oldParams != null) {
            htmlReset += "\n" + oldParams + "\n";
        }

        // Object array
        // {0} From: field
        // {1} HTML code for From: field
        // {2} To: field
        // {3} HTML code for To: field
        // {4} Search by: field
        // {5} HTML code for Search by: field
        // {6} Submit button
        // {7} Reset button
        Object[] searchObjects = {
                searchFromLabel,
                htmlFrom,
                searchToLabel,
                htmlTo,
                searchFieldLabel,
                htmlBy,
                htmlSubmit,
                htmlReset
        };

        String readySearchBanner = MessageFormat.format(searchBanner, searchObjects);

        out += "<table id=\"" + tableId + "-" + Constants.CSS_SEARCH_TABLE + "\" class=\"" + Constants.CSS_SEARCH_TABLE + "\">" + "\n";

        out += "<thead>" + "\n";
		out += "<tr>" + "\n";

        out += "<form method=\"get\" name=\"" + tableId + "SearchForm\" action=\"" + requestUri + "\">" + "\n";

        out += "\n<!-- search.banner starts here -->\n";

        out += readySearchBanner;

        out += "\n<!-- search.banner ends here -->\n";

        out += "</form>" + "\n";

        // Reset FORM ends ---

        out += "\n";
        out += "</tr>" + "\n";
        out += "</thead>" + "\n";
        out += "</table>" + "\n";

		return out;
	}

	
	/**
	 * Converts any bad-typed words in given query into uppercased SQL-query acceptable
	 * words. Complete lis: ORDER BY, WHERE, LIKE, AND, OR, NOT
	 * Other ones we leave untouched.
	 * 
	 * @param str Give query
	 * @return String, which is fixed.
	 */
	public String upperCaseIntoCorrectWords(final String str) {
		String ret = str;
		ret = ret.replaceAll("\\s+[oO][rR][dD][eE][rR] [bB][yY]\\s+", " ORDER BY ");
		ret = ret.replaceAll("\\s+[wW][hH][eE][rR][eE]\\s+", " WHERE ");
		ret = ret.replaceAll("\\s+[lL][iI][kK][eE]\\s+", " LIKE ");
		ret = ret.replaceAll("\\s+[aA][nN][dD]\\s+", " AND ");
		ret = ret.replaceAll("\\s+[oO][rR]\\s+", " OR ");
		ret = ret.replaceAll("\\s+[nN][oO][tT]\\s+", " NOT ");
		return ret;
	}

}
