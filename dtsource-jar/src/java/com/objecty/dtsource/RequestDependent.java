package com.objecty.dtsource;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.tags.TableTagParameters;

import com.objecty.dtsource.tags.HibernateTag;
import com.objecty.dtsource.tags.JdbcTag;

/**
 * 
 * Class in which we will keep re-usable common methods for {@link HibernateTag} and {@link JdbcTag}.
 * Intentions of these methods are to help with routine operations by fetching some
 * variables from request scope only. They all depend on HttpServletRequest
 * 
 * @author olexiy
 * 
 */
public class RequestDependent {

	/**
	 * Will return so called "start number" - number from which you need to start fetching elements.
	 * They have to be shown on this page starting from this number. It depends on which page is currently
	 * in use and we get this information with the help of DisplayTag's parameters.
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from request scope
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @param pageSize Long, means how many items per page need to be displayed.
	 * @return Long value, from which number we need to start.
	 */
	public Long getDisplayTagStartNumber(final HttpServletRequest request, final String tableId, final Long pageSize) {
		Long startNumber = (long)0;
		String paramPage = new ProcessingUtil().getDisplayTagParameter(tableId, TableTagParameters.PARAMETER_PAGE); 
		String obj = request.getParameter(paramPage);
		if (obj != null) {
			startNumber = (Long.parseLong(obj) - 1) * pageSize;
		}
		return startNumber;
	}

	/**
	 * Method to dermine by which field name we need to do sorting. This information is gathered with the
	 * help of DisplayTag, from it's parameters. It's a String value, one of "sortName" values which you
	 * enter in the display tag.
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from request scope
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @return String value, name of field by which we sort.
	 */
	public String getDisplayTagSortName(final HttpServletRequest request, final String tableId) {
		String paramSort = new ProcessingUtil().getDisplayTagParameter(tableId, TableTagParameters.PARAMETER_SORT);
		return request.getParameter(paramSort);
	}

	/**
	 * Method to dermine which sorting we need to do - ascending or descending. This information is gathered with 
	 * the help of DisplayTag, from it's parameters. It's a String value, one of "sortName" values which you
	 * enter in the display tag.
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from request scope
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @return String value, which is 1 for ASCending, and 2 for DESCending order.
	 */
	public String getDisplayTagSortOrder(final HttpServletRequest request, final String tableId) {
		String paramOrder = new ProcessingUtil().getDisplayTagParameter(tableId, TableTagParameters.PARAMETER_ORDER);
		return request.getParameter(paramOrder);
	}
	
	/** 
	 * Crap method to fetch current (if any) value of selected criteria.
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from it.
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @return value of current selected criteria (or null if none)
	 */
	public String getDisplayTagCurrentCriteria(final HttpServletRequest request, final String tableId) {
		String paramCrit = new ProcessingUtil().getDisplayTagParameter(tableId, Constants.PARAMETER_FILTER_CRITERIA);
		return request.getParameter(paramCrit);
	}

	/** 
	 * Crap method to fetch current (if any) value of selected field.
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from it.
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @return value of current selected field (or null if none)
	 */
	public String getDisplayTagCurrentSearchField(final HttpServletRequest request, final String tableId) {
		String paramSearchField = new ProcessingUtil().getDisplayTagParameter(tableId, Constants.PARAMETER_SEARCH_FIELD);
		return request.getParameter(paramSearchField);
	}

	/** 
	 * Crap method to fetch current (if any) value of selected From.
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from it.
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @return value of current From (or null if none)
	 */
	public String getDisplayTagCurrentSearchFrom(final HttpServletRequest request, final String tableId) {
		String paramSearchFrom = new ProcessingUtil().getDisplayTagParameter(tableId, Constants.PARAMETER_SEARCH_FROM);
		return request.getParameter(paramSearchFrom);
	}

	/** 
	 * Crap method to fetch current (if any) value of selected To.
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from it.
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @return value of current To (or null if none)
	 */
	public String getDisplayTagCurrentSearchTo(final HttpServletRequest request, final String tableId) {
		String paramSearchTo = new ProcessingUtil().getDisplayTagParameter(tableId, Constants.PARAMETER_SEARCH_TO);
		return request.getParameter(paramSearchTo);
	}

	/**
	 * Works with this.requestURI, and depending on was it specified and how
	 * it creates real request URI which needed to be used.
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from it.
	 * @param requestURI provided request uri by user as a tag lib argument.
	 * @return Real request URI to be used in <a href=""></a> depending on what you specified.
	 */
	public String getRequestURI(final HttpServletRequest request, final String requestURI) {
		String realURItoUse;
		if (requestURI == null) {
			// nothing specified, use default
			realURItoUse = "";
		} else {
			if (requestURI.equalsIgnoreCase("")) {
				// empty one, but specified, use current from request
				realURItoUse = request.getRequestURI();
			} else {
				// you specified something for real!
				realURItoUse = requestURI;
			}
		}
		return realURItoUse;
	}


    /**
	 * It returns part of URI tailing with ? (if empty) either & sign, with list of names and values
	 * of all old parameters, except PAGE, FILTER field name and FILTER criteria value for exactly
	 * table's ID . 
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from request scope
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @return URI, tailing with ? (if empty) either with & sign.
	 */
	public String getURIwithOldParameters(final HttpServletRequest request, final String tableId) {
		String oldParameters = "?";

		// we load parameters, except parameter of page related to exactly this
		// table, and of course previous filter paramters related to the table
		ProcessingUtil pu = new ProcessingUtil();
		String pageParam = pu.getDisplayTagParameter(tableId, TableTagParameters.PARAMETER_PAGE);
		String filternameParam = pu.getDisplayTagParameter(tableId, Constants.PARAMETER_FILTER_FIELD);
		String filtercriteriaParam = pu.getDisplayTagParameter(tableId, Constants.PARAMETER_FILTER_CRITERIA);

		// here we do processing
		// with parameters which we TAKE we create URI, adding them to it
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String keyx = (String) e.nextElement();
			String valx = request.getParameter(keyx);
			if (keyx.equalsIgnoreCase(pageParam) || keyx.equalsIgnoreCase(filtercriteriaParam) || keyx.equalsIgnoreCase(filternameParam)) {
			} else {
				oldParameters += keyx + "=" + valx + "&";
			}
		}
		return oldParameters;
	}

	/**
	 * It returns String with part of FORM including list of names and values (hidden)
	 * of all old parameters, except PAGE and SEARCH Field, From and To values for exactly
	 * table's ID.  As far as submitting a FORM with GET to the URI with already query string
	 * inside results as clearing query string, we need to have this workaround.
	 * 
	 * @param request is just a {@link HttpServletRequest}, we need to fetch information from request scope
	 * @param tableId is a String name of table, it's an "id" value for dtsource and/or display tags.
	 * @return Part of FROM with HTML code for hidden values.
	 */
	public String getURIwithOldParametersExcludeSearch(final HttpServletRequest request, final String tableId) {
		String oldParameters = "";

		// we load parameters, except parameter of page related to exactly this
		// table, and of course previous filter paramters related to the table
		ProcessingUtil pu = new ProcessingUtil();
		String pageParam = pu.getDisplayTagParameter(tableId, TableTagParameters.PARAMETER_PAGE);
		String searchFieldParam = pu.getDisplayTagParameter(tableId, Constants.PARAMETER_SEARCH_FIELD);
		String searchFromParam = pu.getDisplayTagParameter(tableId, Constants.PARAMETER_SEARCH_FROM);
		String searchToParam = pu.getDisplayTagParameter(tableId, Constants.PARAMETER_SEARCH_TO);

		// here we do processing
		// with parameters which we TAKE we create URI, adding them to it
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String keyx = (String) e.nextElement();
			String valx = request.getParameter(keyx);
			if (keyx.equalsIgnoreCase(pageParam) || keyx.equalsIgnoreCase(searchFieldParam) || keyx.equalsIgnoreCase(searchFromParam) || keyx.equalsIgnoreCase(searchToParam)) {
			} else {
                // Remove wrong staff
                valx = valx.replace('<',' ').replace('>',' ').replace('\'',' ').replace('(',' ').replace(')',' ');
                oldParameters += "<input type=hidden name=\"" + keyx + "\" value=\"" + valx + "\"/>" + "\n";
			}
		}
		return oldParameters;
	}


	
}
