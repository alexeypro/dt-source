package com.objecty.dtsource.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.objecty.dtsource.Constants;

/**
 * 
 * Implementation of dtsource:inplaceEditor tag.
 * Outputs JavaScript info for using Script.Aculo.Us's InPlaceEditor 
 * 
 * @author olexiy
 *
 */
public class InPlaceEditorTag extends SimpleTagSupport {
	
	private static final Logger log = Logger.getLogger(InPlaceEditorTag.class);

	private String table;				// name of the table, or entity
	private String entity;				// name of the entity, in case of hibernate
	
	private String propertyId;			// id field, by which we identify the field to update
	private String property;			// actual field, it's name
	private String valueId;				// value field, by which we show field
	private String value;				// value of actual field, it's name	
	private String serviceURI;			// optional, not required, but you define your own updater
	private String serviceRequest;		// optional, not required, but you define your own updater request	
	private String errorMessage;		// optional, error message preceding the real message
	private String highlightcolor;		// optional, we have E0EFFF by default
	private String highlightendcolor;	// optional, we have FFFFFF by default
	
	public String getValue() {
		return value;
	}

	public String getServiceRequest() {
		return serviceRequest;
	}

	public void setServiceRequest(String serviceRequest) {
		this.serviceRequest = serviceRequest;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getHighlightcolor() {
		return highlightcolor;
	}

	public void setHighlightcolor(String highlightcolor) {
		this.highlightcolor = highlightcolor;
	}

	public String getHighlightendcolor() {
		return highlightendcolor;
	}

	public void setHighlightendcolor(String highlightendcolor) {
		this.highlightendcolor = highlightendcolor;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getServiceURI() {
		return serviceURI;
	}

	public void setServiceURI(String serviceURI) {
		this.serviceURI = serviceURI;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}	

	public void doTag() throws JspException { 
		log.debug("We are in the InPlaceEditorTag now...");
		PageContext pageContext = (PageContext) getJspContext(); 
		JspWriter out = pageContext.getOut(); 
		if ((this.getErrorMessage() == null) || (this.getErrorMessage().length() <= 0))				this.setErrorMessage(Constants.INPLACE_EDITOR_ERRORMESSAGE);
		if ((this.getHighlightcolor() == null) || (this.getHighlightcolor().length() <= 0))			this.setHighlightcolor(Constants.INPLACE_EDITOR_HIGHLIGHTCOLOR);		
		if ((this.getHighlightendcolor() == null) || (this.getHighlightendcolor().length() <= 0))	this.setHighlightendcolor(Constants.INPLACE_EDITOR_HIGHLIGHTENDCOLOR);
		boolean useTable = false;
		boolean useEntity = false;
		if ((this.getTable() != null) && (this.getTable().length() > 0))	useTable = true;
		if ((this.getEntity() != null) && (this.getEntity().length() > 0))	useEntity = true;
		
		log.debug("Checking for using table or entity...");
		if ((useTable) && (useEntity)) {
			try {
				out.println("Please use only TABLE or ENTITY tag, not both of them");
				log.error("Please use only TABLE or ENTITY tag, not both of them");
			} catch (Exception e) {
				log.error("Exception happened while doing output - " + e.getMessage()); 
			}
		} else {
			if ((this.getServiceURI() == null) || (this.getServiceURI().length() <= 0)) {
				if (useTable)	this.setServiceURI(Constants.INPLACE_EDITOR_JDBC_SERVICEURI);
				if (useEntity)	this.setServiceURI(Constants.INPLACE_EDITOR_HIBERNATE_SERVICEURI);
			}
		}
		
		log.debug("Actual output now...");
		try { 
			String requestLine = "'t=" + ((useTable)?this.getTable():this.getEntity()) + "&vid=" + this.getValueId() + "&fid=" + this.getPropertyId() + "&f=" + this.getProperty() + "&v=' + escape(value);";
			if ((this.getServiceRequest() == null) || (this.getServiceRequest().length() <=0))		this.setServiceRequest("'" + requestLine + "' + escape(value);");
			log.debug("request line will be [" + this.getServiceRequest() + "]");
			out.println("<div id=\"" + this.getPropertyId() + "Editable" + this.getValueId() + "\">" + this.getValue() + "</div>");
			out.println("<script type=\"text/javascript\">");
			out.print("var editor = new Ajax.InPlaceEditor('" + this.getPropertyId() + "Editable" + this.getValueId() + "', '" + this.getServiceURI() + "',"); 
			out.print(" { "); 
			out.print(" callback: function(form, value) { return " + this.getServiceRequest() + " },");
			out.print(" onFailure: function(transport) { alert('" + this.getErrorMessage() + " \\n\\n'  + transport.responseText.stripTags()); },");
			out.print(" highlightcolor: \"" + this.getHighlightcolor() + "\",");
			out.print(" highlightendcolor: \"" + this.getHighlightendcolor() + "\"");
			out.println(" });");
			out.println("</script>");
		} catch (Exception e) { 
			log.error("Exception happened while doing output - " + e.getMessage()); 
		} 
	} 


}
