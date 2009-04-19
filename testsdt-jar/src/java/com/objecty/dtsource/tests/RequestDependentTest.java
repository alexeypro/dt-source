package com.objecty.dtsource.tests;

import com.objecty.dtsource.RequestDependent;
import com.objecty.dtsource.ProcessingUtil;
import com.objecty.dtsource.Constants;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.displaytag.tags.TableTagParameters;

import javax.servlet.http.HttpServletRequest;

public class RequestDependentTest extends TestCase {

    //private static Logger log = Logger.getLogger(RequestDependentTest.class);
    private RequestDependent rd;

    public void setUp() {
        // once per each test*()
        rd = new RequestDependent();
    }

    public void tearDown() {
        // once per each test*()
        rd = null;
    }

    // we want it to return real request, either our own specified (if it's not empty)
    public void testGetRequestURI() {
        HttpServletRequest req = new MockHttpServletRequest("GET", "/path/to/somewhere/zzz.jsp");
        assertEquals("/path/to/somewhere/zzz.jsp", rd.getRequestURI(req, ""));
        assertEquals("/nowhere/nothing/html/zzz.html", rd.getRequestURI(req, "/nowhere/nothing/html/zzz.html"));
    }

    public void testGetURIwithOldParametersExcludeSearch() {
        ProcessingUtil pu = new ProcessingUtil();
        String pageP = pu.getDisplayTagParameter("test", TableTagParameters.PARAMETER_PAGE);
        String sFieldP = pu.getDisplayTagParameter("test", Constants.PARAMETER_SEARCH_FIELD);
        String sFromP = pu.getDisplayTagParameter("test", Constants.PARAMETER_SEARCH_FROM);
        String sToP = pu.getDisplayTagParameter("test", Constants.PARAMETER_SEARCH_TO);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter(pageP, "page");
        req.addParameter(sFieldP, "search_field");
        req.addParameter(sFromP, "search_from");
        req.addParameter(sToP, "search_to");
        req.addParameter("param1", "value1");
        req.addParameter("param2", "value2");
        req.addParameter("param3", "value3");
        //String expected = "<input type=hidden name=\"param1\" value=\"value1\"/>\n" +
        //                  "<input type=hidden name=\"param2\" value=\"value2\"/>\n" +
        //                  "<input type=hidden name=\"param3\" value=\"value3\"/>\n";
        String result = rd.getURIwithOldParametersExcludeSearch(req, "test");
        //assertEquals(expected, result);
        assertFalse(result.contains("<input type=hidden name=\"" + pageP + "\" value=\"page\"/>"));
        assertFalse(result.contains("<input type=hidden name=\"" + sFieldP + "\" value=\"search_field\"/>"));
        assertFalse(result.contains("<input type=hidden name=\"" + sFromP + "\" value=\"search_from\"/>"));
        assertFalse(result.contains("<input type=hidden name=\"" + sToP + "\" value=\"search_to\"/>"));
        assertTrue(result.contains("<input type=hidden name=\"param1\" value=\"value1\"/>"));
        assertTrue(result.contains("<input type=hidden name=\"param2\" value=\"value2\"/>"));
        assertTrue(result.contains("<input type=hidden name=\"param3\" value=\"value3\"/>"));
        assertTrue(result.endsWith("\n"));
    }

    public void testGetURIwithOldParameters() {
        ProcessingUtil pu = new ProcessingUtil();
        String pageP = pu.getDisplayTagParameter("test", TableTagParameters.PARAMETER_PAGE);
        String fnameP = pu.getDisplayTagParameter("test", Constants.PARAMETER_FILTER_FIELD);
        String fcriteriaP = pu.getDisplayTagParameter("test", Constants.PARAMETER_FILTER_CRITERIA);
        String pagePother = pu.getDisplayTagParameter("other", TableTagParameters.PARAMETER_PAGE);
        String fnamePother = pu.getDisplayTagParameter("other", Constants.PARAMETER_FILTER_FIELD);
        String fcriteriaPother = pu.getDisplayTagParameter("other", Constants.PARAMETER_FILTER_CRITERIA);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter(pageP, "1");
        req.addParameter(fnameP, "2");
        req.addParameter(fcriteriaP, "3");
        req.addParameter("someother", "4");
        req.addParameter("different", "5");
        req.addParameter(pagePother, "6");
        req.addParameter(fnamePother, "7");
        req.addParameter(fcriteriaPother, "8");
        //String expected = "?someother=4&different=5&" + pagePother + "=6&" + fnamePother + "=7&" + fcriteriaPother + "=8&";
        String result = rd.getURIwithOldParameters(req, "test");
        //assertEquals(expected, result);
        assertFalse(result.contains(pageP + "=1"));
        assertFalse(result.contains(fnameP + "=2"));
        assertFalse(result.contains(fcriteriaP + "=3"));
        assertTrue(result.contains("someother=4"));
        assertTrue(result.contains("different=5"));
        assertTrue(result.contains(pagePother + "=6"));
        assertTrue(result.contains(fnamePother + "=7"));
        assertTrue(result.contains(fcriteriaPother + "=8"));
        assertTrue(result.startsWith("?"));
        assertTrue(result.endsWith("&"));
    }

    public void testGetDisplayTagStartNumber() {
        ProcessingUtil pu = new ProcessingUtil();
        String paramP = pu.getDisplayTagParameter("test", TableTagParameters.PARAMETER_PAGE);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter(paramP, "4");      // we are on the 4th page
        Long expected = (long)30;              // 1st page: 0-10, 2nd: 10-20, 3rd: 20-30; 4th: 30-...
        Long result = rd.getDisplayTagStartNumber(req, "test", (long)10);  // 10 elements per page
        assertEquals(expected, result);
    }

    public void testGetDisplayTagSortName() {
        ProcessingUtil pu = new ProcessingUtil();
        String paramP = pu.getDisplayTagParameter("test", TableTagParameters.PARAMETER_SORT);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter(paramP, "someField");
        String expected = "someField";
        String result = rd.getDisplayTagSortName(req, "test");
        assertEquals(expected, result);
    }

    public void testGetDisplayTagSortOrder() {
        ProcessingUtil pu = new ProcessingUtil();
        String paramP = pu.getDisplayTagParameter("test", TableTagParameters.PARAMETER_ORDER);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter(paramP, "ASC");
        String expected = "ASC";
        String result = rd.getDisplayTagSortOrder(req, "test");
        assertEquals(expected, result);
    }

    public void testGetDisplayTagCurrentCriteria() {
        ProcessingUtil pu = new ProcessingUtil();
        String paramP = pu.getDisplayTagParameter("test", Constants.PARAMETER_FILTER_CRITERIA);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter(paramP, "some_criteria");
        String expected = "some_criteria";
        String result = rd.getDisplayTagCurrentCriteria(req, "test");
        assertEquals(expected, result);
    }

    public void testGetDisplayTagCurrentSearchField() {
        ProcessingUtil pu = new ProcessingUtil();
        String paramP = pu.getDisplayTagParameter("test", Constants.PARAMETER_SEARCH_FIELD);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter(paramP, "some_search");
        String expected = "some_search";
        String result = rd.getDisplayTagCurrentSearchField(req, "test");
        assertEquals(expected, result);
    }

    public void testGetDisplayTagCurrentSearchFrom() {
        ProcessingUtil pu = new ProcessingUtil();
        String paramP = pu.getDisplayTagParameter("test", Constants.PARAMETER_SEARCH_FROM);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter(paramP, "some_search_from");
        String expected = "some_search_from";
        String result = rd.getDisplayTagCurrentSearchFrom(req, "test");
        assertEquals(expected, result);
    }

    public void testGetDisplayTagCurrentSearchTo() {
        ProcessingUtil pu = new ProcessingUtil();
        String paramP = pu.getDisplayTagParameter("test", Constants.PARAMETER_SEARCH_TO);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter(paramP, "some_search_to");
        String expected = "some_search_to";
        String result = rd.getDisplayTagCurrentSearchTo(req, "test");
        assertEquals(expected, result);
    }

}