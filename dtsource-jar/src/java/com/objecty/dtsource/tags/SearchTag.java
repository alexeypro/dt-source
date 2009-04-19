package com.objecty.dtsource.tags;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import com.objecty.dtsource.ProcessingUtil;
import com.objecty.dtsource.RequestDependent;

/**
 * 
 * Implementation of dtsource:filter tag.
 * Makes line with filter choices. On click on each prepares 
 * argument, places in GET request. Also, it loads all other
 * arguments, and passes them as well.
 * 
 * @author olexiy
 *
 */
public class SearchTag extends BodyTagSupport {

	// Instantiate Logger class
	private static final Logger log = Logger.getLogger(SearchTag.class);
		
	private static final long serialVersionUID = -421827403928138422L;
	
	private String id;				// name of the table
	private String requestURI;		// not required, optional, URI
    private String classSubmit;		// not required, optional, CSS class for submit button
    private String searchBanner;	// not required, optional, text with search banner for formatting
	private String tagBody;			// body, globalized, we set it in doAfterBody, but process in doEndTag
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setTagBody(String tagBody) {
		this.tagBody = tagBody;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

    public void setClassSubmit(String classSubmit) {
		this.classSubmit = classSubmit;
	}

    public void setSearchBanner(String searchBanner) {
		this.searchBanner = searchBanner;
	}

    public int doStartTag() throws JspException {
		// do not have too much to do here
		return EVAL_BODY_BUFFERED;
	}

	public int doAfterBody() throws JspException {
		BodyContent body = getBodyContent(); 
		this.tagBody = body.getString();		
		return SKIP_BODY; 
	}

	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		JspWriter out = pageContext.getOut();

		ProcessingUtil pu = new ProcessingUtil();
		RequestDependent rd = new RequestDependent();

		List<HashMap> allSearches = pu.parseSearchTagBody(this.tagBody);

		String currentSearchField = rd.getDisplayTagCurrentSearchField(request, this.id);
		log.debug("currentSearchField=" + currentSearchField);

		String currentSearchFrom = rd.getDisplayTagCurrentSearchFrom(request, this.id);
		log.debug("currentSearchFrom=" + currentSearchFrom);

		String currentSearchTo = rd.getDisplayTagCurrentSearchTo(request, this.id);
		log.debug("currentSearchTo=" + currentSearchTo);

		String realRequestURI = rd.getRequestURI(request, this.requestURI);
		log.debug("realRequestURI=" + realRequestURI);

        String URIwithOldParameters = rd.getURIwithOldParametersExcludeSearch(request, this.id);

		String toPrint = pu.showHtmlWithSearch(allSearches, URIwithOldParameters, realRequestURI, currentSearchField, currentSearchFrom, currentSearchTo, this.id, classSubmit, searchBanner);
		
		try {
			out.println(toPrint);
		} catch (Exception e) {
			throw new JspException("Exception during output " + e.getMessage());
		}
			
		return EVAL_PAGE;
	}


}
