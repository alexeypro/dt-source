package com.objecty.dtsource.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.displaytag.util.ParamEncoder;

import com.objecty.dtsource.Constants;
import com.objecty.dtsource.ProcessingUtil;

public class ProcessingUtilTest extends TestCase {
    ProcessingUtil pu;

    public static Test suite() {
        TestSuite suite = new TestSuite(ProcessingUtilTest.class);
        return new TestSetup(suite) {
            protected void setUp() {
                // once per suite
            }

            protected void tearDown() {
                // once per suite
            }
        };
    }

    public void setUp() {
        // once per each test*()
        pu = new ProcessingUtil();
    }

    public void tearDown() {
        // once per each test*()
        pu = null;
    }

    // tests if we are using DisplayTag's encoding for param in this getDisplayTagParameter method
    public void testGetDisplayTagParameter() {
        String tableId = "testTableName";
        String paramName = "SomeParamName";
        String expected = new ParamEncoder(tableId).encodeParameterName(paramName);
        String result = pu.getDisplayTagParameter(tableId, paramName);
        assertEquals(expected, result);
    }

    // tests if we are using creating correct sorting subquery
    public void testCreateSortSubquery() {
        String expected;
        String result;

        expected = "x.sortableField " + Constants.QL_ASC;
        result = pu.createSortSubquery("sortableField", "1", "x", "defaultSortableField");
        assertEquals(expected, result);

        expected = "y.defaultSortableField";
        result = pu.createSortSubquery(null, "1", "y", "defaultSortableField");
        assertEquals(expected, result);

        expected = "z.defaultSortableField";
        result = pu.createSortSubquery(null, null, "z", "defaultSortableField");
        assertEquals(expected, result);

        expected = "o.sortableField " + Constants.QL_DESC;
        result = pu.createSortSubquery("sortableField", "2", "o", "defaultSortableField");
        assertEquals(expected, result);
    }

    // test if it returns exactly count SQL command like we expect (upper case, right)
    public void testCreateCountQuerySQL() {
        String expected = "SELECT COUNT(*) FROM zigmund AS z";
        String result = pu.createCountQuerySQL("zigmund", "z");
        assertEquals(expected, result);
    }

    // test if it returns exactly count SQL command with Where clause like we expect
    public void testCreateCountQuerySQLwithWhere() {
        String expected = "SELECT COUNT(*) FROM bermuda AS b WHERE b.berName LIKE 'turnados'";
        String result = pu.createCountQuerySQL("bermuda", "b", " WHERE b.berName LIKE 'turnados' ");
        assertEquals(expected, result);
    }


    // test if it returns exactly count HQL command like we expect (lower case)
    public void testCreateCountQueryHQL() {
        String expected = "select count(z) from zigmund as z";
        String result = pu.createCountQueryHQL("zigmund", "z");
        assertEquals(expected, result);
    }

    // test if it returns exactly count HQL command with Where clause like we expect
    public void testCreateCountQueryHQLwithWhere() {
        String expected = "select count(b) from bermuda as b where b.berName like 'turnados'";
        String result = pu.createCountQueryHQL("bermuda", "b", " where b.berName like 'turnados' ");
        assertEquals(expected, result);
    }

    // check if it fetches correctly WHERE clause from any query
    public void testGetCompleteWhereClause() {
        String query;
        String expected;
        String result;

        query = "Select blablah from hanugha As h  Where blahblah Like 'zzz%' order By blahblah DESc;";
        expected = "WHERE blahblah LIKE 'zzz%'";
        result = pu.getCompleteWhereClause(query);
        assertEquals(expected, result);

        query = "select blah-bla from brandont as b   whEre xon = '1' and   onx = '2'";
        expected = "WHERE xon = '1' AND onx = '2'";
        result = pu.getCompleteWhereClause(query);
        assertEquals(expected, result);
    }

    // test how it inserts our criteria into query
    public void testInsertCriteriaRule() {
        String query;
        String expected;
        String result;

        query = "select tundra from Blambra a WhErE kundatsvon='12' order by Kilandrop Asc";
        expected = "select tundra from Blambra a WHERE kundatsvon='12' AND (criteria) ORDER BY Kilandrop Asc";
        result = pu.insertCriteriaRule(query, "criteria");
        assertEquals(expected, result);

        query = "select tundra from BlambraBumba a   oRdEr by	Kilandrop Asc";
        expected = "select tundra from BlambraBumba a WHERE (criteria) ORDER BY Kilandrop Asc";
        result = pu.insertCriteriaRule(query, "criteria");
        assertEquals(expected, result);

        query = "select tundra from Bahturam a;";
        expected = "select tundra from Bahturam a WHERE (criteria);";
        result = pu.insertCriteriaRule(query, "criteria");
        assertEquals(expected, result);
    }

    // test how our table with criterias will look like
    public void testShowHtmlWithCriterias() {
        List<HashMap> allCriterias = new ArrayList<HashMap>();
        String tableId = "tundra";                                // static, not a big deal, stays the same
        String oldParams = "?old1=1&old2=2&old3=3&";                // same
        String requestUri = "";                                     // same
        String fieldNameToFilter = "fieldname";                    // same
        String filterField = pu.getDisplayTagParameter(tableId, Constants.PARAMETER_FILTER_FIELD);        // assume they are tested
        String filterValue = pu.getDisplayTagParameter(tableId, Constants.PARAMETER_FILTER_CRITERIA);        // above

        String currCriteriaValue = "2";
        String expected;
        String result;

        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put(Constants.CRITERIALABEL_KEY, "one");
        hm.put(Constants.CRITERIAVALUE_KEY, "1");
        allCriterias.add(hm);
        hm = new HashMap<String, String>();
        hm.put(Constants.CRITERIALABEL_KEY, "two");
        hm.put(Constants.CRITERIAVALUE_KEY, "2");
        allCriterias.add(hm);

        expected = "<table id=\"" + tableId + "-" + Constants.CSS_TABLE + "\" class=\"" + Constants.CSS_TABLE + "\">\n" +
                "<thead>\n<tr>\n" +
                "<th class=\"" + Constants.CSS_TH + "\"><a href=\"" + requestUri + oldParams + filterField + "=" + fieldNameToFilter + "&" + filterValue + "=1\">one</a></th>" +
                "<th class=\"" + Constants.CSS_TH_SELECTED + "\"><a href=\"" + requestUri + oldParams + filterField + "=" + fieldNameToFilter + "&" + filterValue + "=" + currCriteriaValue + "\">two</a></th>" +
                "\n</tr>\n</thead>\n</table>\n";
        result = pu.showHtmlWithCriterias(allCriterias, oldParams, requestUri, currCriteriaValue, tableId, fieldNameToFilter);
        assertEquals(expected, result);
    }

    // test how body of tag with user inputted criterias is parsed
    public void testParseFilterTagBody() {
        String body;
        List<HashMap> expected = new ArrayList<HashMap>();

        body = "\n\t\n\t" +
                "\nA| A*\n\t" +
                "B |B*B*\t C|C***";

        HashMap<String, String> hm1 = new HashMap<String, String>();
        hm1.put(Constants.CRITERIALABEL_KEY, "A");
        hm1.put(Constants.CRITERIAVALUE_KEY, "A*");
        HashMap<String, String> hm2 = new HashMap<String, String>();
        hm2.put(Constants.CRITERIALABEL_KEY, "B");
        hm2.put(Constants.CRITERIAVALUE_KEY, "B*B*");
        HashMap<String, String> hm3 = new HashMap<String, String>();
        hm3.put(Constants.CRITERIALABEL_KEY, "C");
        hm3.put(Constants.CRITERIAVALUE_KEY, "C***");
        expected.add(hm1);
        expected.add(hm2);
        expected.add(hm3);

        List<HashMap> result = pu.parseFilterTagBody(body);
        assertEquals(expected, result);
    }

    // test conversion of words into proper SQL/HQL commands
    public void testUpperCaseIntoCorrectWords() {
        String expected;
        String result;

        expected = " ORDER	BY something";
        result = pu.upperCaseIntoCorrectWords(" ORDER	BY something");
        assertEquals(expected, result);

        expected = " ORDER BY zuka";
        result = pu.upperCaseIntoCorrectWords("	OrDeR By		zuka");
        assertEquals(expected, result);

        expected = " ORDER BY x WHERE y LIKE z AND o OR j NOT p";
        result = pu.upperCaseIntoCorrectWords("	orDeR By	  x wherE y	LikE			z		And o oR	j Not p");
        assertEquals(expected, result);
    }

}