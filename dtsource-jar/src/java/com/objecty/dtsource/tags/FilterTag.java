package com.objecty.dtsource.tags;

import com.objecty.dtsource.ProcessingUtil;
import com.objecty.dtsource.RequestDependent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.HashMap;
import java.util.List;

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
public class FilterTag extends BodyTagSupport {

	private static final long serialVersionUID = -421827403928138422L;
	
	private String id;				// name of the table
	private String field;				// field, for which we need do filtering
	private String requestURI;		// not required, optional, URI
	private String tagBody;			// body, globalized, we set it in doAfterBody, but process in doEndTag
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setField(String field) {
		this.field = field;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public void setTagBody(String tagBody) {
		this.tagBody = tagBody;
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

		List<HashMap> allCriterias = pu.parseFilterTagBody(this.tagBody);
		String currentCriteria = rd.getDisplayTagCurrentCriteria(request, this.id);
		String realRequestURI = rd.getRequestURI(request, this.requestURI);
		String URIwithOldParameters = rd.getURIwithOldParameters(request, this.id);
		String toPrint = pu.showHtmlWithCriterias(allCriterias, URIwithOldParameters, realRequestURI, currentCriteria, this.id, this.field);
		
		try {
			out.println(toPrint);
		} catch (Exception e) {
			throw new JspException("Exception during output " + e.getMessage());
		}
			
		return EVAL_PAGE;
	}


}
