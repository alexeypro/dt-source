package com.objecty.dtsource;

/**
 * 
 * We keep constants here, nothing else.
 * 
 * @author olexiy
 * 
 */
public class Constants {

    private Constants() {
        // preven of instantioning
    }
	
	// these are tags which you can use inside your query
	public final static String TAG_ALIAS = "~alias~";
	public final static String TAG_SORT = "~sort~";
	public final static String TAG_ENTITY = "~entity~";
	public final static String TAG_TABLE = "~table~";
	
	// except setting necessary information, this tag will set also 
	// extra values - for table id and pagesize value.
	public final static String ATTR_PAGESIZE = "pagesize";
	public final static String ATTR_TABLEID = "tableid";
	
	// this is how EntityManager defined in Spring's config for Hibernate and JDBC
	public final static String ENTITYMANAGER_HIBERNATE_NAME = "entityHibernateManager";
	public final static String ENTITYMANAGER_JDBC_NAME = "entityJdbcManager";
	
	// this is how we mark ascending/descending order in QL's - HQL and SQL
	public final static String QL_ASC = "ASC";
	public final static String QL_DESC = "DESC";
	// this is how we exchange query wildcards to HQL and SQL (first part is how you can use)
	public final static char WILDCARD_SINGLECHAR = '?';
	public final static char WILDCARD_MANYCHARS = '*';
	public final static char QL_SINGLECHAR = '_';
	public final static char QL_MANYCHARS = '%';
	
	// these delimiters are ignored for common filter body
	// this is how (with which delimiters) each criteria can be separated from other
	public final static String FILTERBODY_DELIMITERS = "\t\n\r";
	// this is how each criteria Label is separated from Value
	public final static String FILTERBODYCRITERIA_DELIMITERS = "|";

	// Delimiters for Search screen:
	// this is how (with which delimiters) each field can be separated from other
	public final static String SEARCHBODY_DELIMITERS = "\t\n\r";
	// this is how each field and shown name of field are separated
	public final static String SEARCHFIELD_DELIMITERS = "|";
	
	// key for label and for value of criteria, under which we will keep
	// them in hashmaps
	public final static String CRITERIALABEL_KEY = "label";
	public final static String CRITERIAVALUE_KEY = "value";

	// key for field and for shown name of field, under which we will keep
	// them in hashmaps
	public final static String SEARCHFIELD_KEY = "field";
	public final static String SEARCHLABEL_KEY = "label";
	
	// parameter in request scope, for filtering - field name and
	// exact criteria
	public final static String PARAMETER_FILTER_FIELD = "ff";
	public final static String PARAMETER_FILTER_CRITERIA = "fc";

	// parameter in request scope, for searching - field name and
	// from and to
	public final static String PARAMETER_SEARCH_FIELD = "sf";
	public final static String PARAMETER_SEARCH_FROM = "sr";
	public final static String PARAMETER_SEARCH_TO = "st";
	
	// css style names
	public final static String CSS_TABLE = "filters";
	public final static String CSS_TH = "criteria";
	public final static String CSS_TH_SELECTED = "criteria-selected";
	public final static String CSS_SEARCH_TABLE = "search";
    public final static String CSS_SEARCH_INPUT = "search-input";
    public final static String CSS_SEARCH_SUBMIT = "search-submit";
    public static final String CSS_SEARCH_RESET = "search-reset";

	// name of the default properties file name
	public static final String DEFAULT_PROPERTIES = "dtsource.properties";

	// keys for properties
	public static final String SEARCH_FIELD_LABEL = "search.field.label";
	public static final String SEARCH_FIELD_LABEL_DEFAULT = "Search By Field:";
	public static final String SEARCH_FROM_LABEL = "search.from.label";
	public static final String SEARCH_FROM_LABEL_DEFAULT = "From:";
	public static final String SEARCH_TO_LABEL = "search.to.label";
	public static final String SEARCH_TO_LABEL_DEFAULT = "To:";
    public static final String SEARCH_RESET_LABEL = "search.reset.label";
    public static final String SEARCH_RESET_LABEL_DEFAULT = "Reset";
    public static final String SEARCH_BANNER = "search.banner";
    public static final String SEARCH_BANNER_DEFAULT =
            "<td> {0} {1} </td>\n" +
            "<td> {2} {3} </td>\n" +
            "<td> {4} {5} </td>\n" +
            "<td> {6} </td>\n" +
            "<td> {7} </td>\n";

    // defaults for inPlaceEditor
    public static final String INPLACE_EDITOR_JDBC_SERVICEURI = "inPlaceUpdaterJdbc.service";
    public static final String INPLACE_EDITOR_HIBERNATE_SERVICEURI = "inPlaceUpdaterHibernate.service";
    public static final String INPLACE_EDITOR_ERRORMESSAGE = "Error happened:";
    public static final String INPLACE_EDITOR_HIGHLIGHTCOLOR = "#E0EFFF";
    public static final String INPLACE_EDITOR_HIGHLIGHTENDCOLOR = "#FFFFFF";
    public static final String INPLACE_EDITOR_ERROR_RETURN = "Error!";
    
}
